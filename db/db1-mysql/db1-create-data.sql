DELETE FROM t_order;

DELETE FROM t_customer;

INSERT INTO t_customer (name, age, note) VALUES
('Jone', 18, 'test1@baomidou.com'),
('Jack', 20, 'test2@baomidou.com'),
('Tom', 28, 'test3@baomidou.com'),
('Sandy', 21, 'test4@baomidou.com'),
('Billie', 24, 'test5@baomidou.com');

DELETE FROM t_product;

INSERT INTO t_product (name, note) VALUES
('Banana', 'Good Banana...'),
('Apple', 'Good Apple...'),
('Strawberry', 'Good Strawberry...'),
('Pear', 'Good Pear...'),
('Orange', 'Good Orange...');

DELETE FROM t_order;

INSERT INTO t_order (customer_id, product_id, quantity, note) VALUES
(1, 1, 1, 'Buy...'),
(2, 2, 2, 'Buy buy...'),
(3, 3, 3, 'Buy buy buy...');
