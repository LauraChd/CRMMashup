# Explication du problème de communication Thrift

## 🔍 Architecture de communication

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│   Virtual   │  ────>  │   Internal   │  ────>  │    Model    │
│     CRM     │ Thrift  │     CRM      │  Appels │  (Données)  │
│  (Port 8080)│ HTTP    │  (Port 9090) │         │             │
└─────────────┘         └──────────────┘         └─────────────┘
```

## 🔧 Comment ça fonctionne

### 1. **InternalCRM** (Serveur Thrift)
- **Port** : 9090
- **Servlet Thrift** : `ThrifInternalCRMServiceServlet`
  - URL mapping : `@WebServlet(urlPatterns = {"/"})`
  - Donc accessible sur : `http://localhost:9090/`
- **Rôle** : Expose les méthodes Thrift définies dans `internalCRM.thrift`
  - `findLeads()`
  - `getLeads()`
  - `addLead()`
  - etc.

### 2. **VirtualCRM** (Client Thrift)
- **Port** : 8080 (Spring Boot)
- **Client** : `InternalCRMClient`
  - URL configurée : `http://localhost:9090/`
  - Utilise `THttpClient` pour se connecter au servlet Thrift
- **Rôle** : Appelle InternalCRM via Thrift pour récupérer/modifier les leads

## ❌ Problème actuel

Quand VirtualCRM essaie d'appeler InternalCRM, vous avez une **erreur de connexion**.

### Causes possibles :

1. **InternalCRM n'est pas démarré**
   - Le serveur sur le port 9090 n'est pas lancé
   - Vérifier : `curl http://localhost:9090/`

2. **Le servlet Thrift n'est pas correctement enregistré**
   - L'annotation `@WebServlet` peut ne pas être détectée
   - Il faut un `web.xml` ou que le serveur détecte automatiquement les annotations

3. **L'URL n'est pas correcte**
   - Si vous utilisez `farmRun`, le contextPath peut être différent
   - Le port peut être différent (8080 au lieu de 9090)

4. **Problème de protocole Thrift**
   - Le client envoie des données Thrift binaires
   - Le servlet doit les recevoir et les traiter correctement

## ✅ Solution : Vérifications à faire

### Étape 1 : Vérifier que InternalCRM est démarré

```bash
# Démarrer InternalCRM
./gradlew :internalcrm:appStart

# Vérifier qu'il écoute sur le port 9090
netstat -tlnp | grep 9090
# ou
ss -tlnp | grep 9090
```

### Étape 2 : Tester l'accès au servlet

```bash
# Test simple HTTP
curl -v http://localhost:9090/

# Doit retourner quelque chose (même une erreur est mieux que "connection refused")
```

### Étape 3 : Vérifier que le servlet est bien déployé

Le servlet doit être dans le WAR déployé. Vérifier dans les logs de démarrage.

### Étape 4 : Tester la communication Thrift

Une fois InternalCRM démarré, VirtualCRM devrait pouvoir s'y connecter.

## 🔍 Diagnostic du problème

Pour identifier précisément le problème, vérifiez :

1. **Les logs d'erreur** dans VirtualCRM quand il essaie de se connecter
2. **Les logs de démarrage** d'InternalCRM pour voir si le servlet est enregistré
3. **L'erreur exacte** : "Connection refused" vs "404 Not Found" vs autre

## 📝 Commandes de test

```bash
# 1. Démarrer InternalCRM
./gradlew :internalcrm:appStart

# 2. Attendre quelques secondes, puis vérifier
curl -v http://localhost:9090/

# 3. Dans un autre terminal, démarrer VirtualCRM
./gradlew :virtualcrm:bootRun

# 4. Observer les logs pour voir l'erreur de connexion
```

