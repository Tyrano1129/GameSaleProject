FROM amazoncorretto:17

COPY sale-0.0.1-SNAPSHOT.jar /app/test.jar
COPY entrypoint.sh /app/entrypoint.sh

#ENV APP_HOME /app
#ENV MYSQL_IP=54.180.99.193
#ENV MYSQL_PORT=8081
#ENV MYSQL_DATABASE=gamesale
#ENV MYSQL_USER=root
#ENV MYSQL_ROOT_PASSWORD=1234
EXPOSE 8081
VOLUME /app/upload

WORKDIR $APP_HOME
# ENTRYPOINT ["java","-jar","/app/test.jar"]
RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]
