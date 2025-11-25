# ⚠️ Problème avec VirtualCRM : Build Failed

## 🔍 Le problème

Quand vous lancez :
```bash
./gradlew :virtualcrm:bootRun
```

Vous obtenez :
```
APPLICATION FAILED TO START
Web application could not be started as there was no 
org.springframework.boot.web.servlet.server.ServletWebServerFactory bean defined in the context.
```

## 🔧 Cause

Le plugin `war` marque automatiquement `spring-boot-starter-tomcat` comme `providedRuntime`, ce qui le retire du classpath pour `bootRun`. Spring Boot ne trouve donc pas de serveur web.

## ✅ Solutions possibles

### Solution 1 : Utiliser gretty au lieu de bootRun (Recommandé)

Puisque vous utilisez déjà gretty pour internalcrm, utilisez-le aussi pour virtualcrm :

```bash
# Modifier virtualcrm/build.gradle pour ajouter la config gretty
```

Ou utiliser `farmRun` pour lancer tout ensemble.

### Solution 2 : Retirer temporairement le plugin war

Pour le développement avec bootRun, vous pouvez commenter le plugin war.

### Solution 3 : Utiliser le client directement

Au lieu de lancer VirtualCRM, utilisez le client qui appelle VirtualCRM (si VirtualCRM est déjà déployé ailleurs).

## 📝 État actuel

- ✅ **InternalCRM** : Fonctionne sur le port 9090
- ⚠️ **VirtualCRM** : Ne démarre pas à cause du conflit war/bootRun

## 💡 Recommandation

Pour tester la communication Thrift **maintenant**, vous pouvez :
1. Utiliser le client directement (si VirtualCRM est déployé)
2. Ou déployer VirtualCRM comme WAR dans un serveur externe

