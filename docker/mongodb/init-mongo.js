// MongoDB initialization script
db = db.getSiblingDB("strata");

db.createCollection("users");
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "tenantId": 1 });

db.createCollection("api_keys");
db.api_keys.createIndex({ "keyHash": 1 }, { unique: true });
db.api_keys.createIndex({ "tenantId": 1 });

print("STRATA MongoDB collections and indexes initialized.");
