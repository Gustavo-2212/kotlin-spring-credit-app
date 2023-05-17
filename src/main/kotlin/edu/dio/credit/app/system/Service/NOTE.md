### Camada de Serviços

> Comandos básicos para recuperação dos dados que as classes em **model** modelam

* Interface **Credit**
  * save(credit: Credit): Credit
  * findByCustomer(customerId: Long): List<Credit>
  * findByCreditCode(creditCode: UUID): Credit

* Interface **Customer**
  * save(customer: Customer): Customer
  * findById(id: Long): Customer
  * delete(id: Long): Customer

> Também queremos ter a opção de atualizar um registro da tabela Customer,
> porém como já temos os comandos básicos (save, delete, findById) para essa tarefa e a regra de negócio
> dessa atualização não ser muito complexa, não precisaremos adicionar uma função **update(customerId: Long, customer: Customer): Customer**

---

### Agora temos as Implementações **./Impl**

> Iremos criar as classes que implementam as interfaces criadas (CustomerService, CreditService).
> E anotarmos a classe com o **@Service**.
> 
> Agora, vamos implementar as funções. Para isso, precisaremos do módulo que se comunica com o Banco de Dados,
> a saber, o **Repository**. Através da injeção de dependência, devemos ter acesso ao **repository** respectivo
> da classe que estamos implementando.

* **Classes Concretas Service**
  * ```class CustomerService(private val customerRepository: CustomerRepository): ICustomerService {...}```
  * ```class CreditService(private val creditRepository: CreditRepository): ICreditService {...}```

> Usaremos esse objeto para fazermos as operações sobre os dados. Lembre das operações **find**,
> pois elas retornam um **Optional** (caso não encontrem o **Id**), logo é necessário tratarmos
> essa condição.

---

Para a classe **CreditService**, precisamos dar mais atenção, pois suas funções de busca
não existemo no **JPARepository**, além de que um Crédito só existe se estiver atrelado a
um registro de **Customer**.

Dito isso, devemos adicionar a interface **CreditRepositoy** esses métodos que não padrões. O JPA
já cuida de criar a **query** correta de acordo com a assinatura do método (**Named Query**).
Na classe **CreditService**, fazemos uso da **injeção de dependência** para termos acesso ao método criado.

* **Named Query**
```kotlin
  fun findByCreditCode(creditCode: UUID): Credit
```

**Obs.:** Poderíamos usar o **Native Query**. Temos um exemplo para outro método da classe **CreditService**.

* **Native Query**
```kotlin 
  @Query(value = "SELECT * FROM tab_credit WHERE customer_id = ?1", nativeQuery = true)
  fun findAllByCustomer(customerId: Long): List<Credit>
```
