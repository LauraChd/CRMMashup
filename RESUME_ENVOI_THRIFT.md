# 🎯 Résumé : Comment envoyer les bonnes données Thrift

## ✅ Réponse simple

**Vous n'avez PAS besoin d'envoyer manuellement les données Thrift !**

Quand vous utilisez **VirtualCRM**, il envoie automatiquement les bonnes données Thrift.

## 🔧 Comment ça fonctionne ?

### 1. VirtualCRM utilise `InternalCRMClient`

Dans le code de VirtualCRM, quand il a besoin de données d'InternalCRM, il fait :

```java
InternalCRMClient client = new InternalCRMClient();
List<VirtualLeadDto> leads = client.getLeads();
```

### 2. `InternalCRMClient` gère tout automatiquement

```java
// Dans InternalCRMClient.java
TTransport transport = new THttpClient("http://localhost:9090/");
transport.open();  // Connexion HTTP
TProtocol protocol = new TBinaryProtocol(transport);  // Format Thrift binaire
InternalCRMService.Client client = new InternalCRMService.Client(protocol);
client.getLeads();  // ← ENVOIE AUTOMATIQUEMENT les données Thrift !
```

**Le client Thrift encode automatiquement** :
- Les paramètres de la méthode en binaire Thrift
- Les envoie via HTTP POST avec le bon format
- Reçoit et décode la réponse

## 📝 Pour tester

### Méthode 1 : Via VirtualCRM (automatique)

1. Démarrer InternalCRM (déjà fait ✅)
   ```bash
   ./gradlew :internalcrm:appStart
   ```

2. Démarrer VirtualCRM
   ```bash
   ./gradlew :virtualcrm:bootRun
   ```

3. VirtualCRM utilisera automatiquement `InternalCRMClient` qui envoie les vraies requêtes Thrift !

### Méthode 2 : Via le client en ligne de commande

```bash
./gradlew :client:shadowJar
java -jar client/build/libs/client-*-all.jar getLeads
```

Le client → VirtualCRM (REST) → InternalCRM (Thrift)

## ❌ Pourquoi curl ne fonctionne pas ?

`curl` envoie juste du HTTP simple :
```bash
curl http://localhost:9090/
# → GET / HTTP/1.1 (pas de données Thrift)
```

Le servlet Thrift attend des données binaires au format Thrift spécifique.

**Solution** : Utiliser VirtualCRM ou le client Java qui gèrent tout automatiquement !

## ✅ Conclusion

- ✅ **VirtualCRM** envoie automatiquement les données Thrift
- ✅ **Vous n'avez rien à faire manuellement**
- ❌ **curl** ne peut pas le faire (manque le support Thrift)

Une fois VirtualCRM démarré, tout fonctionne automatiquement !

