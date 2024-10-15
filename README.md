# Golden Raspberry Awards API

## Descrição

Este projeto implementa uma API RESTful para possibilitar a leitura da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards. A API permite consultar informações sobre filmes, produtores e suas respectivas premiações.

## Requisitos do Sistema

1. **Ler o arquivo CSV dos filmes** e inserir os dados em uma base de dados ao iniciar a aplicação.

## Requisitos da API

- **Obter o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido.**

## Requisitos Não Funcionais

1. O web service RESTful é implementado com base no nível 2 de maturidade de Richardson.
2. Apenas testes de integração foram implementados para garantir que os dados obtidos estão de acordo com os dados fornecidos na proposta.
3. O banco de dados utilizado é em memória (H2), não requerendo instalação externa.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Maven
- H2 Database
- JUnit
- Mockito

## Estrutura do Projeto

- **src**: Contém o código-fonte da aplicação.
- **resources**: Inclui arquivos de configuração e outros recursos necessários.
## Como Executar o Projeto

1. Clone o repositório:

   `git clone https://github.com/walterribas/goldenRaspberryAwards`

   `cd goldenRaspberryAwards`


2. Compile o projeto:

   `mvn clean install`


3. Execute a aplicação:

   `mvn spring-boot:run`


4. Acesse a API:
- **A API estará disponível em http://localhost:8080.**
- **Utilize a coleção Postman incluída na pasta `resources` para testar as endpoints**

## Testes de Integração
Os testes de integração foram implementados na classe `MovieControllerTest`. Para executá-los, utilize o seguinte comando:

   `mvn test`

