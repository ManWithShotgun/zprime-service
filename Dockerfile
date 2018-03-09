FROM openjdk:9-jre
VOLUME /tmp
ENV PYTHIA_PATH /app/pythia8226_export/
ENV MODEL_DIR zprime/
ENV PYTHIA_RUNNER /app/pythia_runner.sh

# example call: RUN ./app/pythia8226_export/zprime/calc_zprime 0.02 3250
ADD ./pythia/pythia8226_export ${PYTHIA_PATH}
ADD ./pythia/pythia_runner.sh ${PYTHIA_RUNNER}
ADD target/gs-spring-boot-docker-0.1.0.jar app.jar
# ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]