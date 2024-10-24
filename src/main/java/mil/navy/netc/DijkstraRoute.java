/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mil.navy.netc;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DijkstraRoute extends RouteBuilder {

    @Value("${test.file.path:/default/tmp/testfile.txt}")
    private String testFilePath;

    @Value("${route.id:dijkstra}")
    private String routeId;

    @Value("${mensaje:burrito}")
    private String lamesa;

    @Override
    public void configure() throws Exception {
        // Define the set of allowed third-field values
        Set<String> allowedQueues = Set.of(
                "C_WC_PM_PE",
                "C_WC_PM_TLA",
                "C_WC_MCTIMS_PE",
                "C_WC_MCTIMS_QUO",
                "C_WC_MCTIMS_CXL",
                "C_WC_ATRRS_RESV",
                "C_WC_ATRRS_STU_UPDT",
                "C_WC_ATRRS_HOLDS",
                "C_WC_ATRRS_SEND_REPLIES",
                "C_WC_ATRRS_GET_REPLIES",
                "C_WC_FLTRD_PE",
                "C_WC_FLTRD_CDP"
        );

        // Pattern to extract the third field
        Pattern pattern = Pattern.compile("^[0-9]+,[0-9]+,([^,]+),[0-9]+,[0-9]+$");

        // Extract parent directory and file name
        String parentPath = new File(testFilePath).getParent();
        String fileName = new File(testFilePath).getName();

        from("oracleaq:queue:TESSERACT_QUEUE")
        //from("file:" + parentPath + "?fileName=" + fileName + "&noop=true")
                .routeId(routeId)  // <-- Setting the route ID here
                .split(body().tokenize("\n")).streaming()
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    Matcher matcher = pattern.matcher(body);
                    if (matcher.matches()) {
                        String thirdField = matcher.group(1);
                        if (allowedQueues.contains(thirdField)) {
                            exchange.setProperty("destinationQueue", thirdField);
                        } else {
                            exchange.setProperty("destinationQueue", "DEAD.LETTER");
                        }
                    } else {
                        exchange.setProperty("destinationQueue", "DEAD.LETTER");
                    }
                })
                .toD("activemq:queue:${exchangeProperty.destinationQueue}");
    }
}
