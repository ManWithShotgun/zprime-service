FROM openjdk:11.0.2-jre-stretch
VOLUME /tmp
ENV PYTHIA_PATH /app/pythia8226_export/
ENV MODEL_DIR zprime/
ENV PYTHIA_RUNNER /app/pythia_runner.sh

# example call: RUN ./app/pythia8226_export/zprime/calc_zprime 0.02 3250
ADD ./pythia/pythia8226_export ${PYTHIA_PATH}
ADD ./pythia/pythia_runner.sh ${PYTHIA_RUNNER}
# ADD target/zprime-service-0.1.0.jar app.jar

# ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
# CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-Xdebug", "-Xrunjdwp:server=y,transport=dt_socket,suspend=n", "-jar","/app.jar"]
# CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-Xdebug", "-Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n", "-jar","/app.jar"]
# CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-Xdebug", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000", "-jar","/app.jar"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Xdebug", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "-jar","/data/zprime-service-0.1.0.jar"]
# ENTRYPOINT ["java","-jar","/app.jar"]