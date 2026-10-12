import { FastifyInstance } from "fastify";
import { register } from "../observability/metrics.js";

export async function healthRoutes(fastify: FastifyInstance) {
  fastify.get("/health", async () => {
    return {
      status: "ok",
      version: "3.0.0",
      timestamp: new Date().toISOString(),
    };
  });

  fastify.get("/metrics", async (request, reply) => {
    reply.header("Content-Type", register.contentType);
    return register.metrics();
  });
}
