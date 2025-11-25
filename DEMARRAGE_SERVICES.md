# 🚀 Guide de démarrage des services

## Étape 1 : Démarrer InternalCRM (port 9090)

```bash
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :internalcrm:appStart
```

**Attendez** que vous voyiez dans les logs :
- "Server started"
- Ou "Application context started"

**Tester** que le servlet répond :
```bash
curl http://localhost:9090/
# Vous devriez avoir une réponse (même une erreur Thrift est normale)
```

## Étape 2 : Démarrer VirtualCRM (port 8080)

**Dans un nouveau terminal** :
```bash
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :virtualcrm:bootRun
```

**Attendez** le démarrage complet de Spring Boot.

## Étape 3 : Tester la communication

VirtualCRM devrait maintenant pouvoir se connecter à InternalCRM via Thrift !

**Tester avec le client** :
```bash
# Builder le client
./gradlew :client:shadowJar

# Tester une commande
java -jar client/build/libs/client-*-all.jar getLeads
```

## ✅ Vérifications

- **Port 9090 ouvert** : `netstat -tlnp | grep 9090`
- **Port 8080 ouvert** : `netstat -tlnp | grep 8080`
- **Servlet accessible** : `curl http://localhost:9090/`

## ❌ En cas de problème

- Vérifier les logs de démarrage
- Vérifier que le web.xml est bien dans le WAR
- Vérifier que les ports ne sont pas déjà utilisés

