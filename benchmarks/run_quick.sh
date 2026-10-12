#!/usr/bin/env bash
set -euo pipefail

echo "Running STRATA Quick Benchmark Suite..."
python filtered_bench.py "${1:-http://localhost:8080}"
