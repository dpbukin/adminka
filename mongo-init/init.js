db = connect("mongodb://localhost:27017/orders");

db = db.getSiblingDB('orders');

db.createUser({
    user: 'admin',
    pwd: 'admin',
    roles: [{ role: 'readWrite', db: 'orders' }]
});

db.orders.insertMany([
    { orderId: '1', customerName: 'John Doe', amount: 123.45, date: '2025-01-07' },
    { orderId: '2', customerName: 'John Doe 2', amount: 1232.45, date: '2025-01-08' }
]);
