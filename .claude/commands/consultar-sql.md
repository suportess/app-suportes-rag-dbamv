# Consultar SQL via RAG

Gera um SQL Oracle a partir de uma pergunta em linguagem natural usando o pipeline RAG.

## Como funciona

1. A pergunta é convertida em embedding (OpenAI `text-embedding-3-small`)
2. Busca por similaridade no pgvector (`topK` documentos mais próximos)
3. Nomes de tabelas detectados na pergunta e nos JOINs são buscados diretamente no catálogo
4. O contexto expandido (tabelas + colunas reais) é enviado ao LLM (`gpt-4o`)
5. O SQL retornado vem limpo, sem markdown, pronto para executar no Oracle

## Endpoint

```
POST http://localhost:8080/api/consulta
Content-Type: application/json

{
  "pergunta": "Sua pergunta aqui",
  "topK": 5
}
```

## Dicas para boas perguntas

- **Cite os nomes das tabelas** quando souber (ex: "na tabela EST_CONSIG_FORN")
- **Descreva os joins necessários** (ex: "fazendo join com FORNECEDOR para trazer o nome")
- **Filtre explicitamente** (ex: "apenas onde saldo > 0")
- Aumente `topK` para 8-10 quando a pergunta envolver muitas tabelas

## Exemplos prontos na collection Insomnia
Arquivo: `g:\SUPORTES\app-suportes-rag-dbamv\doc\insomnia-collection.json`
- Saldo consignado por fornecedor
- Entradas por produto com lote e validade
- Devoluções consolidadas por fornecedor

## Regras que o LLM segue
- Usa SOMENTE colunas listadas no contexto — nunca inventa
- Qualifica sempre com schema: `DBAMV.<TABELA>`
- Se uma tabela pedida não estiver no contexto, informa que os metadados não estão disponíveis
