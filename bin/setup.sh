#!/bin/bash

api_error() {
   echo "API call failed! Check {partnerId}, {partnerSecret} and {appKey} and make sure you are located in the US."
   enter_to_exit
}

enter_to_exit() {
  read -p "Press [Enter] to exit..."
  exit
}

if [ "$#" -ne 3 ]; then
    echo "Usage: ./setup.sh {partnerId} {partnerSecret} {appKey}"
    enter_to_exit
fi

echo "Creating access token ..."
token_response=$(curl -s --location --request POST 'https://api.finicity.com/aggregation/v2/partners/authentication' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Finicity-App-Key:'$3 \
--data-raw '{ "partnerId": "'$1'", "partnerSecret": "'$2'" }')

# {"token":"P09NAglkaBTyJrHoFGmL"}
if [[ "$token_response" != *"token"* ]]; then 
  api_error
fi;

token=$(echo $token_response | sed -E 's/\{"token":"(.*)"\}/\1/')
echo "Access token: "$token

echo "Creating test customer ..."
customer_response=$(curl -s --location --request POST 'https://api.finicity.com/aggregation/v2/customers/testing' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Finicity-App-Key:'$3 \
--header 'Finicity-App-Token:'$token \
--data-raw '{ "username": "customerusername_'$RANDOM$RANDOM'" }')

# {"id":"5026948632","username":"customerusername1","createdDate":"1649244189"}
if [[ "$customer_response" != *"username"* ]]; then 
  api_error
fi;

customer_id=$(echo $customer_response | sed -E 's/\{"id":"([0-9]*).*/\1/')
echo "Customer ID: "$customer_id

echo "Generating connect URL ..."
connect_url_response=$(curl -s --location --request POST 'https://api.finicity.com/connect/v2/generate' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Finicity-App-Key:'$3 \
--header 'Finicity-App-Token:'$token \
--data-raw '{ "partnerId": "'$1'", "customerId": "'$customer_id'" }')

# {"link":"https://..."}
link=$(echo $connect_url_response | sed -E 's/\{"link":"(.*)"\}/\1/')

echo ""
echo "Ctrl+click on the URL below, search for 'FinBank', then sign in with 'profile_09'/'password' and add all available accounts"
echo ""
echo $link
echo ""
read -p "After you see 'Thank you, your submission was successful', press [Enter] to continue ..."

echo "Refreshing accounts ..."
accounts_response=$(curl -s --location --request POST 'https://api.finicity.com/aggregation/v1/customers/'$customer_id'/accounts' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Finicity-App-Key:'$3 \
--header 'Finicity-App-Token:'$token \
--data-raw '{}')

# { "accounts": [...]}

echo ""
echo "APP_KEY: "$3
echo "CUSTOMER_ID: "$customer_id
echo "PARTNER_ID: "$1
echo "PARTNER_SECRET: "$2
echo ""

echo "mvn clean test -DpartnerId="$1" -DpartnerSecret="$2" -DappKey="$3" -DcustomerId="$customer_id""
echo ""

echo "npx newman run finicity.postman_collection.json --env-var partnerId="$1" --env-var partnerSecret="$2" --env-var appKey="$3" --env-var customerId="$customer_id""
enter_to_exit