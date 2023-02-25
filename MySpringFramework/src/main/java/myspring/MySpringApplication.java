package myspring;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import myspring.annotation.MySpringBootApplication;
import myspring.context.SpringApplicationContext;
import server.MyServer;

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
        MySpringApplication.context = new SpringApplicationContext(bootClass);
        if (bootClass.isAnnotationPresent(MySpringBootApplication.class)) {
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
            switch (e.getMessage()) {
                case "not found":
                    handleResponse(httpExchange, 404, e.getMessage());
                    break;
                default:
                    handleResponse(httpExchange, 500, e.getMessage());
                    break;
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
