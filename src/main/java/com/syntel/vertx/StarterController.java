package com.syntel.vertx;
import com.syntel.vertx.MyFirstVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class StarterController extends AbstractVerticle {

	
	  /**
    *
    * @param args
    */
   public static void main(String[] args){
	   Logger logger = LoggerFactory.getLogger(StarterController.class);
	   
       
       
      // VertxOptions vxOptions = new VertxOptions().setBlockedThreadCheckInterval(200000000);
    			//.setblockedThreadCheckInterval(200000000);
    		//Vertx myVertx = Vertx.vertx(vxOptions);
    		final Vertx vertx = Vertx.vertx();
       vertx.deployVerticle("com.syntel.vertx.VehicleVertx", new DeploymentOptions().setInstances(1));
     //  vertx.deployVerticle("com.syntel.vertx.VehicleVertx", new DeploymentOptions().setWorker(true));
       
       //setInstances--Set the number of instances that should be deployed.
     //  vertx.deployVerticle("com.syntel.vertx.heatsensor.HeatSensor", new DeploymentOptions().setInstances(4));
     /*  vertx.deployVerticle("com.syntel.vertx.heatsensor.HeatSensor",
    		      new DeploymentOptions().setConfig(new JsonObject()
    		        .put("http.port", 3000)));
       */
      // vertx.deployVerticle("com.syntel.vertx.SensorData");
       
       /*vertx.deployVerticle(new RealTimeCollectorVertx(), res -> {
           if (res.succeeded()) {
        	   logger.info("Deployment id is: " + res.result());
           } else {
               logger.info("Deployment failed!");
           }
       });*/
       
       /*vertx.deployVerticle(new VehicleVertx(), res -> {
           if (res.succeeded()) {
        	   logger.info("Deployment[VehicleVertx] id is: " + res.result());
           } else {
               logger.info("Deployment VehicleVertx failed!");
           }
       });*/
      // vertx.deployVerticle(new BlockEventLoop());
      
   }
   
}
