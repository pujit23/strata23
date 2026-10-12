package tech.pujit.strata.storage.wal;

/**
 * Type of mutation record written to the Write-Ahead Log.
 */
public enum WalRecordType {
    UPSERT,
    DELETE,
    CHECKPOINT
}
