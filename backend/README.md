# Appointment Backend Showcase

Kleines Java/Spring-Boot-Backend als recruiter-freundlicher Kompetenzbeweis fuer Backend Engineering, API-Design und wartbare Business-Logik.

## Enthaltene Nachweise

- REST-Endpunkte fuer Anlegen, Abrufen, Auflisten und Stornieren von Terminen
- Request-Validierung mit strukturierten Fehlerantworten
- Business-Regeln fuer Zeitfenster, Dauer und Ueberschneidungen
- OpenAPI/Swagger unter `/swagger-ui.html`
- Unit- und Integrationstests
- Dockerfile fuer reproduzierbaren Start

## Lokal starten

```bash
mvn spring-boot:run
```

Danach erreichbar unter:

- API: `http://localhost:8080/api/appointments`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Tests

```bash
mvn test
```

## Docker

```bash
docker build -t appointment-backend-showcase .
docker run --rm -p 8080:8080 appointment-backend-showcase
```
