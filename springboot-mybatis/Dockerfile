FROM java:8
MAINTAINER patrick
EXPOSE 8080
ADD target/springboot-mybatis-0.0.1-SNAPSHOT.jar boot.jar
ENTRYPOINT ["java","-jar","/boot.jar"]