# 🎯 Explication simple : Comment tout fonctionne

## 📋 Ce que vous avez (d'après le sujet)

**VirtualCRM** doit :
1. Récupérer les leads depuis **InternalCRM** (via Thrift)
2. Récupérer les leads depuis **Salesforce** (via REST)
3. Ajouter la **géolocalisation** (via OpenStreetMap)
4. **Fusionner** et **trier** les résultats
5. Exposer une **API REST**

## ❌ Les erreurs que vous voyez

### Erreur 1 : InternalCRM - "Socket is closed by peer"

**C'est NORMAL !** ✅

Quand vous faites `curl http://localhost:9090/` :
- curl envoie juste du HTTP simple
- Le servlet Thrift attend des données **binaires Thrift**
- Il ne peut pas lire → erreur

**Le serveur fonctionne !** Il attend juste des requêtes Thrift valides.

### Erreur 2 : VirtualCRM - 500 Error

**C'est le problème à corriger** ⚠️

Quand VirtualCRM essaie d'appeler InternalCRM :
- Il y a probablement une exception non gérée
- Ou une erreur de connexion

## ✅ Comment ça DOIT fonctionner

### Étape par étape :

1. **Client** appelle VirtualCRM : `GET /countLeads`
2. **VirtualCRMController** reçoit la requête
3. **VirtualCRMServiceImpl** appelle :
   ```java
   internalCRMClient.countLeads()  // ← Appel Thrift automatique
   ```
4. **InternalCRMClient** :
   - Crée une connexion HTTP vers `http://localhost:9090/`
   - **Encode** la requête en binaire Thrift (automatique)
   - Envoie via HTTP POST
   - Reçoit la réponse
   - **Décode** et retourne le résultat
5. **InternalCRM** :
   - Reçoit la requête Thrift
   - Décode et traite
   - Envoie la réponse Thrift
6. **VirtualCRM** renvoie le résultat au client

**Tout est automatique !** Vous n'avez rien à faire manuellement.

## 🔧 Ce qu'il faut corriger

Le problème est dans `InternalCRMClient` : les exceptions sont capturées mais pas propagées correctement.

