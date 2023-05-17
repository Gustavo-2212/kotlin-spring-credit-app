### Camada de Persistência *MODEL*

> Criaremos **data class** representando o que temos no banco de dados.

Criaremos as seguintes classes:

* **Customer** (id, firstName, lastName, cpf, email, password, address, credits)
* **Address** (zipCode, street)
* **Credit** (id, creditCode, creditValue, dayFirstInstallment, numberOfInstallments, status, customer)
  * **creditCode** foi gerado com o UUID, para mais informações entre no link: [UUID](https://www.uuidgenerator.net/dev-corner/kotlin)

> OBS.: Temos também uma **enum class Status** para setar o status de cada *installment*

### Adicionando as **Annotations**

> 1. Configurando o arquivo **application.yaml** para acessar o banco em memória [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
> 2. Adicionar as anotações **Entity, Table, Id, GeneratedValue, Column, OntToMany, ManyToOne, Embedded, Embeddable, ...**
> 3. Adicionando configurações sobre JPA/Hibernate no **application.yaml**. Irá criar as tabelas automaticamente.


### Usando o **Flyway**

Para fazer o versionamento do nosso banco de dados.

> 1. Instalar um pluggin **JPA Buddy** para ajudar na criação das **migrations**
> 2. Copiar o DDL da classe **Customer**
> 3. Em **./resources/db.migration** adicionar um arquivo [New] > [Other] > [Flyway Empty Migration]
> 4. Nomeie descritivamente, como por exemplo: "V1_create_table_customer.sql" e **cole** o DDL copiado do item .2
> 5. Faça o mesmo processo com as outras classes: **Credit**
> 6. Agora, reconfiguramos o `jpa/hibernate/ddl-auto` no **application.yaml**


### Repositories

> Precisamos agora criar os protocolos de acesso aos dados no banco de dados,
> através dos objetos modelados até então. (DAO Repository)

Criamos na pasta **../Repository/** as interfaces **Credit** e **Customer**
