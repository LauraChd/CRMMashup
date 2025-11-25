# 📚 Guide complet : Comment tout fonctionne

## 🎯 Comprendre votre projet (d'après le sujet)

Votre projet est un **Mashup** qui combine plusieurs services :

```
┌─────────────────────────────────────────────────────────────────┐
│                    VirtualCRM (Port 8080)                       │
│  Service REST qui combine InternalCRM + Salesforce + GeoLoc     │
└───────────────────┬───────────────────┬─────────────┬───────────┘
                    │                   │             │
         ┌──────────▼────────┐ ┌───────▼──────┐ ┌────▼──────┐
         │  InternalCRM      │ │  Salesforce  │ │ OpenStreet│
         │  (Thrift)         │ │  (REST)      │ │ Map (REST)│
         │  Port 9090        │ │              │ │           │
         └───────────────────┘ └──────────────┘ └───────────┘
```

## ❌ Explication des erreurs

### Erreur 1 : InternalCRM - "Socket is closed by peer"

**Quand** : `curl http://localhost:9090/`

**Pourquoi c'est NORMAL** :
1. `curl` envoie une requête HTTP GET simple
2. Le servlet Thrift attend des données **binaires** au format Thrift
3. Le servlet essaie de lire les données Thrift mais ne trouve rien
4. → Erreur "Socket is closed by peer"

**Conclusion** : ✅ C'est normal ! Le serveur fonctionne, il attend juste des données Thrift.

### Erreur 2 : VirtualCRM - 500 Internal Server Error

**Quand** : VirtualCRM essaie d'appeler InternalCRM

**Pourquoi** :
- VirtualCRM appelle `internalCRMClient.countLeads()`
- Le client Thrift essaie de se connecter
- Il y a probablement une exception qui n'est pas gérée correctement

**À CORRIGER** ⚠️

## 🔍 Comment ça DOIT fonctionner

### 1. Le client Thrift encode automatiquement

Quand VirtualCRM fait :
```java
internalCRMClient.countLeads()
```

Le client fait automatiquement :
1. Créer une connexion HTTP vers `http://localhost:9090/`
2. **Encoder** la méthode `countLeads()` en binaire Thrift
3. Envoyer via HTTP POST avec Content-Type: `application/x-thrift`
4. Recevoir la réponse encodée en Thrift
5. **Décoder** et retourner le résultat

**Tout est automatique !** Vous n'avez rien à faire.

### 2. Le servlet Thrift décode automatiquement

Quand le servlet reçoit la requête Thrift :
1. Il lit les données binaires
2. Il **décode** la requête Thrift
3. Il appelle `InternalCRMServiceImpl.countLeads()`
4. Il **encode** la réponse en Thrift
5. Il renvoie via HTTP

## ✅ Comment tester correctement

### Option 1 : Via VirtualCRM (Recommandé)

```bash
# Les endpoints REST de VirtualCRM utilisent automatiquement InternalCRM via Thrift
curl http://localhost:8080/countLeads
curl http://localhost:8080/leads
```

### Option 2 : Via le client en ligne de commande

```bash
./gradlew :client:shadowJar
java -jar client/build/libs/client-*-all.jar getLeads
```

Le client → VirtualCRM (REST) → InternalCRM (Thrift)

## 🔧 Correction nécessaire

Le problème est que `InternalCRMClient` capture les exceptions mais ne les propage pas correctement. Il faut améliorer la gestion d'erreurs.

