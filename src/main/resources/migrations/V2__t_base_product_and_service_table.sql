-- Creating table product_service
CREATE TABLE t_base_products
(
    id                 SERIAL PRIMARY KEY,
    name               VARCHAR(255)                                       NOT NULL,
    description        TEXT                                               NOT NULL,
    type               VARCHAR(10) CHECK (type IN ('PRODUCT', 'SERVICE')) NOT NULL,
    price              DECIMAL(10, 2)                                     NOT NULL,
    available          BOOLEAN                                            NOT NULL,
    is_deleted         BOOLEAN                                            NOT NULL,
    created_by         VARCHAR(50)                                        NOT NULL,
    created_date       TIMESTAMP                                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP                                          DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE seq_base_product
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Creating table product
CREATE TABLE t_products
(
    id             BIGINT PRIMARY KEY REFERENCES t_base_products (id) ON DELETE CASCADE,
    stock_quantity INT
);

-- Creating table service
CREATE TABLE t_services
(
    id              BIGINT PRIMARY KEY REFERENCES t_base_products (id) ON DELETE CASCADE,
    estimated_time  BIGINT NOT NULL
);
