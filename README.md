# ProductServiceAPI

![Arquitetura de Microserviços](microservicos.jpg)

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