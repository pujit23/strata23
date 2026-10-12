# Design 001: Storage Engine & Durability

## Log-Structured Segments & Write-Ahead Logging

- **Append-Only WAL**: All mutations (upserts and deletes) are appended to an active WAL file and fsync'd before being acknowledged.
- **Sealed Segments**: When an in-memory mutable buffer reaches target size (e.g. 100k vectors or 256MB), it is sealed into an immutable on-disk segment.
- **Versioned Tombstones**: Deletes never mutate sealed segments in place; they record a tombstone containing `(point_id, deleted_at_lsn)`.
- **Epoch Visibility**: Readers pin an LSN snapshot epoch, seeing all mutations <= epoch and ignoring tombstones > epoch.
