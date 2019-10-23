package com.syntel.vertx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syntel.service.ProductService;
import com.syntel.service.StaticHandlerService;
import com.syntel.service.VehicleService;
import com.syntel.service.WeatherService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.TimeoutStream;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class RealTimeCollectorVertx extends AbstractVerticle {
	
	 private static final Logger logger = LoggerFactory.getLogger(RealTimeCollectorVertx.class);
	 
	 private Map<String, JsonObject> products = new HashMap<>();
	 private static int counter = 0;
	 private static final OperatingSystemMXBean osMBean;
	 
	 
	 static {
		    try {
		      osMBean = ManagementFactory.newPlatformMXBeanProxy(ManagementFactory.getPlatformMBeanServer(),
		          ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		    } catch (IOException e) {
		      throw new RuntimeException(e);
		    }
		  }

	 
    @Override
    public void start() {
    	
    	Router router = Router.router(vertx);
    	StaticHandlerService staticHandler = new StaticHandlerService();
    	VehicleService vehicleService = new VehicleService();
    	WeatherService tempService = new WeatherService();
    	ProductService prodService = new ProductService();
    	
    	//EventBus
    	EventBus evBus = vertx.eventBus();
    	
    	router.route("/eventbus/*").handler(eventBusHandler());
    	//router.route("/eventbusreal/*").handler(eventBusHandlerReal());
    	
		 router.route().failureHandler(errorHandler());
		 router.get("/products/:productID").handler(prodService::handleGetProduct);
		 router.put("/products/:productID").handler(prodService::handleAddProduct);
		 //http://localhost:9090/products
		 router.get("/products").handler(prodService::handleListProducts);
		 //http://localhost:9090//api/v1/sensor/temperature/12?temp=45
		 router.route("/api/v1/sensor/temperature/:temp").handler(tempService::handleTemprature);
		//http://localhost:9090//api/v1/sensor/vehiclegps/10?vehicleSpeed=20
		 router.route("/api/v1/sensor/vehiclegps/:vehicleId").handler(vehicleService::handleVehicleGPS);
		//http://localhost:9090//api/1 
    	 router.route("/api/:id").handler(staticHandler::handleStatic);
    	 router.route("/sse").handler(this::sse);
    	 router.route("/heatsensor").handler(this::handler);
    	 router.route("/realtime").handler(this::handlerRealTime);
    	 router.route().handler(StaticHandler.create());
    	/**
    	 * 	http://localhost:8090//api
    	 **/
    	router.route("/api/*").handler(staticHandler::handleDefaultStatic);
    	  
    	  

    	vertx.createHttpServer().requestHandler(router::accept).listen(8092);
    	 //vertx.createHttpServer().requestHandler(this::handler).listen(config().getInteger("port", 8092));

    		 
	    vertx.setPeriodic(1000, t -> {
	    	 final String url = "http://localhost:8092/api/v1/sensor/temperature/"+(LocalDateTime.now( ).getSecond()/2);
               vertx.createHttpClient().getAbs(url, response -> {
       	        if (response.statusCode() != 200) {
       	            System.err.println("fail");
       	        } else {
       	          //  response.bodyHandler(b -> System.out.println(b.toString()));
       	         response.bodyHandler(buffer -> {
                        	vertx.eventBus().publish("load",
            		        new JsonObject()
            		            .put("creatTime", System.currentTimeMillis())
            		           // .put("cpuTime", Integer.parseInt(buffer.toString().substring(1, buffer.toString().length()-1)))) ;
            		            .put("cpuTime", Double.parseDouble(buffer.toString().substring(1, buffer.toString().length()-1)))) ;
            		            		
                  });
       	        }
       	    }).end();
	    	});
	    Random r = new Random();
	    vertx.setPeriodic(1000, t -> {
	    	int randNum =r.ints(40, (140 + 1)).findFirst().getAsInt();
	    	long time =System.currentTimeMillis();
	    	  DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
	    	  System.out.println("randNum  ["+randNum + "]time ["+new Date(time)+"]" +  df.format(new Date(time)));
                       	vertx.eventBus().publish("loadtime",
           		        new JsonObject()
           		            .put("creatTime", time)
           		            .put("cpuTime",randNum  )) ;
                       	
           		            		
                 });
	    
	    vertx.setPeriodic(1000, t -> {
	    	int randNum =r.ints(40, (140 + 1)).findFirst().getAsInt();
	    	long time =System.currentTimeMillis();
	    	  DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
	    	  System.out.println("randNum  ["+randNum + "]time ["+new Date(time)+"]" +  df.format(new Date(time)));
                       	vertx.eventBus().send("loadtimeTest",
           		        new JsonObject()
           		            .put("creatTime", time)
           		            .put("cpuTime",randNum  )) ;
                       	
           		            		
                 });
    }

    private SockJSHandler eventBusHandler() {
        BridgeOptions options = new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddress("load"));
        options.addOutboundPermitted(new PermittedOptions().setAddress("loadtime"));
        return SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == io.vertx.ext.bridge.BridgeEventType.SOCKET_CREATED) {
                //logger.info("A socket was created");
                System.out.println("A socket was created");
            }
            event.complete(true);
        });
    }
  /*  private SockJSHandler eventBusHandlerReal() {
        BridgeOptions options = new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddress("loadtime"));
        return SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == io.vertx.ext.bridge.BridgeEventType.SOCKET_CREATED) {
                //logger.info("A socket was created");
                System.out.println("B socket was created");
            }
            event.complete(true);
        });
    }*/
    
    private void handler(RoutingContext context) {
    	
       // if ("/heat".equals(context.request().path())) {
        	context.response().sendFile("webroot/heatsensor.html");
     //   }
      }
    private void handlerRealTime(RoutingContext context) {
    	
        // if ("/heat".equals(context.request().path())) {
         	context.response().sendFile("webroot/realtimechart.html");
      //   }
       }
     
    
    private void sse(RoutingContext context) {
        HttpServerResponse response = context.response();
        response
          .putHeader("Content-Type", "text/event-stream")//"text/event-stream"
          .putHeader("Cache-Control", "no-cache")
          .setChunked(true);

        MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("sensor.updates");
        consumer.handler(msg -> {
          response.write("event: update\n");
          response.write("data: " + msg.body().encode() + "\n\n");
        });


        TimeoutStream ticks = vertx.periodicStream(1000);
     // send "sensor.average" 
        // send --The message will be delivered to at most one of the handlers registered to the address.
        ticks.handler(id -> {
          vertx.eventBus().<JsonObject>send("sensor.average", "", reply -> {
            if (reply.succeeded()) {
              response.write("event: average\n");
              response.write("data: " + reply.result().body().encode() + "\n\n");
            }
          });
        });

        response.endHandler(v -> {
          consumer.unregister();
          ticks.cancel();
        });
      }
    
    private ErrorHandler errorHandler() {
        return ErrorHandler.create(true);
    }



}
