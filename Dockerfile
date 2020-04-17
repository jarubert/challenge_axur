FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY checkMysqlAndRabbitMQ.sh checkMysqlAndRabbitMQ.sh
RUN chmod +x /checkMysqlAndRabbitMQ.sh
ENTRYPOINT [ "/checkMysqlAndRabbitMQ.sh"]
CMD ["java","-jar","/app.jar"]
