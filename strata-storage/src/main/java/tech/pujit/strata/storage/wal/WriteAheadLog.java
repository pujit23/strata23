package tech.pujit.strata.storage.wal;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Write-Ahead Log interface for durable mutation persistence.
 * Contract:
 * 1. A mutation is acknowledged only after the corresponding WAL record has been durably flushed.
 * 2. State containing a mutation is published only after the WAL durability point.
 * 3. Single-writer guarantees strictly increasing LSN order.
 */
public interface WriteAheadLog extends AutoCloseable {

    /**
     * Append a record to the log buffer.
     *
     * @param record the mutation record
     * @return the assigned LSN
     */
    long append(WalRecord record) throws IOException;

    /**
     * Synchronously flush buffered records to durable disk storage.
     */
    void flush() throws IOException;

    /**
     * Last durably flushed LSN.
     */
    long flushedLsn();

    /**
     * Replay WAL records from the given start LSN for crash recovery.
     */
    void replay(long fromLsn, Consumer<WalRecord> recordConsumer) throws IOException;

    @Override
    void close() throws IOException;
}
