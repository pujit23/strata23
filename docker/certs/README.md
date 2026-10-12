# STRATA Transport Security & Certificates

This directory is designated for internal mTLS certificates securing communication between the Service Layer and the Java Vector Engine.

## Development Usage

In local development (`NODE_ENV=development`), plaintext internal communication on localhost/Docker network is permitted via `ENGINE_GRPC_USE_TLS=false`.

## Production mTLS Generation

For staging or production deployments:
1. Generate Root CA: `openssl req -x509 -newkey rsa:4096 -keyout ca.key -out ca.crt -days 365`
2. Generate Engine Server Certificate: `openssl req -newkey rsa:2048 -nodes -keyout server.key -out server.csr`
3. Generate Service Client Certificate: `openssl req -newkey rsa:2048 -nodes -keyout client.key -out client.csr`

Do not commit private keys (`*.key`, `*.pem`) to git.
