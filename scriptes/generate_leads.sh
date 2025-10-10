#!/bin/bash

source .env

# 1. Authentification (récupère le token et l'URL de l'instance)
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
  echo "❌ Erreur d'authentification Salesforce."
  echo "Réponse brute : $RESPONSE"
  exit 1
fi

echo "✅ Authentifié avec succès."

# 2. Générer N leads
N=5  # nombre de leads à créer

for i in $(seq 1 $N); do
  ID=$((1000 + $i))
  FIRST_NAME="Lead$i"
  LAST_NAME="Test"
  ANNUAL_REVENUE=$(awk -v min=10000 -v max=1000000 'BEGIN{srand(); print int(min+rand()*(max-min+1))}')
  PHONE="06$(shuf -i 10000000-99999999 -n 1)"
  STREET="123 Rue Exemple"
  POSTAL_CODE="7500$i"
  CITY="Paris"
  COUNTRY="France"
  CREATION_DATE=$(date +%s%3N)  # timestamp en millisecondes
  COMPANY="Entreprise$i"
  STATE="Ile-de-France"

  # 3. Envoi à Salesforce
  JSON_PAYLOAD=$(cat <<EOF
{
  "FirstName": "$FIRST_NAME",
  "LastName": "$LAST_NAME",
  "Company": "$COMPANY",
  "Phone": "$PHONE",
  "Street": "$STREET",
  "PostalCode": "$POSTAL_CODE",
  "City": "$CITY",
  "Country": "$COUNTRY",
  "AnnualRevenue": $ANNUAL_REVENUE,
  "State": "$STATE"
}
EOF
)

  echo "🔄 Création du Lead $i..."
  RESPONSE=$(curl -s -X POST "$INSTANCE/services/data/v45.0/sobjects/Lead" \
    -H "Authorization: Bearer $ACCESS_TOKEN" \
    -H "Content-Type: application/json" \
    -d "$JSON_PAYLOAD")

  echo "📩 Réponse : $RESPONSE"
done
