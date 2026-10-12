# STRATA Benchmarking & Recall Evaluation

## 1. Objectives

The STRATA benchmark suite provides empirical validation of:
1. **Query Throughput (QPS)** across concurrent clients.
2. **Latency Distributions** (p50, p90, p99, max in microseconds).
3. **Recall@K Quality** compared against the exact brute-force oracle.
4. **Filter Selectivity Performance** across five regimes:
   - Ultra-selective (selectivity < 0.1%)
   - Highly selective (0.1% - 2%)
   - Moderate selectivity (2% - 20%)
   - Broad selectivity (20% - 80%)
   - Unfiltered (100%)

## 2. Tools

- **JMH**: Microbenchmarks for Java Vector API SIMD distance kernels (`VectorDistance`).
- **Python Harness (`benchmarks/`)**: End-to-end dataset generation, filtered workload replay, and oracle comparison.
- **HDR Histogram**: Tail latency measurement without coordinated omission.
