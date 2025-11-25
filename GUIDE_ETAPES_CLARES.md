# 📖 Guide étape par étape - Comment tout faire fonctionner

## 🎯 Comprendre le projet (d'après le sujet)

### Architecture globale

```
Client CLI → VirtualCRM (REST) → [InternalCRM (Thrift) + Salesforce (REST) + GeoLocalisation (REST)]
```

**VirtualCRM** doit :
1. Récupérer les leads depuis InternalCRM (via Thrift) ✅
2. Récupérer les leads depuis Salesforce (via REST)
3. Ajouter la géolocalisation (via OpenStreetMap)
4. Fusionner et trier les résultats
5. Exposer une API REST

## ❌ Les erreurs actuelles

### Erreur 1 : InternalCRM - "Socket is closed by peer"

**QUAND** : Quand vous faites `curl http://localhost:9090/`

**POURQUOI C'EST NORMAL** :
- `curl` envoie juste du HTTP simple
- Le servlet Thrift attend des données **binaires Thrift**
- Il ne peut pas décoder → erreur

**C'EST NORMAL !** ✅ Ne vous inquiétez pas de cette erreur avec curl.

### Erreur 2 : VirtualCRM - 500 Internal Server Error

**QUAND** : Quand VirtualCRM essaie d'appeler InternalCRM

**POURQUOI** :
- VirtualCRM appelle `internalCRMClient.countLeads()`
- Le client essaie de se connecter à InternalCRM via Thrift
- Il y a probablement une exception non gérée ou une erreur de connexion

**À CORRIGER** ⚠️

## 🔧 Étapes pour corriger

### Étape 1 : Comprendre comment VirtualCRM appelle InternalCRM

```java
// Dans VirtualCRMServiceImpl.countLeads() :
int countLeads = salesforceCRMClient.countLeads() + internalCRMClient.countLeads();
//                                                      ↑ ICI : Appel Thrift
```

### Étape 2 : Comprendre comment InternalCRMClient envoie les données Thrift

```java
// Dans InternalCRMClient.countLeads() :
TTransport transport = new THttpClient("http://localhost:9090/");
transport.open();  // ← Connexion HTTP avec support Thrift
TProtocol protocol = new TBinaryProtocol(transport);  // Format binaire Thrift
InternalCRMService.Client client = new InternalCRMService.Client(protocol);
int count = client.countLeads();  // ← ENVOIE les données Thrift !
```

**C'est automatique !** Le client Thrift encode tout.

### Étape 3 : Vérifier que InternalCRM est accessible

```bash
# InternalCRM doit être démarré sur le port 9090
netstat -tlnp | grep 9090
```

### Étape 4 : Corriger les erreurs dans InternalCRMClient

Le problème est que quand il y a une exception, le client renvoie `-1` au lieu de gérer l'erreur proprement.

## ✅ Solution : Corriger InternalCRMClient

Il faut gérer les exceptions correctement pour voir les vraies erreurs.

