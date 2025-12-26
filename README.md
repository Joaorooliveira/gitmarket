# GitMarket API 🛒

![Badge de Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Badge de Licença](https://img.shields.io/badge/license-MIT-blue)
![Java](https://img.shields.io/badge/java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/spring--boot-3-green)

Uma API RESTful robusta para E-commerce, desenvolvida em Java e Spring Boot. O **GitMarket** gerencia todo o ecossistema
de vendas, desde o cadastro de produtos e categorias até a autenticação segura de usuários.

Este projeto demonstra a aplicação de arquitetura profissional, boas práticas de código e ferramentas modernas do
mercado.

## 🚀 Funcionalidades Atuais

* **Autenticação e Segurança:**
    * Login e Registro de usuários.
    * Proteção de rotas com **Spring Security** e Tokens **JWT** (JSON Web Token).
* **Gestão de Produtos:**
    * CRUD completo (Create, Read, Update, Delete).
    * Validações de regras de negócio (ex: proibição de redução drástica de preço).
    * Paginação e ordenação de listagens.
* **Gestão de Categorias:**
    * Organização de produtos por departamentos.
    * Soft Delete (exclusão lógica) para manter integridade dos dados.
* **Documentação:**
    * API documentada automaticamente com **Swagger/OpenAPI**.
* **Banco de Dados:**
    * Versionamento de banco de dados com **Flyway**.
    * Suporte a **PostgreSQL** (Produção/Dev) e H2 (Testes).

## 🚧 Roadmap (Em Desenvolvimento)

O projeto está em constante evolução. Próximas funcionalidades planejadas:

- [ ] **Carrinho de Compras:** Adicionar/remover itens e calcular totais.
- [ ] **Cupons de Desconto:** Lógica promocional e validação de cupons.
- [ ] **Checkout e Pagamento:** Integração com gateways de pagamento.
- [ ] **Gestão de Pedidos:** Histórico e status de entrega.

## 🛠️ Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot 3**
* **Spring Security & JWT**: Para autenticação stateless.
* **Spring Data JPA**: Camada de persistência.
* **Flyway**: Migrations e versionamento de banco de dados.
* **PostgreSQL**: Banco de dados relacional.
* **Swagger (OpenAPI)**: Documentação interativa.
* **Bean Validation**: Validação de DTOs.
* **Lombok**: Redução de código boilerplate.

## 🚀 Como Rodar o Projeto

### Pré-requisitos

* Java 17 ou superior.
* Maven instalado.
* PostgreSQL (Opcional, caso queira rodar com banco real).

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/Joaorooliveira/gitmarket.git
   ```

2. **Navegue até o diretório:**
   ```bash
   cd gitmarket
   ```

3. **Configuração do Banco de Dados:**
    * O projeto está configurado para conectar no PostgreSQL.
    * Certifique-se de configurar a variável de ambiente `DB_PASSWORD` ou ajustar o arquivo `application.properties` com
      suas credenciais locais.

4. **Execute a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Acesse a Documentação (Swagger):**
   Com a aplicação rodando, acesse:
   👉 `http://localhost:8080/swagger-ui.html`

## 📖 Endpoints Principais

| Recurso        | Método | Rota                  | Descrição                              |
|:---------------|:-------|:----------------------|:---------------------------------------|
| **Auth**       | `POST` | `/api/auth/login`     | Autentica usuário e retorna Token JWT. |
| **Auth**       | `POST` | `/api/auth/registrar` | Cria um novo usuário no sistema.       |
| **Produtos**   | `GET`  | `/api/produtos`       | Lista produtos (paginado).             |
| **Produtos**   | `POST` | `/api/produtos`       | Cria novo produto (Requer Token).      |
| **Categorias** | `GET`  | `/api/categorias`     | Lista categorias disponíveis.          |

---

### Exemplo de JSON (Criar Produto)

```json
{
  "nome": "Notebook Gamer",
  "preco": 4500.00,
  "quantidade": 10,
  "categoriaId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
}
```

### 📂 Estrutura do Projeto

#### A arquitetura segue o padrão de camadas (Layered Architecture) focada no domínio:

```plantuml
src/main/java/com/product/api/gitmarket
├── domain # Regras de negócio e Entidades
│ ├── categoria # Módulo de Categorias
│ ├── produto # Módulo de Produtos (com validações)
│ └── usuario # Módulo de Usuários
├── infra # Configurações transversais
│ ├── exception # Tratamento global de erros
│ ├── security # Configurações de Segurança e Token JWT
│ └── springdoc # Configuração do Swagger
└── GitMarketApplication.java

```
