import { buildApp } from "./app.js";
import { config } from "./config/index.js";
import { userRepository } from "./repositories/userRepository.js";

async function start() {
  const app = await buildApp();

  try {
    await userRepository.connect();
    await app.listen({ port: config.SERVICE_PORT, host: config.SERVICE_HOST });
    app.log.info(`STRATA API Gateway listening at http://${config.SERVICE_HOST}:${config.SERVICE_PORT}`);
  } catch (err) {
    app.log.error(err);
    process.exit(1);
  }
}

start();
