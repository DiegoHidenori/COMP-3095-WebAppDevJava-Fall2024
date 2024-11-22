print('START OF MONGO-INIT.JS');

db = db.getSiblingDB('product-service');
db.createUser(
    {
        user: 'admin',
        pwd: 'password',
        roles: [{role: 'readWrite', db: 'product-service'}]
    }
);

db.createCollection('user');

print('END OF MONGO-INIT.JS');