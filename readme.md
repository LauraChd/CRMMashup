./gradlew build
./gradlew :virtualcrm:build
./gradlew :client:build
./gradlew :internalcrm:build
./gradlew :marger:build
Exécution
Option 1 : Lancer l'application Spring Boot (virtualcrm) uniquement :
./gradlew :virtualcrm:bootRun

Option 2 : Lancer l'environnement complet avec Gretty Farm :
./gradlew farmRun
Cette commande lance plusieurs webapps (notamment internalcrm) selon la configuration dans build.gradle. Le port par défaut est 8080 (configurable via httpPort).
Option 3 : Créer un JAR exécutable pour le client :
./gradlew :client:shadowJar
Puis exécuter :
java -jar client/build/libs/client-<version>-all.jar



1. Démarrer InternalCRM (déjà fait ✅)
   ```bash
   ./gradlew :internalcrm:appStart
   ```

2. Démarrer VirtualCRM
   ```bash
   ./gradlew :virtualcrm:bootRun
   ```

http://localhost:8080/findLeads?lowAnnualRevenue=0&highAnnualRevenue=1000000&state=Île-de-France

./gradlew :client:shadowJar
java -jar client/build/libs/client-all.jar findLeads 0 1000000 "Île-de-France"
http://localhost:8080/leads/1
