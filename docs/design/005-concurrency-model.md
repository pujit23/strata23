# Design 005: Concurrency & Isolation Model

## Concurrency Architecture

- **Single Writer**: All collection mutations (upsert/delete) are serialized by a single writer lock or event loop thread per collection, guaranteeing monotonically increasing LSN ordering.
- **Lock-Free Concurrent Readers**: Multiple reader queries execute concurrently without blocking the writer, pinning an immutable manifest snapshot epoch.
- **Epoch Garbage Collection**: Segments and WAL files marked for deletion or compaction are only unlinked once all reader epochs referencing them have completed.
