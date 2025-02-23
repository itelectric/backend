CREATE TABLE t_quotation_order
(
    id              UUID PRIMARY KEY,
    description     VARCHAR(255),
    deadline_answer DATE,
    is_deleted      BOOLEAN,
    created_by         VARCHAR(50)        NOT NULL,
    created_date       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE t_quotation_item
(
    id                    UUID PRIMARY KEY,
    quantity              INT  NOT NULL,
    fk_base_product       UUID NOT NULL,
    fk_quotation_order_id UUID NOT NULL,
    is_deleted            BOOLEAN,
    created_by         VARCHAR(50)        NOT NULL,
    created_date       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_base_product FOREIGN KEY (fk_base_product) REFERENCES t_base_product (id) ON DELETE CASCADE,
    CONSTRAINT fk_quotation_order_id FOREIGN KEY (fk_quotation_order_id) REFERENCES t_quotation_order (id) ON DELETE CASCADE
);
