# demo_fcg_usr_mgmt

## Requisiti

- un'applicazione basata su Java 
- deve esporre gli endpoint di CRUD sull'entità User (con nome, cognome, mail e indirizzo per esempio)
- deve essere esposta una funzionalità di ricerca degli utenti, filtrando (opzionalmente) anche per nome, cognome o entrambi
- Uso di un DB a piacere
- Creazione di una immagine Docker del progetto e utilizzo di Docker per il DB
- Gestione del progetto con Git
- Esporre una API che prenda in input un file csv contenente le informazioni sugli User sopra descritti e li memorizzi nel DB

**REGOLE**

- Il progetto può essere consegnato anche se non completo.
- Il progetto va consegnato entro una settimana dalla richiesta.
- Il lavoro finito sarà poi discusso in un colloquio dedicato.

## Prerequisites
- JDK 17 (JAVA_HOME and PATH env variables set accordingly)
- Maven
- Docker

## Build & Run
- `mvn package`
- `docker compose up fcg_usr_mgmt_app`

## Usage
CRUD REST operations are supported, just point your favourite tool to: `http://localhost:8881/api/users`

Tip: you can find a Postman collection to start with under `src/test/resources`.

**Search**

To perform a search use  `http://localhost:8881/api/users/search`
with at least one of the following query parameters:

- id (same as /api/users/{id})
- firstname
- lastname

Example: `http://localhost:8881/api/users/search?lastname=Lametta`

**Upload CSV**

There is a CSV example file in the same folder.
You can use the UploadCSV request in the Postman collection; in any case you just need to POST the file to `http://localhost:8881/api/users/upload-csv`
with a multipart/form-data content-type and a form parameter of type `file` and name `file`

