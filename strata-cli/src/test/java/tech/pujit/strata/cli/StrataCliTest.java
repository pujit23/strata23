package tech.pujit.strata.cli;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;

class StrataCliTest {

    @Test
    @DisplayName("CLI help command returns success exit code")
    void testCliHelp() {
        int exitCode = new CommandLine(new StrataCli()).execute("--help");
        assertThat(exitCode).isZero();
    }

    @Test
    @DisplayName("CLI version command returns success exit code")
    void testCliVersion() {
        int exitCode = new CommandLine(new StrataCli()).execute("--version");
        assertThat(exitCode).isZero();
    }
}
