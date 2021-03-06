DROP TABLE IF EXISTS t_customer;

CREATE TABLE t_customer (
  id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  name VARCHAR(30) NOT NULL COMMENT '客户名',
  age INT(11) NULL DEFAULT NULL COMMENT '客户年龄',
  note VARCHAR(255) NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS t_product;

CREATE TABLE t_product (
  id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  name VARCHAR(30) NOT NULL COMMENT '产品名',
  note VARCHAR(255) NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS t_order;

CREATE TABLE t_order (
  id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  customer_id BIGINT(20) NOT NULL COMMENT '客户ID',
  product_id BIGINT(20) NOT NULL COMMENT '产品ID',
  quantity INT(11) NOT NULL COMMENT '购买数量',
  note VARCHAR(255) NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY(id),
  FOREIGN KEY(customer_id) REFERENCES t_customer(id),
  FOREIGN KEY(product_id) REFERENCES t_product(id)
);
