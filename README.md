# Biblioteca Sicredi - Spring Boot, MongoDB e Redis embarcados

Aplicação criada para rodar diretamente no STS sem Docker, sem MongoDB instalado e sem Redis instalado.

## Stack

- Java 21
- Spring Boot 3.4.5
- Spring Data MongoDB
- Spring Data Redis
- Spring Cache
- MongoDB embarcado via Flapdoodle
- Redis embarcado
- Lombok
- ModelMapper
- SpringDoc OpenAPI
- JUnit 5 / Mockito

## Como rodar no STS

1. Extraia o zip.
2. No STS: `File -> Import -> Maven -> Existing Maven Projects`.
3. Selecione a pasta onde está o `pom.xml`.
4. Clique com botão direito no projeto: `Maven -> Update Project` e marque `Force Update`.
5. Rode: `Run As -> Spring Boot App`.

## Swagger

http://localhost:8080/swagger-ui.html

## Endpoints

- `POST /livros`
- `GET /livros/{id}`
- `GET /livros?pagina=0&tamanho=10&genero=TECNOLOGIA`
- `PUT /livros/{id}`
- `DELETE /livros/{id}`

## Observação

A classe `RuntimeContainersConfig` foi removida porque ela depende de Docker/Testcontainers. Esta versão usa MongoDB e Redis embarcados para execução local.
