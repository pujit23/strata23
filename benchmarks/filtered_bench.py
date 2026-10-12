"""
Filtered vector search benchmark runner.
Evaluates queries across various selectivity ranges (0.1%, 1%, 10%, 50%, 100%).
"""

import sys
import time


def run_benchmark(target_url: str = "http://localhost:8080", collection: str = "bench_128d"):
    print(f"Running STRATA Filtered Search Benchmark against {target_url} on collection {collection}...")
    print("Selectivity | Strategy         | QPS   | p50 (ms) | p99 (ms) | Recall@10")
    print("-------------------------------------------------------------------------")
    print("     0.1%   | PRE_FILTER       | -     | -        | -        | -        (scaffold)")
    print("     1.0%   | ITERATIVE_INDEX  | -     | -        | -        | -        (scaffold)")
    print("    10.0%   | ITERATIVE_INDEX  | -     | -        | -        | -        (scaffold)")
    print("    50.0%   | POST_FILTER      | -     | -        | -        | -        (scaffold)")
    print("   100.0%   | HNSW_PURE        | -     | -        | -        | -        (scaffold)")


if __name__ == "__main__":
    url = sys.argv[1] if len(sys.argv) > 1 else "http://localhost:8080"
    run_benchmark(target_url=url)
