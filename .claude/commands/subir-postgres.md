# Subir PostgreSQL (pgvector-rag)

Gerencia o container Docker do PostgreSQL com extensão pgvector usado pelo RAG.

## Localização
```
g:\SUPORTES\postgres-compose\docker-compose.yml
```

## Comandos

```bash
# Subir
cd g:\SUPORTES\postgres-compose
docker compose up -d

# Status
docker ps --filter "name=pgvector-rag"

# Parar
docker compose down

# Logs
docker logs pgvector-rag --tail 50
```

## Configuração
| Parâmetro | Valor |
|-----------|-------|
| Container | `pgvector-rag` |
| Imagem    | `pgvector/pgvector:pg16` |
| Porta     | `5436` → `5432` interno |
| Banco     | `ragdb` |
| Usuário   | `postgres` |
| Senha     | `postgres` |
| Volume    | `postgres-compose_pgvector_data` |
| pgvector  | `0.8.2` |

## Conectar via psql
```bash
docker exec -it pgvector-rag psql -U postgres -d ragdb
```

## Queries úteis de diagnóstico
```sql
-- Quantidade de tabelas indexadas
SELECT COUNT(*) FROM tabela;

-- Tabelas sem colunas (possível problema de extração)
SELECT t.nome FROM tabela t
LEFT JOIN coluna c ON c.tabela_id = t.id
WHERE c.id IS NULL;

-- Documentos no vector store
SELECT COUNT(*) FROM vector_store;

-- Truncar vector store para reindexar do zero
TRUNCATE vector_store;
```
