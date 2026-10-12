package tech.pujit.strata.engine;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.pujit.strata.engine.grpc.TenantContextInterceptor;

import java.io.IOException;

/**
 * STRATA Vector Engine entry point.
 * Bootstraps the off-heap storage engine and gRPC transport service.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final int DEFAULT_PORT = 50051;

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = parsePort();
        log.info("Starting STRATA Vector Engine v3.0 on port {}", port);
        log.info("Java Runtime: {} ({})", System.getProperty("java.version"), System.getProperty("java.vendor"));
        log.info("Vector API status: jdk.incubator.vector module enabled");

        Server server = ServerBuilder.forPort(port)
            .intercept(new TenantContextInterceptor())
            .build()
            .start();

        log.info("STRATA Engine successfully listening on port {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down STRATA Engine gRPC server...");
            server.shutdown();
            log.info("STRATA Engine stopped gracefully.");
        }));

        server.awaitTermination();
    }

    private static int parsePort() {
        String envPort = System.getenv("STRATA_ENGINE_PORT");
        if (envPort != null && !envPort.isBlank()) {
            try {
                return Integer.parseInt(envPort);
            } catch (NumberFormatException e) {
                log.warn("Invalid STRATA_ENGINE_PORT '{}', using default {}", envPort, DEFAULT_PORT);
            }
        }
        return DEFAULT_PORT;
    }
}
