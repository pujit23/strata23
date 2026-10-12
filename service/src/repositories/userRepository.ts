import { MongoClient, Db, Collection } from "mongodb";
import { config } from "../config/index.js";

export interface UserDocument {
  id: string;
  email: string;
  tenantId: string;
  role: "admin" | "member" | "readonly";
  createdAt: Date;
}

export interface ApiKeyDocument {
  id: string;
  keyHash: string;
  prefix: string;
  tenantId: string;
  userId: string;
  createdAt: Date;
  expiresAt?: Date;
}

export class UserRepository {
  private client: MongoClient | null = null;
  private db: Db | null = null;

  async connect(): Promise<void> {
    try {
      this.client = new MongoClient(config.MONGODB_URI);
      await this.client.connect();
      this.db = this.client.db();
    } catch (err) {
      console.warn("MongoDB connection deferred in test/scaffold mode:", err);
    }
  }

  get users(): Collection<UserDocument> | null {
    return this.db ? this.db.collection<UserDocument>("users") : null;
  }

  get apiKeys(): Collection<ApiKeyDocument> | null {
    return this.db ? this.db.collection<ApiKeyDocument>("api_keys") : null;
  }

  async close(): Promise<void> {
    if (this.client) {
      await this.client.close();
    }
  }
}

export const userRepository = new UserRepository();
