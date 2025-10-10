#!/bin/bash
# retrieve.sh

source .env

# Exécute credentials.sh et récupère ACCESS_TOKEN et INSTANCE_URL
eval $(bash credentials.sh | grep -E 'ACCESS_TOKEN=|INSTANCE_URL=')

curl -G "$INSTANCE_URL/services/data/v45.0/query/" \
    --header "Content-Type: application/x-www-form-urlencoded" \
    --header "Authorization: Bearer $ACCESS_TOKEN" \
    --header "Accept: application/xml" \
    -d "q=SELECT+FirstName,LastName,ConvertedAccountId+FROM+Lead"
