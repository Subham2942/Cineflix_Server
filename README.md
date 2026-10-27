# Cineflix Server

Spring Boot API for Cineflix semantic movie search. It uses OpenAI embeddings
and Pinecone vector search.

## Local configuration

Create an uncommitted `.env` file in the repository root:

```properties
OPENAI_API_KEY=your_openai_key
EMBEDDING_MODEL=your_existing_index_embedding_model
PINECONE_API_KEY=your_pinecone_key
PINECONE_INDEX_NAME=your_pinecone_index
CORS_ALLOWED_ORIGINS=http://localhost:5173
INDEXING_ENABLED=true
```

Run the API with `./mvnw spring-boot:run`. The public application endpoints are:

- `GET /health`
- `GET /movies`
- `GET /movies/search?userQuery=...`

`POST /movies/index` exists only when `INDEXING_ENABLED=true`. Keep it disabled
on a public deployment because it creates embeddings and can incur API costs.

## Render deployment

`render.yaml` defines a free Render web service for this API and a free static
site for `Cineflix_frontend`. During Blueprint creation, Render prompts for the
four provider/index values listed above. The Blueprint connects the two public
URLs automatically through `CORS_ALLOWED_ORIGINS` and `VITE_API_URL`.

The free API instance sleeps after periods of inactivity, so the first search
after a sleep can take about a minute while it starts again.
