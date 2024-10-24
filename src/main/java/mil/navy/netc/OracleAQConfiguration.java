package mil.navy.netc;

import oracle.jms.AQjmsFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;

import javax.jms.ConnectionFactory;
import java.util.Properties;

@Configuration
public class OracleAQConfiguration {

    @Bean
    public ConnectionFactory connectionFactoryOracleAQ() throws Exception {
        String url = "jdbc:oracle:thin:@XXXXXXXXX";
        Properties properties = new Properties(); // Empty properties

        // Create the Oracle AQ Connection Factory
        return AQjmsFactory.getQueueConnectionFactory(url, properties);
    }

    @Bean
    public UserCredentialsConnectionFactoryAdapter oracleAQCredentials() throws Exception {
        UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
        adapter.setTargetConnectionFactory(connectionFactoryOracleAQ());
        adapter.setUsername("XXXXXX");
        adapter.setPassword("XXXXXXX");
        return adapter;
    }

    @Bean(name = "oracleaq")
    public JmsComponent oracleaq() throws Exception {
        JmsComponent component = new JmsComponent();
        component.setConnectionFactory(oracleAQCredentials());
        return component;
    }
}
