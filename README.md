# Integration Project using Red Hat Camel for Spring Boot

## Goal of this Lab

The goal of this lab is to demonstrate an integration project using **Red Hat Build of Camel for Spring Boot (3.20.1.redhat-00104)**. The Camel start route reads from an Oracle database via **Oracle Advanced Queue (OracleAQ) JMS JDBC Bridge**. The route then processes some custom business logic and sends the output message to a **Red Hat AMQ Broker Acceptor** using the TCP transport protocol.

## Lab Instructions

### Prerequisites

- **Java Development Kit (JDK):**

  This lab was tested and verified using:

  ```plaintext
  openjdk version "11.0.25" 2024-10-15
  OpenJDK Runtime Environment Homebrew (build 11.0.25+0)
  OpenJDK 64-Bit Server VM Homebrew (build 11.0.25+0, mixed mode)

- **Oracle Database Instance:**
  
  ```plaintext
  Running locally or remote.
  You have the database login credentials.
  You have configured Queue Table with Queue Name: TESSERACT_QUEUE

- **AMQ 7 Broker Instance:**
  
  ```plaintext
  Running locally or remote.
  You have the broker admin login credentials
  At least one Acceptor must be configured with TCP protocol enabled

- **Maven Respository:**

  NOTE: Since Oracle "aqapi.jar" is commercially license, this lab is for
  Educational purposes only.  We're getting the jar from a Maven repo hosted
  by Virginia Tech.  Add the following to your local Mavens "settings.xml"
  
  ```plaintext
  <repository>
    <id>middleware</id>
    <name>Middleware Repository</name>
    <url>https://git.it.vt.edu/middleware/maven-repo/raw/master</url>
  </repository>

  Adding the above Maven repo enables this dependency in your POM

  <dependency>
    <groupId>oracle</groupId>
    <artifactId>aqapi</artifactId>
    <version>11.1.1.2.0</version>
  </dependency>

### Lab Steps

- **Clone, Build, Run**

  ```plaintext
  git clone [repository_url]
  Using your favorite IDE (Eclipse, IntelliJ, VSCode, etc.), open the project folder containing the pom.xml file
  mvn -X clean install
  mvn -X -DskipTests spring-boot:run

