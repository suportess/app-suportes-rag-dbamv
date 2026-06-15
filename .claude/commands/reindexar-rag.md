# Reindexar RAG (gerar embeddings)

Dispara a geração de embeddings OpenAI para todas as tabelas/colunas/relacionamentos
cadastrados no PostgreSQL e os insere no pgvector.

## Quando usar
- Após aplicar uma nova migration (V3, V4…) com tabelas novas
- Após cadastrar tabelas/exemplos manualmente via API
- Após alterar descrições de tabelas ou colunas

## Pré-requisitos

1. Container pgvector rodando:
```bash
cd g:\SUPORTES\postgres-compose
docker compose ps
# se não estiver up:
docker compose up -d
```

2. Aplicação Spring Boot rodando em `http://localhost:8080`

3. Variável de ambiente `OPENAI_API_KEY` definida

## Executar

```bash
curl -X POST http://localhost:8080/api/indexacao
```

Ou use o request **"Indexar tudo"** na collection do Insomnia em:
```
g:\SUPORTES\app-suportes-rag-dbamv\doc\insomnia-collection.json
```

## Resposta esperada
```json
{ "documentos_indexados": 1058 }
```

## Observações
- Cada chamada **adiciona** documentos ao vector store (não apaga os anteriores)
- Se precisar reindexar do zero: truncar a tabela `vector_store` no PostgreSQL antes
```sql
TRUNCATE vector_store;
```
- Custo OpenAI: modelo `text-embedding-3-small`, ~1.000 tokens por tabela
