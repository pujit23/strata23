import { FastifyInstance } from "fastify";
import { CreateCollectionSchema, SearchQuerySchema } from "../schemas/collectionSchemas.js";
import { ValidationError } from "../errors/index.js";

export async function collectionRoutes(fastify: FastifyInstance) {
  fastify.get("/api/v1/collections", async (request) => {
    const tenant = request.tenant;
    return {
      tenantId: tenant?.tenantId,
      collections: [],
      // Scaffold stub
    };
  });

  fastify.post("/api/v1/collections", async (request, reply) => {
    const parseResult = CreateCollectionSchema.safeParse(request.body);
    if (!parseResult.success) {
      throw new ValidationError(parseResult.error.message);
    }

    const { name, dimension, metric } = parseResult.data;
    const tenantId = request.tenant?.tenantId;

    // TODO(phase-1): Forward gRPC CreateCollectionRequest with tenant metadata
    reply.status(201);
    return {
      success: true,
      collection: { name, dimension, metric, tenantId },
    };
  });

  fastify.post("/api/v1/collections/:name/search", async (request) => {
    const parseResult = SearchQuerySchema.safeParse(request.body);
    if (!parseResult.success) {
      throw new ValidationError(parseResult.error.message);
    }

    const { vector, topK } = parseResult.data;
    const { name } = request.params as { name: string };
    const tenantId = request.tenant?.tenantId;

    // TODO(phase-1): Forward gRPC SearchRequest to engine
    return {
      collection: name,
      tenantId,
      results: [],
      topK,
      queryDimension: vector.length,
    };
  });
}
