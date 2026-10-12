# STRATA API Specification

STRATA separates its interface into two primary boundaries:

## 1. External REST API (Service Layer)

- **Base URL**: `http://localhost:8080`
- **Authentication**: `X-API-Key: strata_...` header or Bearer JWT token.
- **Specification**: Complete OpenAPI 3.1 specification available at [`service/openapi.yaml`](file:///c:/Users/user/OneDrive/Desktop/strata/service/openapi.yaml).

### Core Endpoints

- `GET /health` — Service health check.
- `GET /metrics` — Prometheus metrics scrape endpoint.
- `GET /api/v1/collections` — List all collections for authenticated tenant.
- `POST /api/v1/collections` — Create a new vector collection.
- `GET /api/v1/collections/:name` — Retrieve collection schema and statistics.
- `DELETE /api/v1/collections/:name` — Drop a collection.
- `POST /api/v1/collections/:name/points` — Upsert vector points with optional JSON payload.
- `DELETE /api/v1/collections/:name/points` — Delete vector points by ID.
- `POST /api/v1/collections/:name/search` — Query nearest neighbors with filter expressions.

---

## 2. Internal gRPC Interface (Engine Layer)

- **Port**: `50051`
- **Contract**: Defined in [`proto/strata/v1/strata.proto`](file:///c:/Users/user/OneDrive/Desktop/strata/proto/strata/v1/strata.proto).
- **Tenant Context**: Transmitted exclusively via metadata header `x-strata-tenant-id`.
- **Internal Authentication**: Shared internal bearer secret via metadata header `authorization`.
