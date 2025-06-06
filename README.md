
# 📦 API de Controle de Produtos em Estoque

## 📋 Descrição
Esta API permite o cadastro e gerenciamento de produtos em estoque. Cada produto possui nome, descrição, quantidade, preço e um **histórico de movimentações**, possibilitando o controle preciso de entradas e saídas do estoque.

## 🛠️ Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Bean Validation** – Validação de dados de entrada
- **Exception Handler** – Tratamento global de erros
- **Testcontainers** – Testes de integração com banco real
- **Docker Compose** – Orquestração da aplicação e banco de dados

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos
- Java 17
- Docker e Docker Compose
- Git

### 📥 Clonar o Repositório
```bash
git clone https://github.com/seu-usuario/nome-do-repo.git
cd nome-do-repo
```

### 🐳 Executar com Docker Compose
```bash
docker-compose up --build
```
A API estará disponível em: `http://localhost:8080`

## 📡 Endpoints Principais
| Método | Endpoint                  | Descrição                           |
|--------|---------------------------|-------------------------------------|
| GET    | `/produtos/listar`        | Lista todos os produtos             |
| GET    | `/produtos/{id}`          | Busca produto por ID                |
| POST   | `/produtos/criar`         | Cadastra um novo produto            |
| PUT    | `/produtos/{id}`          | Atualiza os dados de um produto     |
| DELETE | `/produtos/{id}`          | Remove um produto                   |
| GET    | `/movimentacoes/listar`   | Lista histórico de movimentações    |

A documentação completa pode ser acessada via Swagger: `http://localhost:8080/swagger-ui.html` (caso habilitado)

## 🧪 Testes
O projeto utiliza **Testcontainers** para garantir testes realistas com banco de dados PostgreSQL isolado.

Para executar os testes:
```bash
./gradlew test
```

## 🐳 Docker

### 📁 Estrutura
- `Dockerfile`: Build da aplicação Spring Boot
- `docker-compose.yml`: Serviços da aplicação e banco de dados

## 🙋‍♂️ Autor
Fernando Pereira
Email: `contatofernando2021@gmail.com`
