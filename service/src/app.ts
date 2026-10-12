import Fastify, { FastifyInstance } from "fastify";
import cors from "@fastify/cors";
import helmet from "@fastify/helmet";
import sensible from "@fastify/sensible";
import tenantPlugin from "./middleware/tenantContext.js";
import { healthRoutes } from "./routes/healthRoutes.js";
import { collectionRoutes } from "./routes/collectionRoutes.js";
import { StrataError } from "./errors/index.js";

export async function buildApp(): Promise<FastifyInstance> {
  const app = Fastify({
    logger: {
      level: process.env.SERVICE_LOG_LEVEL || "info",
    },
  });

  await app.register(sensible);
  await app.register(cors, { origin: true });
  await app.register(helmet, { contentSecurityPolicy: false });

  await app.register(tenantPlugin);
  await app.register(healthRoutes);
  await app.register(collectionRoutes);

  app.setErrorHandler((error, request, reply) => {
    if (error instanceof StrataError) {
      reply.status(error.statusCode).send({
        error: error.code,
        message: error.message,
      });
      return;
    }

    request.log.error(error);
    reply.status(500).send({
      error: "INTERNAL_SERVER_ERROR",
      message: "An unexpected internal error occurred",
    });
  });

  return app;
}
