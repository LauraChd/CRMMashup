# 📖 Guide : Comment envoyer les bonnes données Thrift

## 🎯 Réponse rapide

**Vous n'avez pas besoin de le faire manuellement !** 

Quand vous utilisez **VirtualCRM**, il envoie automatiquement les bonnes données Thrift via `InternalCRMClient`.

## 🔍 Comment ça marche ?

### Ce que fait VirtualCRM automatiquement :

```java
// Dans InternalCRMClient.java
// 1. Création du transport HTTP avec support Thrift
TTransport transport = new THttpClient("http://localhost:9090/");
transport.open();  // ← Se connecte au serveur

// 2. Utilisation du protocole binaire Thrift
TProtocol protocol = new TBinaryProtocol(transport);  // ← Format Thrift

// 3. Création du client Thrift
InternalCRMService.Client client = new InternalCRMService.Client(protocol);

// 4. Appel d'une méthode (ex: countLeads())
int count = client.countLeads();  
// ← ICI : Le client encode automatiquement la requête en binaire Thrift
// ← Envoie via HTTP POST avec Content-Type: application/x-thrift
// ← Le serveur reçoit, décode et traite
// ← Renvoie la réponse encodée en Thrift
```

**Tout est automatique !** Vous n'avez rien à faire de spécial.

## ✅ Méthodes pour tester

### Option 1 : Via VirtualCRM (Recommandé)

1. **Démarrer InternalCRM** (port 9090)
   ```bash
   ./gradlew :internalcrm:appStart
   ```

2. **Démarrer VirtualCRM** (port 8080)
   ```bash
   ./gradlew :virtualcrm:bootRun
   ```

3. **VirtualCRM utilisera automatiquement `InternalCRMClient`** qui envoie les vraies requêtes Thrift !

### Option 2 : Via le client en ligne de commande

Le client appelle VirtualCRM (REST), qui appelle InternalCRM (Thrift) :

```bash
# Builder le client
./gradlew :client:shadowJar

# Exécuter
java -jar client/build/libs/client-*-all.jar getLeads
```

## ❌ Pourquoi curl ne fonctionne pas ?

`curl` envoie juste du HTTP simple :
```bash
curl http://localhost:9090/
# → GET / HTTP/1.1
# → Pas de données Thrift
# → Le servlet ne peut pas décoder → Erreur 500
```

**C'est normal !** curl n'a pas le support Thrift.

## 📝 Résumé

- ✅ **VirtualCRM** envoie automatiquement les bonnes données Thrift
- ✅ **Le client** peut aussi être utilisé (via VirtualCRM)
- ❌ **curl** ne peut pas le faire (pas de support Thrift)

**Action** : Une fois VirtualCRM démarré, tout fonctionnera automatiquement !

