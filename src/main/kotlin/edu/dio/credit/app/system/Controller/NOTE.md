### Camada de Controle

> Camada usada para definir os **Endpoints** de sua aplicação.
> Aqui tratamos os envios das classes de transferência que estarão
> transistando entre o cliente e a aplicação.

* **Obs.:**
  * ````kotlin
    @RestController
    @RequestMapping("/api/client")
    class ClientController {...}
````