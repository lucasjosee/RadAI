# RadAI

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spark](https://img.shields.io/badge/Spark_Framework-2.x-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?logo=postgresql&logoColor=white)
![Azure](https://img.shields.io/badge/Azure_Custom_Vision-IA-0078D4?logo=microsoftazure&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)
![License](https://img.shields.io/badge/License-CC--BY--4.0-lightgrey)

> Sistema web para análise automatizada de fraturas ósseas em radiografias, utilizando Inteligência Artificial via Azure Custom Vision.

---

## Sobre o Projeto

Fraturas ósseas muitas vezes passam despercebidas em radiografias quando analisadas manualmente sob pressão de tempo. O **RadAI** automatiza essa triagem: o médico envia a imagem do raio-X e o sistema retorna instantaneamente se há indício de fratura, o nível de confiança da análise e se o caso precisa de revisão humana.

Este projeto foi desenvolvido como trabalho acadêmico na **PUC Minas** (2022), nas disciplinas orientadas pelas professoras Luciana Mara Freitas Diniz e Ilo Amy Saldanha Rivero.

---

## Funcionalidades

- ✅ Autenticação de usuários com senha criptografada (BCrypt)
- ✅ Cadastro e listagem de pacientes
- ✅ Agendamento e listagem de consultas
- ✅ Upload de imagem de raio-X diretamente pelo portal médico
- ✅ Análise automática de fraturas via Azure Custom Vision
- ✅ Resultado com diagnóstico, confiança e flag de revisão humana
- ✅ Deploy via Docker com suporte a variáveis de ambiente

---

## Arquitetura

```
┌─────────────────────────────────────────────────┐
│                   NAVEGADOR                      │
│   login.html │ dashboard.html │ medico.html      │
└──────────────────────┬──────────────────────────┘
                       │ HTTP/REST  :8080
┌──────────────────────▼──────────────────────────┐
│            BACKEND  (Java 21 + Spark)            │
│  /login │ /pacientes │ /consultas │ /upload-raiox│
└──────────────┬────────────────────┬─────────────┘
               │                    │
  ┌────────────▼──────┐  ┌──────────▼──────────────┐
  │    PostgreSQL     │  │   Azure Custom Vision    │
  │  (dados clínicos) │  │   (análise de raio-X)    │
  └───────────────────┘  └─────────────────────────┘
```

---

## Tecnologias

| Tecnologia | Versão | Papel |
|---|---|---|
| Java | 21 | Linguagem do backend |
| Spark Framework | 2.x | Servidor HTTP embarcado |
| PostgreSQL | 15 | Banco de dados relacional |
| Sql2o | — | Mapeamento SQL → objetos Java |
| BCrypt | — | Hash de senhas |
| Gson | — | Serialização JSON |
| Azure Custom Vision | API v3 | Modelo de IA para detecção de fraturas |
| Apache HttpClient | — | Chamadas HTTP para a Azure |
| Bootstrap | 5.3 | Estilização do frontend |
| Docker | — | Containerização e deploy |

---

## Pré-requisitos

Para rodar com Docker (recomendado):
- [Docker](https://www.docker.com/get-started) instalado
- Banco PostgreSQL acessível (local ou na nuvem)
- Chaves do Azure Custom Vision (opcional — sem elas a análise de IA retorna erro)

Para rodar manualmente:
- Java 21
- Maven 3.9+
- PostgreSQL 15

---

## Como Rodar

### Via Docker (recomendado)

```bash
# 1. Clone o repositório
git clone https://github.com/lucasjosee/RadAI.git
cd RadAI

# 2. Configure as variáveis de ambiente
cp .env.example .env
# Edite .env com suas credenciais

# 3. Build e run
cd Codigo/backend
docker build -t radai .
docker run -p 8080:8080 --env-file ../../.env radai
```

Acesse: http://localhost:8080/login.html

---

### Via Maven (manual)

```bash
# 1. Clone e entre na pasta do backend
git clone https://github.com/lucasjosee/RadAI.git
cd RadAI/Codigo/backend

# 2. Configure as variáveis de ambiente
export DB_URL=jdbc:postgresql://localhost:5432/saas_raiox
export DB_USER=postgres
export DB_PASS=sua_senha
export AZURE_URL=https://seu-endpoint...
export AZURE_KEY=sua_chave

# 3. Build e execute
mvn clean package -DskipTests
java -jar target/saas-raiox-0.0.1-SNAPSHOT.jar
```

Acesse: http://localhost:8080/login.html

---

## Variáveis de Ambiente

| Variável | Obrigatória | Padrão | Descrição |
|---|---|---|---|
| `PORT` | Não | `8080` | Porta do servidor |
| `DB_URL` | Sim | `jdbc:postgresql://localhost:5432/saas_raiox` | JDBC URL do PostgreSQL |
| `DB_USER` | Sim | `postgres` | Usuário do banco |
| `DB_PASS` | Sim | — | Senha do banco |
| `AZURE_URL` | Sim* | — | Endpoint do modelo Custom Vision |
| `AZURE_KEY` | Sim* | — | Chave de predição da Azure |

*Sem as chaves Azure, o upload de raio-X retornará erro de IA.

---

## Endpoints da API

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/login` | Autentica usuário (email + senha) |
| `POST` | `/usuarios` | Cadastra novo usuário |
| `GET` | `/pacientes` | Lista todos os pacientes |
| `POST` | `/pacientes` | Cadastra novo paciente |
| `GET` | `/consultas-lista` | Lista todas as consultas |
| `POST` | `/consultas` | Agenda nova consulta |
| `POST` | `/upload-raiox` | Envia imagem e retorna análise de IA |

---

## Estrutura do Projeto

```
RadAI/
├── .env.example              # Template de variáveis de ambiente
├── CITATION.cff              # Metadados para citação acadêmica
├── LICENSE                   # Creative Commons BY 4.0
│
├── Codigo/
│   ├── backend/
│   │   ├── Dockerfile        # Build e deploy via Docker
│   │   ├── public/           # Frontend servido pelo Spark
│   │   │   ├── login.html
│   │   │   ├── dashboard.html
│   │   │   ├── medico.html
│   │   │   ├── cadastro.html
│   │   │   └── style.css
│   │   └── src/main/java/
│   │       ├── Main.java         # Servidor + rotas REST
│   │       ├── dao/              # Acesso ao banco de dados
│   │       ├── model/            # Entidades (Usuario, Paciente, Consulta)
│   │       └── service/
│   │           └── ServicoIA.java  # Integração com Azure Custom Vision
│   └── front/                # Cópia standalone do frontend
│
├── Divulgacao/
│   └── Apresentacao/         # PDFs das apresentações por Sprint
│
└── docs/                     # Documentação do projeto
```

---

## Apresentações

| Sprint | Arquivo |
|---|---|
| Sprint 1 | [Sprint 1 - RadiIA.pdf](Divulgacao/Apresentacao/Sprint%201%20-%20RadiIA.pdf) |
| Sprint 2 | [Sprint 2.pdf](Divulgacao/Apresentacao/Sprint%202.pdf) |

---

## Autores e Orientadores

**Aluno**
- Lucas José Souza Rodrigues

**Orientadores — PUC Minas**
- Profª Luciana Mara Freitas Diniz
- Prof. Ilo Amy Saldanha Rivero

---

## Licença

Distribuído sob a licença [Creative Commons Attribution 4.0 International (CC BY 4.0)](LICENSE).

Você pode usar, adaptar e distribuir livremente, desde que cite os autores originais.
