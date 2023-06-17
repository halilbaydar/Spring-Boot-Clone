package my.spring.boot;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import my.spring.boot.server.MyServer;
import my.spring.boot.annotation.MySpringBootApplication;
import my.spring.boot.context.SpringApplicationContext;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.java.myspring.stream.StreamFindSupportKt.findController;
import static com.java.myspring.stream.StreamFindSupportKt.findEndpointHandler;

public class MySpringApplication {
    private volatile static SpringApplicationContext context;

    public synchronized static void run(Class<?> bootClass) {
        if (bootClass.isAnnotationPresent(MySpringBootApplication.class)) {
            MySpringApplication.context = new SpringApplicationContext(bootClass);
            MyServer.initMyServer((exchange) -> {
                try {
                    handleRequest(exchange);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        throw new RuntimeException("main class has to be annotated with boot application interface");
    }

    private static void handleRequest(HttpExchange httpExchange) throws Exception {
        try {
            URI uri = httpExchange.getRequestURI();
            Map.Entry<Class<?>, Object> controllerCls = findController(uri, httpExchange.getRequestMethod());
            if (controllerCls == null) {
                throw new RuntimeException("not found");
            }
            Method endpointHandler = findEndpointHandler(controllerCls.getKey(), uri, httpExchange.getRequestMethod());
            if (endpointHandler == null) {
                throw new RuntimeException("not found");
            }
            Object controllerComponent = context.getComponent(controllerCls.getKey());
            Object response = endpointHandler.invoke(controllerComponent);
            handleResponse(httpExchange, 200, response);
        } catch (Exception e) {
            if (e.getMessage().equals("not found")) {
                handleResponse(httpExchange, 404, e.getMessage());
            } else {
                handleResponse(httpExchange, 500, e.getMessage());
            }
        }
    }

    private static void handleResponse(HttpExchange httpExchange, int status, Object response) throws Exception {
        OutputStream outputStream = httpExchange.getResponseBody();
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("content-type", "application/json");
        httpExchange.sendResponseHeaders(status, response.toString().length());
        outputStream.write(response.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
