# ✅ Correction : Ajout du web.xml

## Problème résolu

Le servlet Thrift n'était pas enregistré car il manquait le fichier `web.xml`.

## Solution appliquée

### 1. Création du répertoire webapp
```bash
mkdir -p internalcrm/src/main/webapp/WEB-INF
```

### 2. Création du web.xml
Fichier : `internalcrm/src/main/webapp/WEB-INF/web.xml`

Configuration :
- Servlet class : `fr.univangers.internalcrm.service.ThrifInternalCRMServiceServlet`
- URL pattern : `/` (racine)
- Load-on-startup : 1 (chargement au démarrage)

### 3. Retrait de l'annotation @WebServlet
Puisque le servlet est maintenant défini dans web.xml, l'annotation n'est plus nécessaire.

## Vérification

Le serveur démarre maintenant correctement :
- ✅ Port 9090 ouvert
- ✅ Servlet enregistré et accessible
- ✅ InternalCRM répond aux requêtes

## Prochaine étape

Maintenant que InternalCRM fonctionne, vous pouvez :
1. Démarrer VirtualCRM (qui va se connecter à InternalCRM via Thrift)
2. Tester la communication complète

