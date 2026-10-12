import { FastifyPluginAsync, FastifyRequest } from "fastify";
import fp from "fastify-plugin";
import { authenticateApiKey, AuthenticatedTenant } from "../auth/apiKeyAuth.js";

declare module "fastify" {
  interface FastifyRequest {
    tenant?: AuthenticatedTenant;
  }
}

const tenantPlugin: FastifyPluginAsync = async (fastify) => {
  fastify.decorateRequest("tenant", undefined);

  fastify.addHook("preHandler", async (request: FastifyRequest) => {
    // Skip auth for public health/metrics endpoints
    if (request.url === "/health" || request.url === "/metrics") {
      return;
    }

    const apiKey = request.headers["x-api-key"] as string | undefined;
    const rawTenant = request.headers["x-tenant-id"] as string | undefined;

    request.tenant = await authenticateApiKey(apiKey, rawTenant);
  });
};

export default fp(tenantPlugin, { name: "tenantContext" });
