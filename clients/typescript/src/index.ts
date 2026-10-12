export interface StrataClientOptions {
  baseUrl?: string;
  apiKey?: string;
  tenantId?: string;
}

export class StrataClient {
  private baseUrl: string;
  private apiKey?: string;
  private tenantId: string;

  constructor(options: StrataClientOptions = {}) {
    this.baseUrl = (options.baseUrl || "http://localhost:8080").replace(/\/$/, "");
    this.apiKey = options.apiKey;
    this.tenantId = options.tenantId || "default-tenant";
  }

  async health(): Promise<{ status: string; version: string }> {
    const res = await fetch(`${this.baseUrl}/health`);
    return res.json();
  }
}
