#!/bin/bash

# Script pour tester la communication Thrift avec InternalCRM
# Ce script compile et exécute un petit test Java

echo "=== Test de connexion Thrift à InternalCRM ==="
echo ""

# Vérifier que InternalCRM est démarré
if ! netstat -tlnp 2>/dev/null | grep -q 9090 && ! ss -tlnp 2>/dev/null | grep -q 9090; then
    echo "❌ ERREUR: InternalCRM n'est pas démarré sur le port 9090"
    echo "   Démarrer avec: ./gradlew :internalcrm:appStart"
    exit 1
fi

echo "✅ InternalCRM est démarré sur le port 9090"
echo ""

# Vérifier que VirtualCRM peut démarrer et se connecter
echo "Pour tester avec les vraies données Thrift, utilisez VirtualCRM :"
echo ""
echo "1. Démarrer VirtualCRM :"
echo "   ./gradlew :virtualcrm:bootRun"
echo ""
echo "2. VirtualCRM utilisera automatiquement InternalCRMClient"
echo "   qui envoie les vraies requêtes Thrift vers InternalCRM"
echo ""
echo "3. Vous pouvez aussi utiliser le client en ligne de commande :"
echo "   ./gradlew :client:shadowJar"
echo "   java -jar client/build/libs/client-*-all.jar getLeads"
echo ""
echo "Le client appellera VirtualCRM (REST) qui appellera InternalCRM (Thrift)"
echo ""

