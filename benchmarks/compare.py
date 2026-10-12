"""
Compares approximate vector search results against the exact brute-force oracle
to compute recall@K, precision, and latency trade-offs.
"""

import sys


def compare_recall(oracle_results_file: str, strata_results_file: str, top_k: int = 10):
    print(f"Comparing STRATA results from {strata_results_file} against oracle {oracle_results_file}...")
    print(f"Top-K: {top_k}")
    # TODO(phase-7): Parse ground-truth oracle neighbors and compute exact recall intersection
    print("Scaffold: No measurements recorded yet.")


if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python compare.py <oracle_file> <strata_file> [top_k]")
        sys.exit(0)
    compare_recall(sys.argv[1], sys.argv[2])
