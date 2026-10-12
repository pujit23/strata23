import { describe, it, expect, beforeAll, afterAll } from "vitest";
import { FastifyInstance } from "fastify";
import { buildApp } from "../../src/app.js";

describe("Service API Integration", () => {
  let app: FastifyInstance;

  beforeAll(async () => {
    app = await buildApp();
  });

  afterAll(async () => {
    await app.close();
  });

  it("responds to /health with status ok", async () => {
    const res = await app.inject({
      method: "GET",
      url: "/health",
    });

    expect(res.statusCode).toBe(200);
    const body = JSON.parse(res.body);
    expect(body.status).toBe("ok");
    expect(body.version).toBe("3.0.0");
  });

  it("rejects unauthorized access to collections without API key or tenant", async () => {
    const res = await app.inject({
      method: "GET",
      url: "/api/v1/collections",
    });

    expect(res.statusCode).toBe(401);
  });

  it("allows access with development tenant header", async () => {
    const res = await app.inject({
      method: "GET",
      url: "/api/v1/collections",
      headers: {
        "x-tenant-id": "test-tenant",
      },
    });

    expect(res.statusCode).toBe(200);
    const body = JSON.parse(res.body);
    expect(body.tenantId).toBe("test-tenant");
  });
});
