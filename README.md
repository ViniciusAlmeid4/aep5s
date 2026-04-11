# AEP-5S-1B

Projeto acadêmico desenvolvido para a AEP do 5º semestre na Unicesumar. Aplicação Java pura com persistência em banco de dados relacional H2.

---

## Tecnologias

| Tecnologia | Versão | Função |
|---|---|---|
| Java | 24 | Linguagem principal |
| Maven | 3.x | Gerenciamento de build e dependências |
| H2 Database | 2.2.224 | Banco de dados relacional embarcado |

---

## Arquitetura do projeto

```
aep5s/
├── pom.xml                             # Configuração Maven (dependências e compilador)
└── src/
    └── main/
        └── java/
            └── org/cesumar/
                ├── Main.java           # Ponto de entrada da aplicação
                ├── model/              # Entidades / classes de domínio
                ├── dao/                # Camada de acesso a dados (CRUD via JDBC)
                └── db/                 # Configuração e conexão com o banco H2
```

### Responsabilidade de cada camada

- **`Main.java`** — inicializa a aplicação, configura o banco e executa o seed de dados.
- **`model/`** — classes que representam as entidades do sistema (ex: `Aluno.java`). Mapeiam as tabelas do banco.
- **`dao/`** — padrão DAO (Data Access Object). Cada classe é responsável pelas operações de Create, Read, Update e Delete de uma entidade usando JDBC.
- **`db/`** — classe de conexão com o banco H2. Fornece a `Connection` para as classes DAO.

---

## Como o banco H2 funciona aqui

O H2 é um banco relacional que roda **dentro da própria JVM**, sem necessidade de instalar nenhum servidor externo, o banco é criado do zero toda vez que a aplicação sobe e destruído quando ela encerra.

Por isso, o projeto usa um **seed**: um conjunto de instruções SQL executadas na inicialização para criar as tabelas e popular os dados iniciais.

---

## Pré-requisitos

- [Java 24+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)

---

## Como rodar

**1. Clone o repositório**

```bash
git clone https://github.com/ViniciusAlmeid4/aep5s.git
cd aep5s
```

**2. Compile o projeto**

```bash
mvn compile
```

**3. Execute a aplicação**

```bash
mvn exec:java -Dexec.mainClass="org.cesumar.Main"
```

> Pela IDE (IntelliJ IDEA ou Eclipse): abra como projeto Maven e execute a classe `Main.java` diretamente.

---

## Seed do banco

O seed é executado automaticamente ao iniciar a aplicação. Ele é responsável por:

1. Criar as tabelas via `CREATE TABLE IF NOT EXISTS`
2. Inserir os dados iniciais via `INSERT INTO`

Exemplo de estrutura do seed:

```java
String createTable = "CREATE TABLE IF NOT EXISTS aluno ("
    + "id INT PRIMARY KEY AUTO_INCREMENT, "
    + "nome VARCHAR(100) NOT NULL, "
    + "matricula VARCHAR(20) UNIQUE NOT NULL"
    + ")";

String insertData = "INSERT INTO aluno (nome, matricula) VALUES "
    + "('João Silva', '2024001'), "
    + "('Maria Souza', '2024002')";
```

---

## Autor

Desenvolvido por **Vinicius Almeida** — Unicesumar, 5º semestre.
