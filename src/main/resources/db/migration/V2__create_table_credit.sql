CREATE TABLE tab_credit (
  id BIGINT AUTO_INCREMENT NOT NULL,
   credit_code UUID NOT NULL,
   credit_value DECIMAL NOT NULL,
   day_first_installment date NOT NULL,
   number_of_installments INT NOT NULL,
   status INT,
   customer_id BIGINT,
   CONSTRAINT pk_tab_credit PRIMARY KEY (id)
);

ALTER TABLE tab_credit ADD CONSTRAINT uc_tab_credit_credit_code UNIQUE (credit_code);

ALTER TABLE tab_credit ADD CONSTRAINT FK_TAB_CREDIT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES tab_customer (id);