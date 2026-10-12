import { describe, it, expect } from "vitest";
import { engineClient } from "../../src/grpc/client.js";

describe("EngineClientWrapper", () => {
  it("should create gRPC metadata with x-tenant-id per STRATA v3.0 spec", () => {
    const tenantId = "tenant-enterprise-99";
    const meta = engineClient.createMetadata(tenantId);
    
    const values = meta.get("x-tenant-id");
    expect(values).toHaveLength(1);
    expect(values[0]).toBe("tenant-enterprise-99");
  });

  it("should attach bearer authorization header to metadata", () => {
    const meta = engineClient.createMetadata("tenant-1");
    const auth = meta.get("authorization");
    expect(auth).toHaveLength(1);
    expect(auth[0]).toContain("Bearer ");
  });
});
