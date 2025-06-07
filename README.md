# ProductServiceAPI

Aplicação de microserviço para cadastro e consulta de produtos, integrando com serviço externo de usuários, cache Redis e mensageria Kafka.

## Funcionalidades
- Cadastro, consulta e listagem de produtos
- Associação de produto a usuário externo (via API REST)
- Criação de produto e usuário juntos
- Cache de usuários com Redis
- Mensageria com Kafka (producer pronto para ProductUserDTO)
- Validação de dados e tratamento global de erros

## Tecnologias
- Java 17
- Spring Boot 3.5
- Spring Data JPA (MySQL)
- Spring Data Redis
- Spring Kafka
- Resilience4j (circuit breaker)
- Docker Compose (MySQL, Redis, Kafka, Zookeeper)

## Requisitos
- Java 17+
- Maven 3.8+
- Docker e Docker Compose

## Subindo os serviços necessários

```sh
docker-compose up -d
```
Isso irá subir:
- Redis (localhost:6379)
- Kafka (localhost:9092)
- Zookeeper (localhost:2181)

Configure o MySQL conforme seu ambiente (ou use o Docker também).

## Configuração
As principais configurações estão em `src/main/resources/application.properties` e podem ser sobrescritas por variáveis de ambiente:

- `MYSQL_URL`, `MYSQL_USER`, `MYSQL_PASSWORD`
- `USER_API_URL` (URL da API de usuários)
- `REDIS_HOST`, `REDIS_PORT`

## Endpoints principais

### Produtos
- `GET /products` — Lista todos os produtos (com dados do usuário associado)
- `POST /products` — Cria um produto (campos validados)
- `POST /products/with-user` — Cria usuário e produto juntos (envie um ProductUserDTO)

### Kafka
- Producer pronto para enviar `ProductUserDTO` como JSON para o tópico `product-user-topic`.

## Exemplo de uso: Criar produto e usuário juntos

```json
POST /products/with-user
{
  "name": "iPhone 15",
  "description": "Apple iPhone 15, 128GB, Preto",
  "price": 6999.00,
  "quantity": 10,
  "userName": "João Silva",
  "userEmail": "joao@email.com"
}
```

## Tratamento de erros
Todas as respostas de erro seguem o padrão:
```json
{
  "message": "Mensagem de erro detalhada"
}
```

## Mensageria Kafka
Para enviar um `ProductUserDTO` para o Kafka:
- Use o serviço `ProducerKafkaMessage.sendProductUser(ProductUserDTO)`
- O JSON será gravado no tópico `product-user-topic`

## Observações
- O serviço de usuários deve estar rodando e acessível na URL configurada.
- O Redis é usado para cache de usuários e resiliência.
- O Kafka está pronto para uso como fila de eventos de produto/usuário.

---

Desenvolvido para fins didáticos e demonstração de arquitetura de microserviços. 