# 🔍 Explication : Pourquoi curl échoue (et c'est NORMAL !)

## ✅ Le serveur fonctionne !

Votre serveur InternalCRM est bien démarré :
- ✅ Port 9090 ouvert
- ✅ Jetty démarré
- ✅ Servlet Thrift enregistré

## ❓ Pourquoi curl échoue-t-il ?

Quand vous faites :
```bash
curl http://localhost:9090/
```

Vous obtenez une erreur (500 Server Error). **C'est NORMAL !**

### Explication :

1. **curl envoie une requête HTTP GET simple**
   - Pas de données Thrift
   - Juste une requête HTTP classique

2. **Le servlet Thrift attend des données binaires Thrift**
   - Format spécifique Thrift
   - Protocole binaire (TBinaryProtocol)
   - Structure de message Thrift

3. **Le servlet essaie de lire des données Thrift mais ne trouve rien**
   - Il reçoit juste une requête GET vide
   - Il ne peut pas décoder les données (car il n'y en a pas)
   - → Erreur "Socket is closed by peer" ou "TTransportException"

## ✅ Comment tester que ça fonctionne vraiment ?

### Option 1 : Utiliser VirtualCRM (vraie requête Thrift)

VirtualCRM enverra des **vraies requêtes Thrift** avec les bonnes données :
- Format binaire Thrift
- Structure de message correcte
- Données encodées proprement

### Option 2 : Tester avec un client Thrift en ligne de commande

Vous pouvez créer un petit script Java qui envoie une vraie requête Thrift.

## 🎯 Conclusion

- **Le serveur fonctionne** ✅
- **curl échoue parce que ce n'est pas une requête Thrift** ⚠️ (normal)
- **VirtualCRM pourra se connecter** ✅ (avec de vraies requêtes Thrift)

## 📝 Vérification rapide

Le fait d'avoir une **erreur HTTP 500** au lieu de **"Connection refused"** signifie que :
- ✅ Le serveur répond
- ✅ Le servlet est accessible
- ⚠️ Il faut juste envoyer les bonnes données (ce que fait VirtualCRM automatiquement)

