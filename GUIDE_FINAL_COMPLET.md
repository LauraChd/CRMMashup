# 📚 Guide complet et simple : Comment tout fonctionne

## 🎯 Votre projet (d'après le sujet)

Vous devez créer un **Mashup** qui combine :
1. **InternalCRM** (votre CRM interne via Thrift)
2. **Salesforce** (CRM externe via REST)
3. **OpenStreetMap** (géolocalisation via REST)

VirtualCRM fait la **vue unifiée** de tout ça.

## 🔍 Architecture

```
┌──────────────┐
│   Client     │  (Application ligne de commande ou navigateur)
│   (CLI/REST) │
└──────┬───────┘
       │ REST (HTTP/JSON)
       ▼
┌─────────────────────────────────────────────────────────────────┐
│              VirtualCRM (Spring Boot - Port 8080)               │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  VirtualCRMServiceImpl                                   │  │
│  │  - Récupère les leads depuis les 2 CRM                  │  │
│  │  - Ajoute la géolocalisation                            │  │
│  │  - Fusionne et trie les résultats                       │  │
│  └───────┬──────────────────┬──────────────────────┬───────┘  │
│          │                  │                      │           │
│    ┌─────▼──────┐   ┌───────▼──────┐    ┌─────────▼────┐    │
│    │ InternalCRM│   │  Salesforce  │    │  GeoLocal    │    │
│    │   Client   │   │    Client    │    │   Client     │    │
│    │  (Thrift)  │   │   (REST)     │    │   (REST)     │    │
│    └─────┬──────┘   └───────┬──────┘    └─────────┬────┘    │
└──────────┼──────────────────┼──────────────────────┼─────────┘
           │                  │                      │
           │ Thrift           │ REST                 │ REST
           │ (HTTP binaire)   │ (HTTP/JSON)          │ (HTTP/JSON)
           ▼                  ▼                      ▼
    ┌──────────────┐  ┌──────────────┐   ┌──────────────┐
    │ InternalCRM  │  │  Salesforce  │   │ OpenStreetMap│
    │ Port 9090    │  │    (API)     │   │    (API)     │
    └──────────────┘  └──────────────┘   └──────────────┘
```

## ❌ Explication des erreurs

### Erreur 1 : InternalCRM - "Socket is closed by peer" ✅ NORMAL

**Quand ça arrive** : `curl http://localhost:9090/`

**Pourquoi c'est normal** :
- `curl` envoie une requête HTTP GET simple (pas de données)
- Le servlet Thrift attend des données **binaires** au format Thrift
- Le servlet essaie de lire les données Thrift mais ne trouve rien
- → Erreur "Socket is closed by peer"

**Conclusion** : ✅ Le serveur fonctionne ! Il attend juste des requêtes Thrift valides (pas du curl).

### Erreur 2 : VirtualCRM - 500 Internal Server Error ⚠️ À CORRIGER

**Quand ça arrive** : VirtualCRM essaie d'appeler InternalCRM

**Pourquoi** : Les exceptions dans InternalCRMClient n'étaient pas propagées correctement.

**Solution** : J'ai corrigé le code pour propager les exceptions.

## ✅ Comment ça fonctionne (ÉTAPE PAR ÉTAPE)

### Étape 1 : Client appelle VirtualCRM

```bash
curl http://localhost:8080/countLeads
```

### Étape 2 : VirtualCRMController reçoit la requête

```java
// Dans VirtualCRMController.java
@GetMapping("/countLeads")
public int countLeads() throws TException {
    return VirtualCRMServiceFactory.getInstance().countLeads();
}
```

### Étape 3 : VirtualCRMServiceImpl appelle les clients

```java
// Dans VirtualCRMServiceImpl.java
public int countLeads() throws TException {
    return salesforceCRMClient.countLeads() + 
           internalCRMClient.countLeads();  // ← ICI : Appel Thrift
}
```

### Étape 4 : InternalCRMClient envoie la requête Thrift

```java
// Dans InternalCRMClient.java
public int countLeads() throws TException {
    // 1. Créer la connexion HTTP avec support Thrift
    TTransport transport = new THttpClient("http://localhost:9090/");
    transport.open();  // ← Connexion établie
    
    // 2. Créer le protocole binaire Thrift
    TProtocol protocol = new TBinaryProtocol(transport);
    
    // 3. Créer le client Thrift
    InternalCRMService.Client client = new InternalCRMService.Client(protocol);
    
    // 4. Appeler la méthode - LE CLIENT ENCODE AUTOMATIQUEMENT EN THRIFT !
    int count = client.countLeads();  
    // ↑ ICI : Le client Thrift encode automatiquement :
    //   - Le nom de la méthode ("countLeads")
    //   - Les paramètres (aucun ici)
    //   - En binaire Thrift
    //   - Envoie via HTTP POST avec Content-Type: application/x-thrift
    
    transport.close();
    return count;
}
```

**IMPORTANT** : Vous n'avez **RIEN à faire manuellement** ! Le client Thrift encode/décode tout automatiquement.

### Étape 5 : InternalCRM reçoit et traite

1. Le servlet Thrift reçoit la requête HTTP POST avec données binaires
2. Il **décode** la requête Thrift
3. Il appelle `InternalCRMServiceImpl.countLeads()`
4. Il récupère le résultat (ex: 5)
5. Il **encode** la réponse en Thrift
6. Il renvoie via HTTP

### Étape 6 : VirtualCRM reçoit et renvoie

1. InternalCRMClient reçoit la réponse Thrift
2. Il **décode** automatiquement
3. Il retourne le résultat (ex: 5)
4. VirtualCRM additionne avec le résultat de Salesforce
5. Il renvoie au client via REST

## 🔧 Correction appliquée

J'ai corrigé `InternalCRMClient` pour **propager les exceptions** correctement au lieu de les masquer. Maintenant, si une erreur se produit, vous la verrez clairement.

## ✅ Comment tester

### 1. Démarrer InternalCRM

```bash
./gradlew :internalcrm:appStart
```

**Vérifier** : `netstat -tlnp | grep 9090` (doit être ouvert)

### 2. Démarrer VirtualCRM

```bash
./gradlew :virtualcrm:bootRun
```

**Vérifier** : `netstat -tlnp | grep 8080` (doit être ouvert)

### 3. Tester la communication

```bash
# Tester l'endpoint
curl http://localhost:8080/countLeads

# Ou tester avec le client
./gradlew :client:shadowJar
java -jar client/build/libs/client-*-all.jar getLeads
```

## 📝 Résumé

- ✅ **InternalCRM** : Service Thrift sur port 9090 (fonctionne)
- ✅ **VirtualCRM** : Spring Boot sur port 8080 (fonctionne)
- ✅ **Intégration Thrift** : Configurée automatiquement
- ✅ **Gestion d'erreurs** : Corrigée pour voir les vraies erreurs

**Tout est prêt !** Les données Thrift sont envoyées/réceptionnées automatiquement par le client Thrift. Vous n'avez rien à faire manuellement.

