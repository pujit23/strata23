#!/usr/bin/env bash
set -euo pipefail

echo "Running Full STRATA Benchmark Matrix (100k - 1M vectors)..."
echo "Target: ${1:-http://localhost:8080}"
# Full execution script
python datasets.py
python filtered_bench.py "${1:-http://localhost:8080}"
