#!/bin/bash

source .env

# Authentification
RESPONSE=$(curl --silent --location --request POST "$INSTANCE_URL/services/oauth2/token" \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode "grant_type=password" \
  --data-urlencode "client_id=$CLIENT_ID" \
  --data-urlencode "client_secret=$CLIENT_SECRET" \
  --data-urlencode "username=$USERNAME" \
  --data-urlencode "password=$PASSWORD")

ACCESS_TOKEN=$(echo "$RESPONSE" | grep -o '"access_token":"[^"]*' | cut -d':' -f2 | tr -d '"')
INSTANCE=$(echo "$RESPONSE" | grep -o '"instance_url":"[^"]*' | cut -d':' -f2- | tr -d '"')

if [[ -z "$ACCESS_TOKEN" || -z "$INSTANCE" ]]; then
  echo "Erreur d'authentification Salesforce."
  echo "Réponse brute : $RESPONSE"
  exit 1
fi

echo "Authentifié avec succès."

# Lire le CSV ligne par ligne (en ignorant l'entête)
tail -n +2 leads.csv | while IFS=',' read -r FirstName LastName Company Phone Street PostalCode City Country AnnualRevenue State
do
  JSON_PAYLOAD=$(cat <<EOF
{
  "FirstName": "$FirstName",
  "LastName": "$LastName",
  "Company": "$Company",
  "Phone": "$Phone",
  "Street": "$Street",
  "PostalCode": "$PostalCode",
  "City": "$City",
  "Country": "$Country",
  "AnnualRevenue": $AnnualRevenue,
  "State": "$State"
}
EOF
)

  echo "Création du lead $FirstName $LastName..."
  RESPONSE=$(curl -s -X POST "$INSTANCE/services/data/v45.0/sobjects/Lead" \
    -H "Authorization: Bearer $ACCESS_TOKEN" \
    -H "Content-Type: application/json" \
    -d "$JSON_PAYLOAD")

  echo "Réponse : $RESPONSE"
done
