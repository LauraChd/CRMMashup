#!/bin/bash
# credentials.sh

source .env

RESPONSE=$(curl --silent --location --request POST "$INSTANCE_URL/services/oauth2/token" \
 --header 'Content-Type: application/x-www-form-urlencoded' \
 --data-urlencode "grant_type=password" \
 --data-urlencode "client_id=$CLIENT_ID" \
 --data-urlencode "client_secret=$CLIENT_SECRET" \
 --data-urlencode "username=$USERNAME" \
 --data-urlencode "password=$PASSWORD")

# Extraire le token et l’instance URL depuis la réponse JSON
ACCESS_TOKEN=$(echo "$RESPONSE" | grep -o '"access_token":"[^"]*' | cut -d':' -f2 | tr -d '"')
INSTANCE_URL=$(echo "$RESPONSE" | grep -o '"instance_url":"[^"]*' | cut -d':' -f2- | tr -d '"')

# Afficher ou enregistrer les variables pour un usage ultérieur
echo "ACCESS_TOKEN=$ACCESS_TOKEN"
echo "INSTANCE_URL=$INSTANCE_URL"

