# demo_fcg_usr_mgmt

## Requisiti

- un'applicazione basata su Java 
- deve esporre gli endpoint di CRUD sull'entità User (con nome, cognome, mail e indirizzo per esempio)
- deve essere esposta una funzionalità di ricerca degli utenti, filtrando (opzionalmente) anche per nome, cognome o entrambi
- Uso di un DB a piacere
- Creazione di una immagine Docker del progetto e utilizzo di Docker per il DB
- Gestione del progetto con Git
- Esporre una API che prenda in input un file csv contenente le informazioni sugli User sopra descritti e li memorizzi nel DB

## REGOLE

- Il progetto può essere consegnato anche se non completo.
- Il progetto va consegnato entro una settimana dalla richiesta.
- Il lavoro finito sarà poi discusso in un colloquio dedicato.

## Build & Run
- `mvn package`
- `docker compose up fcg_usr_mgmt_app `

## Usage
Tip: you can find a Postman collection to start with under `src/test/resources`.
Point your favourite tool to: `http://localhost:8881/api/users`
CRUD REST operations are supported.

To perform a search use  `http://localhost:8881/api/users/search`
with at least one of the following query parameters:

- id (same as /api/users/{id})
- firstname
- lastname

Example: `http://localhost:8881/api/users/search?lastname=Capatonda`
