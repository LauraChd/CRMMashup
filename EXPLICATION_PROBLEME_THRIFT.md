# 🔍 Explication du problème de connexion Thrift

## 📋 Comment fonctionne la communication

### 1. **VirtualCRM (Client)** → Appelle InternalCRM

Quand VirtualCRM veut récupérer des données d'InternalCRM, il fait ceci :

```java
// Dans InternalCRMClient.java
TTransport transport = new THttpClient("http://localhost:9090/");
transport.open();  // ← Ici, il essaie de se connecter
```

**Ce qui se passe** :
- `THttpClient` fait une requête HTTP POST vers `http://localhost:9090/`
- Il envoie les données au format binaire Thrift
- Il attend une réponse au format Thrift

### 2. **InternalCRM (Serveur)** → Doit recevoir et répondre

Le servlet Thrift dans InternalCRM doit :
- ✅ Être démarré et écouter sur le port 9090
- ✅ Recevoir la requête HTTP POST
- ✅ Traiter les données Thrift
- ✅ Renvoyer une réponse Thrift

## ❌ Votre problème actuel

**Erreur de connexion** = VirtualCRM ne peut pas se connecter à `http://localhost:9090/`

### Causes possibles :

#### 1. **InternalCRM n'est pas démarré** ⚠️ (Probablement ça !)

Le serveur sur le port 9090 n'est pas lancé ou n'écoute pas.

**Solution** :
```bash
# Démarrer InternalCRM
./gradlew :internalcrm:appStart
```

**Vérifier** :
```bash
# Vérifier que le port 9090 est ouvert
netstat -tlnp | grep 9090
# ou
curl http://localhost:9090/
```

#### 2. **Le servlet Thrift n'est pas enregistré** ⚠️

Le servlet avec `@WebServlet(urlPatterns = {"/"})` peut ne pas être détecté automatiquement.

**Vérifier** : Regarder les logs de démarrage d'InternalCRM pour voir si le servlet est enregistré.

#### 3. **Mauvais port ou URL** 

- Si vous utilisez `farmRun`, le port peut être 8080 au lieu de 9090
- Le contextPath peut être `/internalcrm` au lieu de `/`

**Vérifier dans** : `internalcrm/build.gradle` ligne 52
```gradle
gretty {
    httpPort = 9090
    contextPath = '/'
}
```

#### 4. **Le servlet ne répond pas correctement aux requêtes Thrift**

Le servlet doit :
- Accepter les requêtes POST
- Utiliser le protocole binaire Thrift (`TBinaryProtocol`)
- Traiter correctement les données

## ✅ Diagnostic étape par étape

### Étape 1 : Vérifier si InternalCRM est démarré

```bash
# Vérifier si quelque chose écoute sur le port 9090
netstat -tlnp 2>/dev/null | grep 9090 || ss -tlnp 2>/dev/null | grep 9090

# Si rien ne s'affiche = InternalCRM n'est pas démarré !
```

### Étape 2 : Démarrer InternalCRM

```bash
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :internalcrm:appStart
```

**Attendez que vous voyiez dans les logs** :
- "Server started"
- Ou des messages de démarrage du serveur Gretty

### Étape 3 : Tester l'accès au servlet

```bash
# Test simple
curl -v http://localhost:9090/

# Doit retourner quelque chose, même si c'est une erreur Thrift
# (c'est normal, on n'envoie pas les bonnes données Thrift)
```

**Si vous avez** :
- `Connection refused` → InternalCRM n'est pas démarré
- `404 Not Found` → Le servlet n'est pas correctement mappé
- `200 OK` ou autre réponse → Le serveur fonctionne ! ✅

### Étape 4 : Tester avec VirtualCRM

Une fois InternalCRM démarré, VirtualCRM devrait pouvoir s'y connecter.

## 🔧 Solution rapide

```bash
# Terminal 1 - Démarrer InternalCRM
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :internalcrm:appStart

# Attendre 10-15 secondes pour le démarrage

# Terminal 2 - Vérifier que ça fonctionne
curl http://localhost:9090/

# Terminal 3 - Démarrer VirtualCRM
cd /home/etud/IdeaProjects/CRMMashup
./gradlew :virtualcrm:bootRun
```

## 📝 Résumé

**Le problème** : VirtualCRM essaie de se connecter à `http://localhost:9090/` mais ne peut pas.

**La cause** : Probablement InternalCRM n'est pas démarré ou le servlet Thrift n'est pas accessible.

**La solution** : 
1. Démarrer InternalCRM sur le port 9090
2. Vérifier que le servlet Thrift répond
3. Ensuite VirtualCRM pourra s'y connecter

