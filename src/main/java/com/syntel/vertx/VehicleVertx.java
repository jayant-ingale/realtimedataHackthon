package com.syntel.vertx;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syntel.service.StaticHandlerService;
import com.syntel.service.VehicleService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class VehicleVertx extends AbstractVerticle {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleVertx.class);
	//StaticHandlerService staticHandler = new StaticHandlerService();
	VehicleService vehicleService = new VehicleService();
	
    @Override
    public void start() {
    	
    	Router router = Router.router(vertx);
    	VehicleService vehicleService = new VehicleService();
    	//EventBus
    	vertx.createHttpServer().requestHandler(router::accept).listen(8087);
    	EventBus evBus = vertx.eventBus();
    	router.route("/eventbus/*").handler(eventBusHandler());
    	router.route().failureHandler(errorHandler());
    	
    	//Route *********************
		/*
		 * router.route("/vehicleHeat").handler(this::handlerVehicleHeat);
		 * router.route("/vehicleHumid").handler(this::handlerVehicleHumid);
		 * router.route("/vehicleSpeed").handler(this::handlerVehicleSpeed);
		 * router.route("/vehicleAlert").handler(this::handlerVehicleAlert);
		 */
    	//router.route("/api/*").handler(staticHandler::handleDefaultStatic);
    	//router.route().handler(StaticHandler.create());
    	router.route("/static/*").handler(StaticHandler.create("webroot").setCachingEnabled(false));
    	 Random random = new Random();
    	 
    	 
    	 vertx.setPeriodic(500, t -> {
  	    	int randVehicleSpeed =random.ints(50, (60 + 1)).findFirst().getAsInt();
  	    	int randVehicleHumidity =random.ints(10, 20).findFirst().getAsInt();
  	    	int randVehicleHeat =random.ints(30, 40).findFirst().getAsInt();
  	    	int randVehileId =random.ints(1, 5).findFirst().getAsInt();
			int randCO =random.ints(13, 20).findFirst().getAsInt();
  	    	int randCO2 =random.ints(300, 340).findFirst().getAsInt();
  	    	int randNO2 =random.ints(60, 70).findFirst().getAsInt();
  	    	
  	    	long time =System.currentTimeMillis();
  	    	DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
  	    	//System.out.println("randNum  ["+randNum + "]time ["+new Date(time)+"]" +  df.format(new Date(time)));
            // vertx.eventBus().publish("loadtime",
  	    	JsonObject jsonObj = 	new JsonObject()
     		            .put("eventTime", time)
     		            .put("vehicleSpeed",randVehicleSpeed  )
     		            .put("vehicleHumidity",randVehicleHumidity  )
     		            .put("vehicleHeat",randVehicleHeat  )
             			.put("vehicleId",randVehileId  ) 
						.put("vehicleCO",randCO) 
						.put("vehicleCO2",randCO2) 
						.put("vehicleNO2",randNO2);
             publishEvent("loadVehicle", jsonObj);
  	    	});
    	 
    	 
    	 /* vertx.setPeriodic(1000, t -> {
 	    	int randNum =random.ints(40, (140 + 1)).findFirst().getAsInt();
 	    	int randVehileId =random.nextInt(4);
 	    	long time =System.currentTimeMillis();
 	    	DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
 	    	//System.out.println("randNum  ["+randNum + "]time ["+new Date(time)+"]" +  df.format(new Date(time)));
           // vertx.eventBus().publish("loadtime",
 	    	JsonObject jsonObj= 	new JsonObject()
    		            .put("eventTime", time)
    		            .put("vehicleSpeed",randNum  )
            			.put("vehicleId",randVehileId  ) ;
            publishEvent("loadSpeed",   jsonObj);
 	    	});
 	    
 	    
 	   /*vertx.setPeriodic(1000, t -> {
	    	int randNum =random.ints(50, 100).findFirst().getAsInt();
	    	int randVehileId =random.nextInt(4);
	    	long time =System.currentTimeMillis();
	    	DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
	    	System.out.println("randNum  ["+randNum + "]time ["+new Date(time)+"]" +  df.format(new Date(time)));
           //vertx.eventBus().publish("loadHumidity",
		   JsonObject jsonObj= 	new JsonObject()
            .put("eventTime", time)
            .put("vehicleHumidity",randNum  )
   			.put("vehicleId",randVehileId  ) ;
		   	publishEvent("loadHumidity",   jsonObj);
	    	});
    	
 	  vertx.setPeriodic(1000, t -> {
	    	int randNum =random.ints(50, 122).findFirst().getAsInt();
	    	int randVehileId =random.nextInt(4);
	    	long time =System.currentTimeMillis();
	    	//JsonObject vehiclePayload  = vehicleService.handleVehicleHeat();
	    	DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
	    	System.out.println("randNum  ["+randNum + "]time ["+new Date(time)+"]" +  df.format(new Date(time)));
	    	 vertx.eventBus().publish("loadHeat",
        		 vehiclePayload.put("vehicleId",randVehileId  ));
	    	JsonObject vehiclePayload  = new JsonObject()
	            .put("eventTime", time)
	            .put("vehicleHeat",randNum  )
	            .put("vehicleId",randVehileId  ) ;
	    	
	    	publishEvent("loadHeat", vehiclePayload);
         	
	    	});*/
 	  
    }
    
    void publishEvent(String publishName, JsonObject payload) {
    	vertx.eventBus().publish(publishName,payload );
    	
    }
   
    private SockJSHandler eventBusHandler() {
        BridgeOptions options = new BridgeOptions();
        options.addOutboundPermitted(new PermittedOptions().setAddress("loadVehicle"));
        options.addInboundPermitted(new PermittedOptions().setAddress("loadVehicle"));
        /*options.addOutboundPermitted(new PermittedOptions().setAddress("loadSpeed"));
        options.addOutboundPermitted(new PermittedOptions().setAddress("loadHumidity"));
        options.addOutboundPermitted(new PermittedOptions().setAddress("loadHeat"));*/
        try {
        return SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == io.vertx.ext.bridge.BridgeEventType.SOCKET_CREATED) {
                //logger.info("A socket was created");
                System.out.println("A socket was created");
            }
            event.complete(true);
        });
        }catch(Throwable e) {
        	e.printStackTrace();
        }
		return null;
    }
    
    
    private ErrorHandler errorHandler() {
        return ErrorHandler.create(true);
    }
	

}
