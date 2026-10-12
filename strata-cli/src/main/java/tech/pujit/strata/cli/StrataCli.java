package tech.pujit.strata.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * STRATA Diagnostic and Operations Command Line Interface.
 */
@Command(
    name = "strata-cli",
    mixinStandardHelpOptions = true,
    version = "STRATA 3.0.0",
    description = "STRATA Vector Database Diagnostic & Operations Tool"
)
public class StrataCli implements Callable<Integer> {

    @Option(names = {"--host"}, description = "Engine gRPC host", defaultValue = "127.0.0.1")
    private String host;

    @Option(names = {"--port"}, description = "Engine gRPC port", defaultValue = "50051")
    private int port;

    @Override
    public Integer call() {
        System.out.println("STRATA CLI v3.0.0 — target engine: " + host + ":" + port);
        System.out.println("Use --help for available diagnostic subcommands.");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new StrataCli()).execute(args);
        System.exit(exitCode);
    }
}
