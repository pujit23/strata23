# Multi-stage Dockerfile for STRATA Service Gateway
FROM node:22-alpine AS builder

WORKDIR /app
COPY service/package*.json ./
RUN npm ci

COPY proto/ ../proto/
COPY service/tsconfig.json ./
COPY service/src/ src/

RUN npm run build

FROM node:22-alpine

WORKDIR /app
COPY service/package*.json ./
RUN npm ci --omit=dev

COPY proto/ ../proto/
COPY --from=builder /app/dist dist/

ENV NODE_ENV=production
ENV SERVICE_PORT=8080
ENV SERVICE_HOST=0.0.0.0

EXPOSE 8080

CMD ["node", "dist/server.js"]
