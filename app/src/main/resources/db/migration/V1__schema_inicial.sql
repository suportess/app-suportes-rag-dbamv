-- Extensão pgvector (necessária para o VectorStore do Spring AI)
CREATE EXTENSION IF NOT EXISTS vector;

-- Tabela de metadados das tabelas Oracle
CREATE TABLE tabela (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    nome        VARCHAR(128) NOT NULL,
    schema_ora  VARCHAR(64)  NOT NULL DEFAULT 'DBAMV',
    descricao   TEXT,
    indexado_em TIMESTAMP,
    criado_em   TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_tabela_nome_schema UNIQUE (nome, schema_ora)
);

-- Colunas de cada tabela
CREATE TABLE coluna (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    tabela_id       UUID        NOT NULL REFERENCES tabela(id) ON DELETE CASCADE,
    nome            VARCHAR(128) NOT NULL,
    tipo_dado       VARCHAR(64),
    descricao       TEXT,
    nullable        BOOLEAN     NOT NULL DEFAULT TRUE,
    chave_primaria  BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_coluna_tabela_nome UNIQUE (tabela_id, nome)
);

-- Relacionamentos entre tabelas (FKs / JOINs semânticos)
CREATE TABLE relacionamento (
    id                  UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    tabela_origem_id    UUID        NOT NULL REFERENCES tabela(id) ON DELETE CASCADE,
    coluna_origem       VARCHAR(128) NOT NULL,
    tabela_destino_id   UUID        NOT NULL REFERENCES tabela(id) ON DELETE CASCADE,
    coluna_destino      VARCHAR(128) NOT NULL,
    descricao           TEXT
);

-- Exemplos de perguntas naturais + SQL correspondente
CREATE TABLE exemplo (
    id          UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    pergunta    TEXT    NOT NULL,
    sql_gerado  TEXT    NOT NULL,
    descricao   TEXT,
    criado_em   TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Tabela gerenciada pelo Spring AI PgVectorStore
-- (Spring AI cria automaticamente com initialize-schema=true;
--  como usamos false, criamos aqui com as colunas esperadas)
CREATE TABLE IF NOT EXISTS vector_store (
    id        UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    content   TEXT,
    metadata  JSON,
    embedding vector(1536)
);

CREATE INDEX IF NOT EXISTS idx_vector_store_embedding
    ON vector_store USING hnsw (embedding vector_cosine_ops);
