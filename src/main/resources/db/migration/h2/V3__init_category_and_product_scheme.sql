CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    depth INT,
    parent_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price BIGINT NOT NULL,
    stock_quantity INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    seller_id BIGINT,
    category_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
