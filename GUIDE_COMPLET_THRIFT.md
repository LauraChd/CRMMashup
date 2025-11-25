# 📚 Guide complet : Comment envoyer les données Thrift

## 🎯 Réponse directe

**Vous n'avez PAS besoin d'envoyer manuellement les données Thrift !**

C'est **automatique** quand vous utilisez VirtualCRM.

## 🔍 Explication détaillée

### Le problème avec curl

```bash
curl http://localhost:9090/
```

**Pourquoi ça ne marche pas ?**
- curl envoie une requête HTTP GET simple
- Le servlet Thrift attend des données **binaires** au format **Thrift**
- Format spécialisé pour la communication RPC (Remote Procedure Call)

### La solution : Utiliser le client Thrift Java

Quand VirtualCRM appelle InternalCRM, il utilise `InternalCRMClient` qui :

1. **Crée un transport HTTP avec support Thrift**
   ```java
   TTransport transport = new THttpClient("http://localhost:9090/");
   ```

2. **Utilise le protocole binaire Thrift**
   ```java
   TProtocol protocol = new TBinaryProtocol(transport);
   ```

3. **Crée un client Thrift**
   ```java
   InternalCRMService.Client client = new InternalCRMService.Client(protocol);
   ```

4. **Appelle une méthode** (ex: `getLeads()`)
   ```java
   List<InternalLeadDto> leads = client.getLeads();
   // ← ICI : Le client encode AUTOMATIQUEMENT la requête en binaire Thrift
   // ← Envoie via HTTP POST avec Content-Type: application/x-thrift
   // ← Le serveur reçoit, décode et traite
   // ← Renvoie la réponse encodée en Thrift
   ```

**Tout est automatique !** Le client Thrift gère :
- ✅ L'encodage binaire des paramètres
- ✅ L'envoi HTTP POST avec les bons headers
- ✅ Le décodage de la réponse
- ✅ La gestion des erreurs Thrift

## ✅ Comment tester

### Étape 1 : Démarrer InternalCRM

```bash
./gradlew :internalcrm:appStart
```

**Vérifier** :
```bash
netstat -tlnp | grep 9090  # Port doit être ouvert
curl http://localhost:9090/  # Erreur 500 est normale (curl n'envoie pas de Thrift)
```

### Étape 2 : Démarrer VirtualCRM

```bash
./gradlew :virtualcrm:bootRun
```

VirtualCRM utilisera automatiquement `InternalCRMClient` qui envoie les vraies requêtes Thrift !

### Étape 3 : Tester

Via les endpoints REST de VirtualCRM ou via le client en ligne de commande :

```bash
./gradlew :client:shadowJar
java -jar client/build/libs/client-*-all.jar getLeads
```

## 📝 Résumé visuel

```
┌─────────────┐         ┌──────────────┐
│   Virtual   │  Appel  │   Internal   │
│     CRM     │ ──────> │     CRM      │
│             │ Thrift  │  (Port 9090) │
└─────────────┘         └──────────────┘
      │                        │
      │                        │
   Utilise              Reçoit requête
InternalCRMClient       Thrift binaire
qui encode              et traite
automatiquement         automatiquement
```

## ✅ Conclusion

- ✅ **VirtualCRM** fait tout automatiquement
- ✅ **Vous n'avez rien à faire manuellement**
- ✅ Les données Thrift sont encodées/décodées automatiquement
- ❌ **curl** ne peut pas le faire (pas de support Thrift)

**Une fois VirtualCRM démarré, tout fonctionne !** 🎉

