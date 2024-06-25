# OpenAI Service API

Questo progetto è un esempio di servizio API che utilizza OpenAI per rispondere a richieste AI. È stato sviluppato utilizzando Spring Boot e Java 21. Il progetto include anche integrazioni per monitorare le metriche utilizzando Micrometer.

## Requisiti

- Java 21
- Maven
- Una chiave API di OpenAI (OpenAI API Key)

## Configurazione

### Variabili di ambiente

Per eseguire questo progetto, è necessario configurare una variabile di ambiente per la chiave API di OpenAI. Segui questi passaggi per configurare la variabile di ambiente in IntelliJ IDEA:
La chiave la puoi trovare tramite la pagina : https://platform.openai.com/api-keys

1. **Apri le configurazioni di esecuzione**:
   - Vai su **Run** > **Edit Configurations...**.

2. **Aggiungi la variabile di ambiente**:
   - Seleziona la configurazione della tua applicazione (es. Spring Boot Application).
   - Nella sezione **Environment** (Ambiente), trova il campo **Environment variables** (Variabili di ambiente).
   - Clicca sull'icona della cartella a destra del campo.
   - Aggiungi la tua variabile segreta nel formato `KEY=VALUE`. Ad esempio:
     ```
     OPENAI_API_KEY=la_tua_chiave_api
     ```

### application.properties

Assicurati che il file `application.properties` contenga la configurazione per leggere la chiave API dalla variabile di ambiente:

```properties
openai.api.key=${OPENAI_API_KEY}
