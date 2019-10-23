call %MAVEN_HOME%/bin/mvn clean compile &
call %MAVEN_HOME%/bin/mvn package shade:shade
call java -cp target/samplevertxrealtime-0.0.1-SNAPSHOT.jar com.syntel.vertx.StarterController


