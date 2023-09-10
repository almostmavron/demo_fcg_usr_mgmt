FROM openjdk:17-jdk-alpine
RUN addgroup fcgdemo; adduser  --ingroup fcgdemo --disabled-password appuser
USER appuser

# TODO switch from a fat jar approach to a slim jar + dependencies dir in classpath
# Change will affect pom, dockerfile

# FIXME factor app version in a single file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar


EXPOSE 8881
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]