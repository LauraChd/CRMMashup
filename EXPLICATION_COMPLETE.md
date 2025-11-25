# 📚 Explication complète du fonctionnement

## 🎯 Vue d'ensemble du projet

D'après le sujet, voici ce qui doit se passer :

```
┌─────────────┐
│   Client    │  (Application ligne de commande)
│   (CLI)     │
└──────┬──────┘
       │ REST
       ▼
┌─────────────────────────────────────────────────────────┐
│           VirtualCRM (Spring Boot - Port 8080)          │
│  ┌─────────────────────────────────────────────────┐   │
│  │  VirtualCRMService                              │   │
│  │  - Récupère les leads des 2 sources            │   │
│  │  - Ajoute la géolocalisation                    │   │
│  │  - Trie par profit potentiel                    │   │
│  └───────┬─────────────────┬────────────────┬──────┘   │
│          │                 │                │           │
│     ┌────▼────┐    ┌──────▼──────┐   ┌─────▼─────┐   │
│     │Internal │    │  Salesforce │   │ GeoLocal  │   │
│     │ CRM     │    │    CRM      │   │ Service   │   │
│     │ Client  │    │   Client    │   │  Client   │   │
│     └────┬────┘    └──────┬──────┘   └─────┬─────┘   │
└──────────┼─────────────────┼────────────────┼─────────┘
           │ Thrift          │ REST           │ REST
           ▼                 ▼                ▼
    ┌──────────────┐  ┌────────────┐  ┌──────────────┐
    │ InternalCRM  │  │ Salesforce │  │ OpenStreetMap│
    │ (Port 9090)  │  │   (API)    │  │   (API)      │
    │   Thrift     │  │    REST    │  │    REST      │
    └──────────────┘  └────────────┘  └──────────────┘
```

## 🔍 Explication des erreurs actuelles

### Erreur 1 : InternalCRM (Socket is closed by peer)

**Quand ça arrive** : Quand vous faites `curl http://localhost:9090/`

**Pourquoi** : 
- `curl` envoie une requête HTTP GET simple (sans données Thrift)
- Le servlet Thrift attend des données **binaires** au format Thrift
- Il essaie de lire les données Thrift mais ne trouve rien → erreur

**C'est NORMAL !** ✅ Cette erreur est attendue car curl ne peut pas envoyer de données Thrift.

### Erreur 2 : VirtualCRM (500 Internal Server Error)

**Quand ça arrive** : Quand VirtualCRM essaie d'appeler InternalCRM

**Pourquoi** : 
- VirtualCRM essaie de se connecter à InternalCRM via Thrift
- Il y a peut-être une erreur dans la communication Thrift
- Ou une exception dans le code de VirtualCRM

**À CORRIGER** ⚠️ Il faut vérifier les logs et corriger.

## 🔧 Comment ça DOIT fonctionner

### Étape 1 : VirtualCRM appelle InternalCRMClient

```java
// Dans VirtualCRMServiceImpl.java
List<VirtualLeadDto> leads = internalCRMClient.getLeads();
```

### Étape 2 : InternalCRMClient crée une connexion Thrift

```java
// Dans InternalCRMClient.java
TTransport transport = new THttpClient("http://localhost:9090/");
transport.open();  // ← Connexion HTTP avec support Thrift
TProtocol protocol = new TBinaryProtocol(transport);  // Format binaire
InternalCRMService.Client client = new InternalCRMService.Client(protocol);
List<InternalLeadDto> leads = client.getLeads();  // ← VRAIE requête Thrift !
```

### Étape 3 : InternalCRM reçoit et traite

- Le servlet Thrift reçoit les données binaires
- Il décode la requête Thrift
- Il appelle `InternalCRMServiceImpl.getLeads()`
- Il renvoie la réponse encodée en Thrift

### Étape 4 : VirtualCRM reçoit et convertit

- VirtualCRM reçoit les données Thrift
- Il convertit `InternalLeadDto` en `VirtualLeadDto`
- Il renvoie la liste au client

## ✅ Ce qui fonctionne déjà

1. ✅ **InternalCRM démarre** sur le port 9090
2. ✅ **VirtualCRM démarre** sur le port 8080
3. ✅ **Le servlet Thrift est configuré** dans InternalCRM
4. ✅ **Le client Thrift est configuré** dans VirtualCRM

## ⚠️ Ce qu'il faut vérifier

1. ⚠️ **Les erreurs dans VirtualCRM** - Regarder les logs pour voir l'erreur exacte
2. ⚠️ **La conversion des données** - Vérifier que les DTO sont correctement convertis
3. ⚠️ **Les exceptions non gérées** - Gérer les erreurs Thrift correctement

