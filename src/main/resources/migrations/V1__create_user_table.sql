-- Creating table contacts
CREATE TABLE IF NOT EXISTS t_contacts
(
    id          UUID  NOT NULL PRIMARY KEY,
    phone       VARCHAR(100) NOT NULL,
    email       VARCHAR(250) NOT NULL
);

-- Creating table USERS
CREATE TABLE IF NOT EXISTS t_users
(
    id                 UUID  NOT NULL PRIMARY KEY,
    name               VARCHAR(150) NOT NULL,
    nuit               VARCHAR(15)  NOT NULL,
    fk_contact_id      UUID NOT NULL UNIQUE REFERENCES t_contacts (id),
    street             VARCHAR(255),
    number             VARCHAR(255),
    city               VARCHAR(255),
    zip_code           VARCHAR(255),
    province           VARCHAR(255),
    country            VARCHAR(255),
    username           VARCHAR(50)   NOT NULL UNIQUE,
    password           VARCHAR(255)  NOT NULL,
    type               VARCHAR(100),
    is_active          BOOLEAN       NOT NULL,
    is_deleted         BOOLEAN       NOT NULL,
    created_by         VARCHAR(50)   NOT NULL,
    created_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
