# Design 000: System Overview & Architecture

STRATA is structured as a decoupled three-tier system:
1. Java 25 Vector Engine (`strata-core`, `strata-storage`, `strata-query`, `strata-engine`, `strata-cli`).
2. TypeScript Fastify API Gateway (`service/`).
3. React 19 / Vite Web Console (`console/`).

The architecture strictly isolates data plane vector operations from control plane tenant authentication.
Vectors, indexes, and storage never cross into the service or frontend directly; they remain managed inside the off-heap Java memory arenas.
