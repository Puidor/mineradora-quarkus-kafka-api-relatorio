# API de Relatórios de Oportunidades - Quarkus

Este microsserviço atua como um agregador de eventos, consumindo dados de propostas e cotações de moedas via Apache Kafka para gerar um relatório consolidado de oportunidades de negócio.

A aplicação escuta dois tópicos do Kafka: `proposal` e `quotation`. Ela mantém um registro local das cotações de dólar recebidas. Quando uma nova proposta chega, o serviço a combina com a **última cotação de dólar registrada** para criar uma nova "Oportunidade", persistindo essa informação em seu próprio banco de dados.

Adicionalmente, expõe um endpoint de API seguro (protegido por Keycloak/OIDC) para consultar todos os registros de oportunidades gerados.

## ✨ Funcionalidades

* **Consumo de Propostas:** Escuta o tópico `proposal` para receber novas propostas de minério.
* **Consumo de Cotações:** Escuta o tópico `quotation`, salvando localmente cada nova cotação de dólar recebida.
* **Criação de Oportunidades:** No recebimento de uma proposta, correlaciona-a com a última cotação de dólar salva para gerar um registro de oportunidade (Proposta + Cotação).
* **Relatório de Oportunidades:** Expõe um endpoint `GET` seguro que retorna a lista completa de todas as oportunidades geradas.
* **Segurança:** Integração com Keycloak (OIDC) para autenticação e autorização de endpoints.

## 🚀 Tecnologias Utilizadas

* **Java 17+**
* **Quarkus:** Framework Java nativo para nuvem.
* **Hibernate ORM com Panache:** Para persistência de dados.
* **PostgreSQL:** Banco de dados relacional.
* **SmallRye Reactive Messaging (Kafka):** Para consumo assíncrono de eventos.
* **Quarkus OIDC (Keycloak):** Para segurança e autenticação baseada em JWT.
* **Docker:** Para gerenciamento de dependências.

## 📋 Pré-requisitos

* JDK 17 ou superior
* Maven 3.8+
* Docker e Docker Compose
* Uma instância do **Keycloak** rodando (configurado em `http://localhost:8180/realms/quarkus`).
* Instâncias dos serviços de **Proposta** e **Cotação** rodando e publicando nos tópicos Kafka.

## ⚙️ Como Executar

1.  **Inicie os serviços de dependência (Kafka & PostgreSQL):**

    * **Kafka:** Utilize os mesmos comandos de Zookeeper e Kafka do README da "API de Cotação".
    * **PostgreSQL (Banco deste serviço):**
      Este serviço requer seu próprio banco de dados (`reportdb`). Execute o comando abaixo se ele ainda não existir:
        ```bash
        docker run --name report-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=reportdb -p 5433:5432 -d postgres 
        ```
      *(Nota: Se o PostgreSQL do serviço de Cotação (porta 5432) já estiver rodando, este comando usa a porta `5433` na máquina host para evitar conflitos. Ajuste o arquivo `application.properties` deste serviço para `jdbc:postgresql://localhost:5433/reportdb` se necessário).*

2.  **Inicie o Keycloak:**
    Certifique-se de que seu servidor Keycloak esteja rodando em `http://localhost:8180` com o *realm* `quarkus` e o *client* `backend-service` devidamente configurados.

3.  **Execute a aplicação Quarkus:**

    Abra outro terminal e execute o seguinte comando:
    ```bash
    ./mvnw quarkus:dev
    ```
    A aplicação estará disponível em `http://localhost:8091`.

## 📡 Endpoints da API

A URL base da API é `http://localhost:8091/api/opportunity`.

**Nota:** Todos os endpoints requerem um Token JWT (Bearer Token) válido emitido pelo Keycloak.

| Método | Endpoint | Descrição | Roles Permitidas |
| :--- | :--- | :--- | :--- |
| `GET` | `/data` | Retorna um relatório (lista) de todas as oportunidades de negócio criadas (Propostas + última cotação do Dólar). | `user`, `manager` |