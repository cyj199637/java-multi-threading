package performanceOptimization;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {

    private static final String SOURCE_FILE = "./resources/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(SOURCE_FILE)));
        startServer(text);
    }

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);
        server.start();
    }
}
