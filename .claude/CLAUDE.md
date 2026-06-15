# app-suportes-rag-dbamv

RAG sobre o schema DBAMV — catálogo semântico de tabelas + geração de SQL Oracle via LLM.

## Stack
- Java 21 / Spring Boot 3.4.5 / Spring AI 1.0.0 / Lombok / Flyway / pgvector

## Conexões
| Banco | URL | Credenciais |
|-------|-----|-------------|
| PostgreSQL (RAG) | `localhost:5436/ragdb` | `postgres/postgres` |
| Oracle (fonte) | `localhost:1522/XEPDB1` | `dbamv/Oracle123` |

Container: `pgvector-rag` — compose em `g:\SUPORTES\postgres-compose\`

## Flyway migrations
| Versão | Conteúdo |
|--------|----------|
| V1 | Schema: `tabela`, `coluna`, `relacionamento`, `exemplo`, `vector_store` |
| V2 | 1.006 tabelas DBAMV (`oracle_extract_metadata.py`) |
| V3 | Delta: `FORNECEDOR`, família `FORN_*` / `EMPRESA_*` (52 tabelas) |

Próxima migration delta: **V4**.

## Artefatos Spring AI 1.0.0
| API | Uso correto |
|-----|-------------|
| Starter OpenAI | `spring-ai-starter-model-openai` |
| Starter pgvector | `spring-ai-starter-vector-store-pgvector` |
| SearchRequest | `SearchRequest.builder().query(q).topK(n).build()` |
| Document content | `document.getText()` (não `getContent()`) |

## Fluxo operacional
1. `docker compose up -d` em `postgres-compose/`
2. Subir Spring Boot → Flyway aplica V1/V2/V3 automaticamente
3. `POST /api/indexacao` → gera embeddings e popula pgvector
4. `POST /api/consulta` → pergunta em PT → SQL Oracle executável

## Expansão de contexto (ConsultaService)
Após o vector search, o serviço extrai nomes de tabelas dos docs e da pergunta e
faz `findByNomeIgnoreCase` direto no repositório — garante que tabelas referenciadas
por FK (ex: `FORNECEDOR`) entrem no prompt com colunas reais, sem alucinação.

## Scripts Python
| Script | Função |
|--------|--------|
| `oracle_extract_metadata.py` | Gera V2 completo (filtra por prefixo + comentário) |
| `generate_v3_delta.py` | Gera migration delta só com tabelas ausentes no PG |

Para adicionar prefixos: editar `BUSINESS_PREFIXES` em `oracle_extract_metadata.py`.

## Commands disponíveis
- `/extrair-oracle` — gera migration com metadados novos do Oracle
- `/reindexar-rag` — dispara `POST /api/indexacao`
- `/consultar-sql` — guia para usar o endpoint de consulta
- `/subir-postgres` — gerencia o container pgvector-rag
