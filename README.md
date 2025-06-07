# ProductServiceAPI

Projeto base para microserviço de produtos, pronto para integração em arquitetura de microserviços com Java Spring Boot, MySQL, Redis e Kafka.

## 📚 Descrição
API de produtos construída com Spring Boot, conectada a MySQL, Redis e Kafka, pronta para ser usada como microserviço em uma arquitetura maior. Permite cadastro, consulta, associação com usuários externos, cache de usuários, mensageria e tratamento robusto de erros.

## 🚀 Tecnologias
- Java 17+
- Spring Boot 3.5
- Spring Data JPA (MySQL)
- Spring Data Redis
- Spring Kafka
- Resilience4j (circuit breaker)
- Docker Compose (MySQL, Redis, Kafka, Zookeeper)
- Maven

## ⚙️ Requisitos
- Java 17 ou superior
- Maven
- Docker e Docker Compose

## 🐬 Subindo os serviços com Docker Compose
Execute no terminal:
```sh
docker-compose up -d
```
Isso irá subir:
- Redis (localhost:6379)
- Kafka (localhost:9092)
- Zookeeper (localhost:2181)

Configure o MySQL conforme seu ambiente (ou use o Docker também):
```sh
docker run --name meu-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=productservicedb -p 3306:3306 -d mysql:8.0
```

## 🔧 Configuração
O arquivo `src/main/resources/application.properties` já está preparado para variáveis de ambiente:
```properties
spring.datasource.url=${MYSQL_URL:jdbc:mysql://127.0.0.1:3306/productservicedb?createDatabaseIfNotExist=true}
spring.datasource.username=${MYSQL_USER:root}
spring.datasource.password=${MYSQL_PASSWORD:root}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.initialization-mode=always
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
user.api.url=${USER_API_URL:http://localhost:8080}
spring.redis.host=${REDIS_HOST:localhost}
spring.redis.port=${REDIS_PORT:6379}
```

## 🏗️ Rodando o Projeto
1. Clone o repositório
2. Suba o MySQL, Redis, Kafka e Zookeeper (veja acima)
3. Execute:
   ```sh
   ./mvnw spring-boot:run
   ```

## 🌱 Seed de Dados
O arquivo `src/main/resources/data.sql` insere produtos automaticamente ao iniciar o app:
```sql
INSERT INTO product (name, description, price, quantity, user_id) VALUES ('iPhone 15', 'Apple iPhone 15, 128GB, Preto', 6999.00, 50, 1);
INSERT INTO product (name, description, price, quantity, user_id) VALUES ('Samsung Galaxy S24', 'Samsung Galaxy S24, 256GB, Prata', 5999.00, 40, 2);
-- ...
```

## 🧩 Estrutura do Projeto
```
src/main/java/com/microservicos/ProductServiceAPI/
  controllers/
  dtos/
  errors/
  models/
  repositories/
  services/
src/main/resources/
  application.properties
  data.sql
```

## 📖 Exemplos de Endpoints
- `GET /products` — Lista todos os produtos (com dados do usuário associado)
- `POST /products` — Cria um novo produto
  - Body:
    ```json
    {
      "name": "Produto XPTO",
      "description": "Descrição do produto XPTO",
      "price": 149.99,
      "quantity": 10,
      "userId": 1
    }
    ```
- `POST /products/with-user` — Cria usuário e produto juntos
  - Body:
    ```json
    {
      "name": "iPhone 15",
      "description": "Apple iPhone 15, 128GB, Preto",
      "price": 6999.00,
      "quantity": 10,
      "userName": "João Silva",
      "userEmail": "joao@email.com"
    }
    ```

## 📨 Kafka
- Producer pronto para enviar `ProductUserDTO` como JSON para o tópico `product-user-topic`.
- Exemplo de uso:
  ```java
  @Autowired
  private ProducerKafkaMessage producerKafkaMessage;
  
  producerKafkaMessage.sendProductUser(productUserDTO);
  ```

## 🛑 Tratamento de erros
Todas as respostas de erro seguem o padrão:
```json
{
  "message": "Mensagem de erro detalhada"
}
```

## ℹ️ Observações
- O serviço de usuários deve estar rodando e acessível na URL configurada.
- O Redis é usado para cache de usuários e resiliência.
- O Kafka está pronto para uso como fila de eventos de produto/usuário.
- Validações de campos são automáticas e retornam erro 400 com mensagem clara.
- O projeto já está pronto para ser expandido com consumers Kafka, autenticação, etc.

---

![Arquitetura de Microserviços](microservicos.jpg)

Desenvolvido para fins didáticos e demonstração de arquitetura de microserviços.