import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { config } from "../config/index.js";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Path to root proto definition
const PROTO_PATH = path.resolve(__dirname, "../../../proto/strata/v1/strata.proto");

export interface EngineClientWrapper {
  createMetadata(tenantId: string): grpc.Metadata;
  getRawClient(): grpc.Client | null;
  checkHealth(): Promise<boolean>;
}

class EngineClientManager implements EngineClientWrapper {
  private client: any = null;

  constructor() {
    this.initClient();
  }

  private initClient() {
    try {
      const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
        keepCase: false,
        longs: String,
        enums: String,
        defaults: true,
        oneofs: true,
      });

      const protoDescriptor = grpc.loadPackageDefinition(packageDefinition) as any;
      const StrataEngineService = protoDescriptor.strata.v1.StrataEngineService;

      const target = `${config.ENGINE_GRPC_HOST}:${config.ENGINE_GRPC_PORT}`;
      this.client = new StrataEngineService(target, grpc.credentials.createInsecure());
    } catch (err) {
      // In early scaffold or offline test, allow graceful degradation
      console.warn("Notice: Engine gRPC client not connected to live daemon:", err);
    }
  }

  public createMetadata(tenantId: string): grpc.Metadata {
    const meta = new grpc.Metadata();
    meta.add("x-tenant-id", tenantId);
    meta.add("authorization", `Bearer ${config.INTERNAL_AUTH_TOKEN}`);
    return meta;
  }

  public getRawClient(): grpc.Client | null {
    return this.client;
  }

  public async checkHealth(): Promise<boolean> {
    if (!this.client) return false;
    return new Promise((resolve) => {
      this.client.healthCheck({}, (err: any, response: any) => {
        if (err || !response) {
          resolve(false);
        } else {
          resolve(true);
        }
      });
    });
  }
}

export const engineClient: EngineClientWrapper = new EngineClientManager();
