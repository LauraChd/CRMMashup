# ✅ Guide d'intégration du service web Thrift dans VirtualCRM

## 🎯 État actuel : TOUT FONCTIONNE !

### Services démarrés

- ✅ **InternalCRM** : Port 9090 (Service Thrift)
- ✅ **VirtualCRM** : Port 8080 (Spring Boot avec intégration Thrift)

## 🔧 Comment l'intégration fonctionne

### 1. Configuration automatique

VirtualCRM est **déjà configuré** pour utiliser InternalCRM via Thrift :

```java
// Dans VirtualCRMServiceFactory.java
virtualCRMService = new VirtualCRMServiceImpl(
    new SalesforceCRMClient(), 
    new InternalCRMClient(),  // ← Client Thrift pour InternalCRM
    new GeoLocalisationServiceClient()
);
```

### 2. Client Thrift configuré

```java
// Dans InternalCRMClient.java
public static final String INTERNALCRM_URL = "http://localhost:9090/";

// Toutes les méthodes utilisent automatiquement Thrift :
TTransport transport = new THttpClient(INTERNALCRM_URL);
TProtocol protocol = new TBinaryProtocol(transport);
InternalCRMService.Client client = new InternalCRMService.Client(protocol);
```

### 3. Intégration complète

VirtualCRM peut maintenant :
- ✅ Appeler InternalCRM via Thrift
- ✅ Utiliser toutes les méthodes : `getLeads()`, `findLeads()`, `addLead()`, etc.
- ✅ Recevoir et convertir les données automatiquement

## 🚀 Commandes pour démarrer tout

### Terminal 1 : InternalCRM
```bash
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :internalcrm:appStart
```

### Terminal 2 : VirtualCRM
```bash
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :virtualcrm:bootRun
```

### Terminal 3 : Vérifier
```bash
# Vérifier InternalCRM
curl http://localhost:9090/
# (Erreur 500 est normale - curl n'envoie pas de Thrift)

# Vérifier VirtualCRM
curl http://localhost:8080/
# (Doit retourner quelque chose ou une page d'accueil)
```

## 📝 Utilisation dans votre code

### Dans VirtualCRM, utilisez simplement :

```java
// Récupérer le service (utilise déjà InternalCRMClient)
VirtualCRMServiceImpl service = VirtualCRMServiceFactory.getInstance();

// Appeler les méthodes - elles utiliseront automatiquement InternalCRM via Thrift
List<VirtualLeadDto> leads = service.getLeads();
int count = service.countLeads();
```

**Tout est automatique !** VirtualCRM gère la communication Thrift avec InternalCRM.

## ✅ Vérification que ça marche

### Test 1 : Vérifier que les services sont démarrés
```bash
netstat -tlnp | grep -E "8080|9090"
# Doit afficher les deux ports ouverts
```

### Test 2 : Utiliser le client en ligne de commande
```bash
./gradlew :client:shadowJar
java -jar client/build/libs/client-*-all.jar getLeads
```

Le client → VirtualCRM (REST) → InternalCRM (Thrift) → Réponse

## 🎉 Résumé

- ✅ **InternalCRM** : Service Thrift sur port 9090
- ✅ **VirtualCRM** : Spring Boot sur port 8080
- ✅ **Intégration** : VirtualCRM utilise automatiquement InternalCRM via Thrift
- ✅ **Prêt à utiliser** : Tout fonctionne !

Vous pouvez maintenant utiliser VirtualCRM qui communiquera automatiquement avec InternalCRM via Thrift !

