package com.amazonaws.handlers;

import com.amazonaws.AmazonClientException;
import com.amazonaws.util.ClassLoaderHelper;
import com.amazonaws.util.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class HandlerChainFactory {
   private static final String GLOBAL_HANDLER_PATH = "com/amazonaws/global/handlers/request.handler2s";

   public List<RequestHandler2> newRequestHandlerChain(String resource) {
      return this.createRequestHandlerChain(resource, RequestHandler.class);
   }

   public List<RequestHandler2> newRequestHandler2Chain(String resource) {
      return this.createRequestHandlerChain(resource, RequestHandler2.class);
   }

   public List<RequestHandler2> getGlobalHandlers() {
      List<RequestHandler2> handlers = new ArrayList<>();
      BufferedReader fileReader = null;

      try {
         for(URL url : Collections.list(this.getGlobalHandlerResources())) {
            fileReader = new BufferedReader(new InputStreamReader(url.openStream(), StringUtils.UTF8));

            while(true) {
               String requestHandlerClassName = fileReader.readLine();
               if (requestHandlerClassName == null) {
                  break;
               }

               RequestHandler2 requestHandler = this.createRequestHandler(requestHandlerClassName, RequestHandler2.class);
               if (requestHandler != null) {
                  handlers.add(requestHandler);
               }
            }
         }
      } catch (Exception var15) {
         throw new AmazonClientException("Unable to instantiate request handler chain for client: " + var15.getMessage(), var15);
      } finally {
         try {
            if (fileReader != null) {
               fileReader.close();
            }
         } catch (IOException var14) {
         }
      }

      return handlers;
   }

   private Enumeration<URL> getGlobalHandlerResources() throws IOException {
      return HandlerChainFactory.class.getClassLoader() == null
         ? ClassLoader.getSystemResources("com/amazonaws/global/handlers/request.handler2s")
         : HandlerChainFactory.class.getClassLoader().getResources("com/amazonaws/global/handlers/request.handler2s");
   }

   private RequestHandler2 createRequestHandler(String handlerClassName, Class<?> handlerApiClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
      handlerClassName = handlerClassName.trim();
      if (handlerClassName.equals("")) {
         return null;
      } else {
         Class<?> requestHandlerClass = ClassLoaderHelper.loadClass(handlerClassName, handlerApiClass, this.getClass());
         Object requestHandlerObject = requestHandlerClass.newInstance();
         if (handlerApiClass.isInstance(requestHandlerObject)) {
            if (handlerApiClass == RequestHandler2.class) {
               return (RequestHandler2)requestHandlerObject;
            } else if (handlerApiClass == RequestHandler.class) {
               return RequestHandler2.adapt((RequestHandler)requestHandlerObject);
            } else {
               throw new IllegalStateException();
            }
         } else {
            throw new AmazonClientException(
               "Unable to instantiate request handler chain for client.  Listed request handler ('"
                  + handlerClassName
                  + "') does not implement the "
                  + handlerApiClass
                  + " API."
            );
         }
      }
   }

   private List<RequestHandler2> createRequestHandlerChain(String resource, Class<?> handlerApiClass) {
      List<RequestHandler2> handlers = new ArrayList<>();
      BufferedReader reader = null;

      try {
         InputStream input = this.getClass().getResourceAsStream(resource);
         if (input == null) {
            return handlers;
         } else {
            reader = new BufferedReader(new InputStreamReader(input, StringUtils.UTF8));

            while(true) {
               String requestHandlerClassName = reader.readLine();
               if (requestHandlerClassName == null) {
                  return handlers;
               }

               RequestHandler2 requestHandler = this.createRequestHandler(requestHandlerClassName, handlerApiClass);
               if (requestHandler != null) {
                  handlers.add(requestHandler);
               }
            }
         }
      } catch (Exception var16) {
         throw new AmazonClientException("Unable to instantiate request handler chain for client: " + var16.getMessage(), var16);
      } finally {
         try {
            if (reader != null) {
               reader.close();
            }
         } catch (IOException var15) {
         }
      }
   }
}
