# command to build image
# docker build -t atm .

FROM ubuntu

# Install java jdk 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean;

COPY target/atm-0.0.1-SNAPSHOT.jar /apps/atm-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD java -jar /apps/atm-0.0.1-SNAPSHOT.jar

# command to launch container
# docker run -it --name atm -p 8080:8080 -d atm:latest