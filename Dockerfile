# Fase 1: Costruisci l'applicazione utilizzando un'immagine Maven con JDK 17
FROM maven:3.8.4-openjdk-17 as build

# Imposta la directory di lavoro nel container
WORKDIR /app

# Copia i file pom.xml e Maven per la gestione delle dipendenze
COPY pom.xml /app
COPY src /app/src

# Compila e pacchetta l'applicazione, saltando i test per accelerare la costruzione
RUN mvn clean package -DskipTests

# Fase 2: Prepara l'immagine di esecuzione con JDK 17
FROM openjdk:17-jdk

# Imposta la directory di lavoro
WORKDIR /app

# Copia il JAR compilato dalla fase di build
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

# Opzionale: se hai configurazioni esterne o file di properties che vuoi includere
COPY env.properties /app/

# Espone la porta su cui gira il microservizio
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "user-service.jar"]