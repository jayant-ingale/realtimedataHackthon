FROM vertx/vertx3                 
ENV VERTICLE_NAME com.syntel.vertx.StarterController
#ENV VERTICLE_NAME  com.syntel.vertx.VehicleVertx
ENV VERTICLE_FILE target/samplevertxrealtime-0.0.1-SNAPSHOT.jar

ENV VERTICLE_HOME /usr/verticles

EXPOSE 8087

COPY $VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME

ENTRYPOINT ["sh", "-c"]

#CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]
CMD ["exec java -cp samplevertxrealtime-0.0.1-SNAPSHOT.jar com.syntel.vertx.StarterController"]
