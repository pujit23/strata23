package tech.pujit.strata.storage.wal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WalRecordTest {

    @Test
    @DisplayName("WAL record creates correctly with valid LSN and payload")
    void testValidWalRecord() {
        WalRecord record = new WalRecord(
            100L,
            System.nanoTime(),
            WalRecordType.UPSERT,
            "pt-001",
            new float[]{0.1f, 0.2f, 0.3f},
            "{\"category\":\"electronics\"}",
            12345678L
        );

        assertThat(record.lsn()).isEqualTo(100L);
        assertThat(record.type()).isEqualTo(WalRecordType.UPSERT);
        assertThat(record.pointId()).isEqualTo("pt-001");
        assertThat(record.vector()).hasSize(3);
    }

    @Test
    @DisplayName("Negative LSN throws IllegalArgumentException")
    void testNegativeLsn() {
        assertThatThrownBy(() -> new WalRecord(
            -1L,
            System.nanoTime(),
            WalRecordType.UPSERT,
            "pt-001",
            null,
            null,
            0L
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("LSN must be non-negative");
    }
}
