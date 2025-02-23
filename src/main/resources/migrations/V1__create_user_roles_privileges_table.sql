-- Creating role and privilege table
CREATE TABLE t_privileges
(
    id                 SERIAL             NOT NULL PRIMARY KEY,
    name               VARCHAR(50) UNIQUE NOT NULL,
    is_active          BOOLEAN            NOT NULL,
    is_deleted         BOOLEAN            NOT NULL,
    created_by         VARCHAR(50)        NOT NULL,
    created_date       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE t_roles
(
    id                 SERIAL             NOT NULL PRIMARY KEY,
    name               VARCHAR(50) UNIQUE NOT NULL,
    is_active          BOOLEAN            NOT NULL,
    is_deleted         BOOLEAN            NOT NULL,
    created_by         VARCHAR(50)        NOT NULL,
    created_date       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE t_roles_privileges
(
    fk_role_id      INT NOT NULL,
    fk_privilege_id INT NOT NULL,
    PRIMARY KEY (fk_role_id, fk_privilege_id),
    FOREIGN KEY (fk_role_id) REFERENCES t_roles (id) ON DELETE CASCADE,
    FOREIGN KEY (fk_privilege_id) REFERENCES t_privileges (id) ON DELETE CASCADE
);

-- Creating table contacts
CREATE TABLE IF NOT EXISTS t_contacts
(
    id    UUID         NOT NULL PRIMARY KEY,
    phone VARCHAR(100) NOT NULL,
    email VARCHAR(250) NOT NULL
);

-- Creating table users as user roles table
CREATE TABLE IF NOT EXISTS t_users
(
    id                 UUID         NOT NULL PRIMARY KEY,
    name               VARCHAR(150) NOT NULL,
    nuit               VARCHAR(15)  NOT NULL,
    fk_contact_id      UUID         NOT NULL UNIQUE REFERENCES t_contacts (id),
    street             VARCHAR(255),
    number             VARCHAR(255),
    city               VARCHAR(255),
    zip_code           VARCHAR(255),
    province           VARCHAR(255),
    country            VARCHAR(255),
    username           VARCHAR(50)  NOT NULL UNIQUE,
    password           VARCHAR(255) NOT NULL,
    is_active          BOOLEAN      NOT NULL,
    is_deleted         BOOLEAN      NOT NULL,
    created_by         VARCHAR(50)  NOT NULL,
    created_date       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE t_users_roles
(
    fk_user_id UUID NOT NULL,
    fk_role_id INT  NOT NULL,
    PRIMARY KEY (fk_user_id, fk_role_id),
    FOREIGN KEY (fk_user_id) REFERENCES t_users (id) ON DELETE CASCADE,
    FOREIGN KEY (fk_role_id) REFERENCES t_roles (id) ON DELETE CASCADE
);

-- INSERTING DATA TO PRIVILEGE AND ROLE TABLES
INSERT INTO t_privileges (name, is_active, is_deleted, created_by)
VALUES ('READ_PRIVILEGE', true, false, 'SYSTEM'),
       ('WRITE_PRIVILEGE', true, false, 'SYSTEM'),
       ('DELETE_PRIVILEGE', true, false, 'SYSTEM'),
       ('UPDATE_PRIVILEGE', true, false, 'SYSTEM');

INSERT INTO t_roles (name, is_active, is_deleted, created_by)
VALUES ('ROLE_ADMIN', true, false, 'SYSTEM'),
       ('ROLE_COMPANY', true, false, 'SYSTEM'),
       ('ROLE_CUSTOMER', true, false, 'SYSTEM'),
       ('ROLE_SUPERUSER', true, false, 'SYSTEM');