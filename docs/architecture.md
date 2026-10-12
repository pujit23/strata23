# STRATA Architecture Overview

STRATA is a from-scratch, single-node, filter-aware vector database engineered for extreme similarity query performance with high-selectivity metadata filtering.

## 1. Architectural Layers

1. **Vector Storage & Execution Engine (Java 25)**:
   - Owns raw vector representations, indexes, segment storage, WAL, tombstones, and point data.
   - Leverages Java Vector API (`jdk.incubator.vector`) for SIMD data parallelism with scalar fallback.
   - Utilizes Foreign Function & Memory (FFM) API for off-heap segment buffers without garbage collection pauses.
   - Exposes a private, high-throughput gRPC boundary.

2. **API Gateway & Metadata Service (Node.js LTS / TypeScript / Fastify)**:
   - Owns tenant authentication, API key validation, user identities, and rate limiting.
   - Connects to MongoDB for user credentials and API key metadata.
   - Proxies authenticated requests to the engine over gRPC, injecting tenant identity via trusted metadata (`x-strata-tenant-id`).

3. **Web Console (React 19 / TypeScript / Vite / Tailwind CSS 4)**:
   - Operator and developer interface for vector collections, query experimentation, index exploration, and benchmark monitoring.
   - Connects exclusively to the Service REST API.

## 2. Core Correctness Contracts

1. **WAL Durability Before ACK**: A mutation is acknowledged only after the corresponding WAL record has been durably flushed to disk.
2. **Publish After Durability**: Internal state containing a mutation is published to readers only after passing the WAL durability point.
3. **Immutable Epoch Visibility**: Readers pin a consistent immutable epoch to guarantee repeatable read isolation.
4. **Single-Writer Strict LSN Order**: Mutations are serialized through a single writer in strictly increasing Log Sequence Numbers.
5. **Immutable Sealed Segments & Versioned Tombstones**: Segments become completely immutable upon sealing; deletes append versioned tombstones.
6. **WAL Reclamation Discipline**: WAL segments are truncated only after being superseded by durable checkpoints.
7. **Oracle Testability**: Filtered-search strategies are tested against an exact-search brute-force oracle.
8. **Clear Responsibility Boundaries**: Collection schemas and vectors belong to the Java engine; user accounts and API keys belong to the Node.js service.
9. **Metadata-Based Tenant Isolation**: Tenant identity is verified by the service layer and conveyed over gRPC metadata.
10. **Verified Correctness**: All claims must be backed by automated test suites in CI.
