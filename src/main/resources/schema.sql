DROP TABLE IF EXISTS order;
CREATE TABLE order (
    id bigint NOT NULL AUTO_INCREMENT,
    paid boolean DEFAULT False,
    PRIMARY KEY(id)
);

DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(45) NOT NULL,
    description varchar(160),
    amount bigint NOT NULL,
    price DECIMAL NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS shopping_list;
CREATE TABLE shopping_list (
    id bigint NOT NULL AUTO_INCREMENT,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    amount bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT shopping_lists_order_id_fk FOREIGN KEY (order_id) REFERENCES order (id),
    CONSTRAINT shopping_lists_product_id_fk FOREIGN KEY (product_id) REFERENCES product (id)
);