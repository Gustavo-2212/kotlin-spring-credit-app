CREATE TABLE tab_customer (
  id BIGINT AUTO_INCREMENT NOT NULL,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255) NOT NULL,
   cpf VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   zip_code VARCHAR(255) NOT NULL,
   street VARCHAR(255) NOT NULL,
   CONSTRAINT pk_tab_customer PRIMARY KEY (id)
);

ALTER TABLE tab_customer ADD CONSTRAINT uc_tab_customer_cpf UNIQUE (cpf);

ALTER TABLE tab_customer ADD CONSTRAINT uc_tab_customer_email UNIQUE (email);