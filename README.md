# STRATA v0.1.0 (Early stage at phase 1)
> **Status:** 🚧 Early development. See [implementation status](docs/implementation-status.md) for what works today.

> A from-scratch, single-node, filter-aware vector database engineered for extreme similarity query performance with high-selectivity metadata filtering.

[![CI](https://github.com/pujit23/strata23/actions/workflows/ci.yml/badge.svg)](https://github.com/pujit23/strata23/actions/workflows/ci.yml)
[![Version](https://img.shields.io/badge/version-0.1.0-blue.svg)](STRATA_VERSION)
[![Java](https://img.shields.io/badge/Java-25%20(Vector%20API)-orange.svg)](https://openjdk.org/)
[![Node](https://img.shields.io/badge/Node.js-22%20LTS-green.svg)](https://nodejs.org/)
[![React](https://img.shields.io/badge/React-19-cyan.svg)](https://react.dev/)

---

## 1. What STRATA Is and Is Not

### What STRATA Is
- **Filter-Aware by Design**: Unifies vector nearest-neighbor search with structured metadata filtering through cost-based execution planning (pre-filtering, post-filtering, and iterative index search).
- **Single-Node High Performance**: Optimized for single-node vertical scaling using modern hardware capabilities: Java 25 Vector API (`jdk.incubator.vector`) for SIMD acceleration and Foreign Function & Memory (FFM) API for off-heap segment memory.
- **Log-Structured & Durable**: ACID-compliant durability with append-only Write-Ahead Logging (WAL), sealed immutable segments, versioned tombstones, and crash recovery.
- **Multi-Tenant Secure**: Enforces tenant isolation where tenant identity is authenticated at the gateway layer and propagated to the engine exclusively via trusted gRPC metadata.

### What STRATA Is Not
- **Not a Distributed Database**: STRATA does not implement Raft, Paxos, distributed sharding, or multi-node consensus. It is intentionally designed as an ultra-fast, robust single-node engine.
- **Not a Relational Database**: STRATA does not support arbitrary SQL joins or multi-table ACID transactions outside collection point mutations.
- **Not a Black-Box Wrapper**: STRATA is built from scratch; it does not wrap Lucene, Faiss, Milvus, or Qdrant.

---

## 2. Architectural Layers & Repository Modules

```text
strata/
├── proto/strata/v1/          # Authoritative Protocol Buffers gRPC engine contract
├── strata-core/              # Vector distance kernels (Vector API), indexes, quantization, FFM memory
├── strata-storage/           # WAL, immutable segments, manifests, tombstones, compaction
├── strata-query/             # Filter evaluator, query planner, hybrid BM25, query traces
├── strata-engine/            # gRPC server, tenant interceptors, request validation, Main entrypoint
├── strata-cli/               # Diagnostic CLI tool (picocli)
├── service/                  # Node.js LTS Fastify REST API gateway, auth, MongoDB repositories
├── console/                  # React 19 / Vite / Tailwind CSS 4 web console
├── clients/                  # Official Python and TypeScript client SDKs
├── benchmarks/               # Python synthetic data generation and recall evaluation harness
├── docker/                   # Dockerfiles, MongoDB init, Prometheus, and Grafana provisioning
└── docs/                     # Architecture, design specifications, and operations guides
```

### Module Responsibilities

| Module | Language / Runtime | Core Responsibility |
| :--- | :--- | :--- |
| [`strata-core`](file:///c:/Users/user/OneDrive/Desktop/strata/strata-core) | Java 25 | SIMD distance kernels (`VectorDistance`), HNSW, IVF-PQ, Flat index, and FFM off-heap memory. |
| [`strata-storage`](file:///c:/Users/user/OneDrive/Desktop/strata/strata-storage) | Java 25 | Append-only WAL, segment sealing, epoch visibility, tombstones, and manifest checkpoints. |
| [`strata-query`](file:///c:/Users/user/OneDrive/Desktop/strata/strata-query) | Java 25 | Filter predicate evaluation, cost-based query planner, BM25 scoring, and execution traces. |
| [`strata-engine`](file:///c:/Users/user/OneDrive/Desktop/strata/strata-engine) | Java 25 | gRPC server implementation, metadata-based tenant isolation interceptors, and application runtime. |
| [`strata-cli`](file:///c:/Users/user/OneDrive/Desktop/strata/strata-cli) | Java 25 | Operational CLI tool for segment inspection, WAL dumps, and live engine status queries. |
| [`service`](file:///c:/Users/user/OneDrive/Desktop/strata/service) | Node.js 22 / TypeScript | Public REST gateway, tenant API keys, user management, and gRPC engine client. |
| [`console`](file:///c:/Users/user/OneDrive/Desktop/strata/console) | React 19 / Vite | Web console for collection management, query console, 3D index explorer, and benchmark lab. |
| [`benchmarks`](file:///c:/Users/user/OneDrive/Desktop/strata/benchmarks) | Python 3.12+ | Synthetic vector datasets, selectivity test matrices, and recall verification against exact oracle. |

---

## 3. Required Local Development Tools

- **Java Development Kit (JDK)**: JDK 24 or 25 (with `--enable-preview` and `jdk.incubator.vector`).
- **Node.js**: Node.js 22 LTS or newer.
- **npm**: npm 10.9 or newer.
- **Python**: Python 3.10+ (for benchmark scripts and dataset generation).
- **Docker & Docker Compose**: Optional for containerized local services (MongoDB, Prometheus, Grafana).
- **Buf CLI**: Optional for Protocol Buffers linting and code generation (`buf`).

---

## 4. How to Build, Test, and Run the Project

### A. Java Vector Engine (Multi-Module Gradle)

```bash
# Build all modules and run unit tests
./gradlew test

# Run a specific subproject test suite
./gradlew :strata-core:test
./gradlew :strata-storage:test
./gradlew :strata-query:test
./gradlew :strata-engine:test
./gradlew :strata-cli:test

# Run JMH vector distance microbenchmarks
./gradlew :strata-core:jmh

# Run the Vector Engine daemon locally
./gradlew :strata-engine:run
```

### B. Node.js Service Layer (Fastify REST Gateway)

```bash
cd service

# Install dependencies
npm install

# Run TypeScript type check
npm run typecheck

# Run unit and integration tests (Vitest)
npm test

# Start development server with hot-reload
npm run dev
```

### C. React 19 Console UI

```bash
cd console

# Install dependencies
npm install

# Run TypeScript type check
npm run typecheck

# Run component tests (Vitest + Testing Library)
npm test

# Start local Vite development server
npm run dev
```

### D. Full Local Development Stack (Docker Compose)

```bash
# Spin up engine, service gateway, console, MongoDB, Prometheus, and Grafana
docker compose up -d

# Verify container health
docker compose ps
```

---

## 5. Current Implementation Status vs Planned Capabilities

For an honest, comprehensive subsystem breakdown, see [`docs/implementation-status.md`](file:///c:/Users/user/OneDrive/Desktop/strata/docs/implementation-status.md).

### Currently Implemented & Tested
- **Repository Architecture & Toolchain**: Multi-module Gradle build, Java 25 incubator flags, FFM native access, Buf configuration, Docker Compose, and CI workflows.
- **SIMD Distance Kernels**: Java Vector API (`jdk.incubator.vector`) dot product, Euclidean distance, and cosine distance kernels with scalar fallback.
- **Off-Heap Memory Allocator**: Java Foreign Function & Memory (FFM) `Arena`/`MemorySegment` resource wrapper.
- **Metadata-Based Tenant Isolation**: gRPC `TenantContextInterceptor` strictly extracting `x-tenant-id` header metadata.
- **REST Gateway & Auth Middleware**: Fastify HTTP server with Zod validation, API key middleware, domain error mapping, and Prometheus metrics.
- **Console UI Scaffold**: React 19 + Tailwind CSS 4 application with Collections, Query Console, Index Explorer, and Benchmark Lab views.

### Planned for Phase 1 Vertical Slice
- `CreateCollection` → `UpsertPoints` → `ExactSearch` → `DeletePoints` end-to-end flow.
- Append-only WAL file writing and checksum validation.
- Flat exact index vector storage and brute-force oracle verification.
- Replay-based crash recovery tests.
