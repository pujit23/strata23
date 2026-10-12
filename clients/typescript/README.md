# STRATA TypeScript Client SDK

Official TypeScript/JavaScript SDK for STRATA Vector Database.

## Installation

```bash
npm install @strata/client
```

## Basic Usage

```typescript
import { StrataClient } from "@strata/client";

const client = new StrataClient({
  baseUrl: "http://localhost:8080",
  apiKey: "your-api-key",
});

const health = await client.health();
console.log(health);
```
