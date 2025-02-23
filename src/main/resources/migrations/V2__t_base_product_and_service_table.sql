-- Creating table product_service
CREATE TABLE t_base_product
(
    id                 UUID PRIMARY KEY,
    name               VARCHAR(255)                                       NOT NULL,
    description        TEXT                                               NOT NULL,
    type               VARCHAR(10) CHECK (type IN ('PRODUCT', 'SERVICE')) NOT NULL,
    price              DECIMAL(10, 2)                                     NOT NULL,
    available          BOOLEAN                                            NOT NULL,
    is_deleted         BOOLEAN                                            NOT NULL,
    created_by         VARCHAR(50)                                        NOT NULL,
    created_date       TIMESTAMP                                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP                                                   DEFAULT CURRENT_TIMESTAMP
);

-- Creating table product
CREATE TABLE t_product
(
    id             UUID PRIMARY KEY REFERENCES t_base_product (id) ON DELETE CASCADE,
    stock_quantity INT
);

-- Creating table service
CREATE TABLE t_service
(
    id             UUID PRIMARY KEY REFERENCES t_base_product (id) ON DELETE CASCADE,
    estimated_time INTERVAL NOT NULL
);
