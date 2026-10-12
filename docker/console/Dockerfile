# Multi-stage Dockerfile for STRATA Console
FROM node:22-alpine AS builder

WORKDIR /app
COPY console/package*.json ./
RUN npm ci

COPY console/ ./
RUN npm run build

FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
