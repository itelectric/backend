CREATE TABLE t_quotation_orders
(
    id                 SERIAL PRIMARY KEY,
    description        VARCHAR(255),
    deadline_answer    DATE,
    is_deleted         BOOLEAN,
    fk_user_id         UUID NOT NULL,
    created_by         VARCHAR(50)        NOT NULL,
    created_date       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_user_id) REFERENCES t_users (id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_quotation_order
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE t_quotation_items
(
    id                    SERIAL PRIMARY KEY,
    quantity              INT  NOT NULL,
    fk_base_product       BIGINT NOT NULL,
    fk_quotation_order_id BIGINT NOT NULL,
    is_deleted            BOOLEAN,
    created_by         VARCHAR(50)        NOT NULL,
    created_date       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_base_product FOREIGN KEY (fk_base_product) REFERENCES t_base_products (id) ON DELETE CASCADE,
    CONSTRAINT fk_quotation_order_id FOREIGN KEY (fk_quotation_order_id) REFERENCES t_quotation_orders (id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_quotation_item
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;