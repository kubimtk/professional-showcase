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

## Postman

Fuer manuelle API-Tests liegen vorbereitete Postman-Dateien im Ordner [`../postman/`](../postman/):

- Collection: `appointment-backend-showcase.postman_collection.json`
- Environment: `local.postman_environment.json`

Empfohlene Reihenfolge in Postman:

1. `List appointments`
2. `Create appointment`
3. `Get appointment by id`
4. `Cancel appointment`

Die Collection speichert die erzeugte `appointmentId` automatisch und berechnet fuer den Create-Request ein gueltiges naechstes Werktags-Zeitfenster.

## Schneller Shell-Test mit curl

Fuer einen schnellen End-to-End-Test ohne Postman gibt es das Skript [`scripts/quick-test.sh`](scripts/quick-test.sh):

```bash
./scripts/quick-test.sh
```

Optional mit anderem Zielsystem:

```bash
BASE_URL=http://localhost:8080 ./scripts/quick-test.sh
```

Das Skript legt einen Termin an, liest ihn wieder aus, storniert ihn und zeigt anschliessend die Terminliste an.
Falls lokal noch kein Backend laeuft, baut das Skript automatisch das JAR, startet das Backend daraus, wartet auf die API und beendet den gestarteten Prozess am Ende wieder sauber.

## Docker

```bash
docker build -t appointment-backend-showcase .
docker run --rm -p 8080:8080 appointment-backend-showcase
```
