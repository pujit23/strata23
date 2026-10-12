# STRATA Python Client SDK

Official Python SDK for the STRATA single-node filter-aware vector database.

## Installation

```bash
pip install strata-client
```

## Basic Usage

```python
from strata_client import StrataClient

client = StrataClient(base_url="http://localhost:8080", api_key="your-api-key")
print(client.health())
```
