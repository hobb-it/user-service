# User-service

## Run the db

```bash
docker run --name hobbit-db -e POSTGRES_DB=user-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -p 5432:5432 --network host -d postgres
```

```bash
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' user-db
```