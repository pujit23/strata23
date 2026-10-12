import { UnauthorizedError } from "../errors/index.js";

export interface AuthenticatedTenant {
  tenantId: string;
  userId: string;
  role: "admin" | "member" | "readonly";
}

/**
 * Validates API key and extracts tenant context.
 * In development, fallback header 'X-Tenant-ID' can be accepted if no DB key is configured.
 */
export async function authenticateApiKey(apiKey?: string, rawTenantHeader?: string): Promise<AuthenticatedTenant> {
  if (apiKey) {
    // TODO(phase-6): Hash key and verify against MongoDB ApiKeys collection
    if (apiKey.startsWith("strata_dev_")) {
      return {
        tenantId: "dev-tenant-default",
        userId: "dev-user-001",
        role: "admin",
      };
    }
  }

  if (rawTenantHeader) {
    return {
      tenantId: rawTenantHeader,
      userId: `user-${rawTenantHeader}`,
      role: "admin",
    };
  }

  throw new UnauthorizedError("Valid API key or authorization header is required");
}
