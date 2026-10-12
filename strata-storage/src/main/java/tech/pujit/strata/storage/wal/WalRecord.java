package tech.pujit.strata.storage.wal;

/**
 * Immutable log record in the Write-Ahead Log.
 * Enforces strictly increasing LSN order and checksum integrity.
 */
public record WalRecord(
    long lsn,
    long timestampNanos,
    WalRecordType type,
    String pointId,
    float[] vector,
    String payloadJson,
    long crc32Checksum
) {

    public WalRecord {
        if (lsn < 0) {
            throw new IllegalArgumentException("LSN must be non-negative: " + lsn);
        }
        if (type == null) {
            throw new IllegalArgumentException("Record type must not be null");
        }
    }
}
