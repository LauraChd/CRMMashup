
# Instructions pour lancer les services

1. Démarrer InternalCRM
   ```bash
   ./gradlew :internalcrm:appStart
   ```

2. Démarrer VirtualCRM
   ```bash
   ./gradlew :virtualcrm:bootRun
   ```
3. Exécuter des commandes 
   ```bash
    ./gradlew :client:shadowJar
    ```
   ```bash
    java -jar client/build/libs/client-all.jar [commande] [params]
    ```
Remplacer [commande] par le nom de la commande à exécuter et [params] par les éventuels paramètres nécessaires à son exécution

Une interface graphique a été ajoutée à cette adresse : http://localhost:8080/
Elle permet d'exécuter les mêmes requêtes qu'en ligne de commandes.