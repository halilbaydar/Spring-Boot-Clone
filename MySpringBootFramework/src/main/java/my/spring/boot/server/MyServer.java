package my.spring.boot.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MyServer {
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_CONTEXT = "/";
    private static final int DEFAULT_NUMBER_OF_SERVER_THREADS = 10;

    public static void initMyServer(HttpHandler httpHandler) {
        try {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_SERVER_THREADS);
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT), 0);
            httpServer.createContext(DEFAULT_CONTEXT, httpHandler);
            httpServer.setExecutor(threadPoolExecutor);
            httpServer.start();
            System.out.println("My server started on port(s): " + DEFAULT_PORT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
