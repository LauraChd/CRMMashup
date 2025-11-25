# Test de communication Thrift entre InternalCRM et VirtualCRM

## Configuration

- **InternalCRM** : Port 9090, URL Thrift : `http://localhost:9090/`
- **VirtualCRM** : Port 8080 (Spring Boot)
- **Client** : Communique avec VirtualCRM via REST

## Étapes pour tester

### 1. Lancer InternalCRM (port 9090)

```bash
# Dans un terminal
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :internalcrm:appStart
```

Ou utiliser farmRun pour lancer tous les services :
```bash
./gradlew farmStart
```

### 2. Vérifier qu'InternalCRM répond

```bash
curl -v http://localhost:9090/
```

### 3. Lancer VirtualCRM (port 8080)

```bash
# Dans un autre terminal
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :virtualcrm:bootRun
```

### 4. Tester la communication Thrift

VirtualCRM devrait pouvoir appeler InternalCRM via Thrift. Vous pouvez tester via :
- Les endpoints REST de VirtualCRM
- Le client en ligne de commande

### 5. Tester avec le client

```bash
# Builder le client
./gradlew :client:shadowJar

# Exécuter le client
java -jar client/build/libs/client-*-all.jar getLeads
```

## Vérification du servlet Thrift

Le servlet Thrift est maintenant annoté avec `@WebServlet(urlPatterns = {"/"})` dans `ThrifInternalCRMServiceServlet.java`.

## URLs de test

- InternalCRM Thrift : `http://localhost:9090/`
- VirtualCRM REST : `http://localhost:8080/` (endpoints à vérifier)

