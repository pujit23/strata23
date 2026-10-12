export class StrataError extends Error {
  constructor(
    message: string,
    public statusCode: number = 500,
    public code: string = "INTERNAL_ERROR"
  ) {
    super(message);
    this.name = "StrataError";
  }
}

export class UnauthorizedError extends StrataError {
  constructor(message: string = "Missing or invalid API key") {
    super(message, 401, "UNAUTHORIZED");
  }
}

export class NotFoundError extends StrataError {
  constructor(message: string = "Resource not found") {
    super(message, 404, "NOT_FOUND");
  }
}

export class ValidationError extends StrataError {
  constructor(message: string = "Validation failed") {
    super(message, 400, "BAD_REQUEST");
  }
}
