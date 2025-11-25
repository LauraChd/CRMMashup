# 🔧 Comment envoyer les bonnes données Thrift

## ❓ Pourquoi curl ne fonctionne pas ?

Quand vous faites :
```bash
curl http://localhost:9090/
```

**Problème** : curl envoie une requête HTTP GET simple, mais le servlet Thrift attend :
- Des données binaires au format Thrift
- Un protocole spécifique (TBinaryProtocol)
- Une structure de message Thrift bien formée

## ✅ Solutions pour tester la communication Thrift

### Solution 1 : Utiliser VirtualCRM (Automatique)

Quand VirtualCRM démarre, il utilise `InternalCRMClient` qui envoie automatiquement les **vraies requêtes Thrift** :

```java
// Dans InternalCRMClient.java
TTransport transport = new THttpClient("http://localhost:9090/");
transport.open();  // ← Connexion HTTP avec support Thrift
TProtocol protocol = new TBinaryProtocol(transport);  // ← Format binaire Thrift
InternalCRMService.Client client = new InternalCRMService.Client(protocol);
client.countLeads();  // ← Envoie une vraie requête Thrift !
```

**C'est automatique** : VirtualCRM gère tout ça pour vous !

### Solution 2 : Créer un script de test Java

J'ai créé un fichier `TEST_THRIFT_CLIENT.java` pour tester directement.

Pour l'utiliser :

```bash
# 1. Compiler le test (il faut ajouter les dépendances)
cd /home/etud/IdeaProjects/CRMMashup

# 2. Créer un module de test ou utiliser le module client
```

### Solution 3 : Utiliser le module Client existant

Le module `client` peut aussi appeler VirtualCRM qui appellera InternalCRM.

## 🎯 Méthode recommandée : Démarrer VirtualCRM

VirtualCRM va **automatiquement** envoyer les bonnes données Thrift :

1. **Démarrer InternalCRM** (déjà fait ✅)
   ```bash
   ./gradlew :internalcrm:appStart
   ```

2. **Démarrer VirtualCRM**
   ```bash
   ./gradlew :virtualcrm:bootRun
   ```

3. **VirtualCRM utilisera InternalCRMClient** qui envoie les vraies requêtes Thrift automatiquement !

## 📝 Structure d'une requête Thrift

Quand vous utilisez le client Thrift Java, voici ce qui se passe :

```
1. Création du transport HTTP
   └─> THttpClient("http://localhost:9090/")

2. Ouverture de la connexion
   └─> transport.open()

3. Création du protocole binaire
   └─> TBinaryProtocol(transport)

4. Création du client Thrift
   └─> InternalCRMService.Client(protocol)

5. Appel de méthode (ex: countLeads())
   └─> Le client encode automatiquement la requête en binaire Thrift
   └─> Envoie via HTTP POST avec les données binaires
   └─> Le servlet décode et traite la requête
   └─> Renvoie la réponse encodée en Thrift
```

## 🧪 Test rapide avec un appel simple

Une fois VirtualCRM démarré, vous pouvez tester via ses endpoints REST qui utilisent Thrift en interne.

Ou utiliser le client en ligne de commande qui fait toute la chaîne :
```
Client → VirtualCRM (REST) → InternalCRM (Thrift) → Réponse
```

## ✅ Résumé

**Vous n'avez pas besoin d'envoyer manuellement les données Thrift !**

- ✅ VirtualCRM le fait automatiquement via `InternalCRMClient`
- ✅ Le client en ligne de commande peut aussi être utilisé
- ⚠️ curl ne peut pas le faire (il n'a pas le support Thrift)

**Action à faire** : Démarrer VirtualCRM et il gérera automatiquement les requêtes Thrift vers InternalCRM !

