import { z } from "zod";
import dotenv from "dotenv";

dotenv.config();

const envSchema = z.object({
  NODE_ENV: z.enum(["development", "test", "production"]).default("development"),
  SERVICE_PORT: z.coerce.number().default(8080),
  SERVICE_HOST: z.string().default("0.0.0.0"),
  MONGODB_URI: z.string().default("mongodb://localhost:27017/strata"),
  ENGINE_GRPC_HOST: z.string().default("127.0.0.1"),
  ENGINE_GRPC_PORT: z.coerce.number().default(50051),
  INTERNAL_AUTH_TOKEN: z.string().default("dev_internal_grpc_secret"),
  JWT_SECRET: z.string().default("dev_jwt_secret_min_32_chars_placeholder"),
});

export type ServiceConfig = z.infer<typeof envSchema>;

export const config: ServiceConfig = envSchema.parse({
  NODE_ENV: process.env.NODE_ENV,
  SERVICE_PORT: process.env.SERVICE_PORT,
  SERVICE_HOST: process.env.SERVICE_HOST,
  MONGODB_URI: process.env.MONGODB_URI,
  ENGINE_GRPC_HOST: process.env.ENGINE_GRPC_HOST,
  ENGINE_GRPC_PORT: process.env.ENGINE_GRPC_PORT,
  INTERNAL_AUTH_TOKEN: process.env.ENGINE_GRPC_INTERNAL_AUTH_TOKEN,
  JWT_SECRET: process.env.JWT_SECRET,
});
