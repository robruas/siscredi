Biblioteca Sicredi - Gerenciamento de Livros
API REST desenvolvida com Spring Boot para o gerenciamento de uma biblioteca, utilizando MongoDB como banco de dados principal e Redis para cache, ambos operando em modo embarcado para facilitar o desenvolvimento local.

🚀 Tecnologias e Stack
Java 21: Versão mais recente do JDK de suporte longo (LTS).
Spring Boot 3.4.5: Framework base para construção da aplicação.
Spring Data MongoDB: Abstração para persistência de dados no MongoDB.
Spring Data Redis & Cache: Gerenciamento de cache para otimização de consultas.
MongoDB Embarcado (Flapdoodle): Banco de dados NoSQL que sobe automaticamente com a aplicação.
Redis Embarcado: Sistema de cache em memória que sobe automaticamente com a aplicação.
SpringDoc OpenAPI (Swagger): Documentação interativa da API.
Lombok & MapStruct: Redução de código boilerplate e mapeamento eficiente de DTOs.
🛠️ Arquitetura e Organização
A aplicação segue uma arquitetura em camadas bem definida:

Controller: @G:\Meu Drive\Desenvolvimento\dev\siscredi\src\main\java\br\com\sicredi\biblioteca\controller\LivroController.java:1-79 - Expõe os endpoints REST.
Service: @G:\Meu Drive\Desenvolvimento\dev\siscredi\src\main\java\br\com\sicredi\biblioteca\service\impl\LivroServiceImpl.java:1-85 - Contém as regras de negócio e integração com cache.
Repository: @G:\Meu Drive\Desenvolvimento\dev\siscredi\src\main\java\br\com\sicredi\biblioteca\repository\LivroRepository.java:1-12 - Interface de comunicação com o MongoDB.
Config: Configurações de beans, cache e bancos de dados embarcados.
⚡ Funcionalidades Principais
CRUD de Livros: Cadastro, consulta, atualização e exclusão.
Paginação e Filtros: Listagem de livros com suporte a paginação obrigatória e filtro opcional por gênero.
Cache Inteligente:
Consultas por ID são cacheadas no Redis.
O cache é automaticamente invalidado em operações de atualização ou remoção.
Carga de Dados Inicial: O @G:\Meu Drive\Desenvolvimento\dev\siscredi\src\main\java\br\com\sicredi\biblioteca\config\DataLoader.java:1-55 popula o banco com dados de exemplo ao iniciar.
📋 Endpoints Principais
Acesse a documentação completa via Swagger em: http://localhost:8080/swagger-ui.html

POST /livros: Cria um novo livro.
GET /livros/{id}: Busca um livro por ID (com cache).
GET /livros?pagina=0&tamanho=10&genero=TECNOLOGIA: Lista livros de forma paginada.
PUT /livros/{id}: Atualiza um livro existente.
DELETE /livros/{id}: Remove um livro.
⚙️ Como Rodar
Certifique-se de ter o Java 21 e Maven instalados.
Clone o repositório ou extraia os arquivos.
Importe como um projeto Maven em sua IDE (STS, IntelliJ, VS Code).
Execute a classe principal: @G:\Meu Drive\Desenvolvimento\dev\siscredi\src\main\java\br\com\sicredi\biblioteca\BibliotecaApplication.java:1-15.
Nota: Não é necessário ter MongoDB ou Redis instalados/rodando via Docker; a aplicação iniciará instâncias embarcadas automaticamente.