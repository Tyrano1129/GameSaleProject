FROM amazoncorretto:17

COPY sale-0.0.1-SNAPSHOT.jar /app/test.jar
COPY entrypoint.sh /app/entrypoint.sh

ENV APP_HOME /app
#ENV MYSQL_IP=my_mysql_ip
#ENV MYSQL_PORT=my_mysql_port
#ENV MYSQL_DATABASE=my_database
#ENV MYSQL_USER=my_user
#ENV MYSQL_ROOT_PASSWORD=my_password
EXPOSE 8081
VOLUME /app/upload

WORKDIR $APP_HOME
# ENTRYPOINT ["java","-jar","/app/test.jar"]
RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]
