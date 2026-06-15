# Extrair Metadados Oracle → PostgreSQL

Executa os scripts de extração de metadados do schema DBAMV e gera as migrations Flyway.

## Quando usar
- Tabelas novas precisam ser adicionadas ao catálogo RAG
- Prefixos novos foram adicionados ao filtro em `oracle_extract_metadata.py`
- V2 precisa ser regerado do zero

## Passo 1 — Tabelas completamente novas (regenerar V2)

Edite `BUSINESS_PREFIXES` em `oracle_extract_metadata.py` para incluir o novo prefixo, depois:

```bash
cd g:\SUPORTES\app-suportes-rag-dbamv
python oracle_extract_metadata.py
```

Saída: `app/src/main/resources/db/migration/V2__dados_dbamv.sql`

> Atenção: o Flyway não reaplicará V2 se já foi executado. Use o Passo 2 nesse caso.

## Passo 2 — Delta (tabelas adicionais sem recriar V2)

```bash
cd g:\SUPORTES\app-suportes-rag-dbamv
python generate_v3_delta.py
```

O script:
1. Conecta no PostgreSQL (`localhost:5436/ragdb`) e lista tabelas já existentes
2. Conecta no Oracle (`localhost:1522/XEPDB1/dbamv`) e busca tabelas candidatas
3. Gera apenas as tabelas **ausentes** no PostgreSQL
4. Saída: `app/src/main/resources/db/migration/V3__dados_dbamv_delta.sql`

Se já existir V3, incremente o número para V4, V5, etc.

## Passo 3 — Reiniciar app + reindexar

Após a migration ser aplicada pelo Flyway:
```
POST http://localhost:8080/api/indexacao
```

## Conexões
- Oracle: `localhost:1522` | `XEPDB1` | `dbamv/Oracle123` | Instant Client: `C:\oracle\instantclient_23_4`
- PostgreSQL: `localhost:5436` | `ragdb` | `postgres/postgres`
