function ApiError($Res) {
   Write-Host "API call failed! Check {partnerId}, {partnerSecret} and {appKey} and make sure you are located in the US, UK or Canada." -ForegroundColor Red
   Write-Output $Res
   EnterToExit
}

function EnterToExit() {
   Read-Host "`r`nPress Enter to exit..."
   Exit
}

If ($Args.Count -lt "3" -or $Args.Count -gt "4") {
   Write-Host "Usage: .\setup.ps1 {partnerId} {partnerSecret} {appKey} [--no-interaction]" -ForegroundColor Red
   EnterToExit
}

$PartnerId = $Args[0]
$PartnerSecret = $Args[1]
$AppKey = $Args[2]
$Interactive = ($Args[3] -ne "--no-interaction")

$Headers = @{
   "Content-Type" = "application/json"
   "Accept" = "application/json"
   "Finicity-App-Key" = "$AppKey"
}

Write-Output "`r`nStep 1 - Creating access token ..."
$Body = @{
   "partnerId" = "$PartnerId";
   "partnerSecret" = "$PartnerSecret"
} | ConvertTo-Json
$Res = Invoke-WebRequest -Uri https://api.finicity.com/aggregation/v2/partners/authentication `
                         -Method POST `
                         -Headers $Headers `
                         -Body $Body
If ($Res.StatusCode -ne "200") {
   ApiError($Res)
}

# {"token":"P09NAglkaBTyJrHoFGmL"}
$Token = ($Res.Content | ConvertFrom-Json).token
Write-Host "Access token: $Token" -ForegroundColor Green

Write-Output "`r`nStep 2 - Creating test customer ..."
$Random = Get-Random
$Headers["Finicity-App-Token"] = "$Token"
$Body = @{
    "username" = "customerusername_$Random$Random"
} | ConvertTo-Json
$Res = Invoke-WebRequest -Uri https://api.finicity.com/aggregation/v2/customers/testing `
                         -Method POST `
                         -Headers $Headers `
                         -Body $Body
If ($Res.StatusCode -ne "201") {
   ApiError($Res)
}

# {"id":"5026948632","username":"customerusername1","createdDate":"1649244189"}
$CustomerId = ($Res.Content | ConvertFrom-Json).id
Write-Host "Customer ID: $CustomerId" -ForegroundColor Green

If (!$Interactive) {
   Write-Output "`r`nSteps 3 and 4 - Skipping Connect, adding customer accounts ..."
   $Body = '{"credentials":[{"name":"Banking Userid","value":"profile_03","id":"102105001"},{"name":"Banking Password","value":"profile_03","id":"102105002"}]}'
   $Res = Invoke-WebRequest -Uri https://api.finicity.com/aggregation/v1/customers/$CustomerId/institutions/102105/accounts/addall `
                           -Method POST `
                           -Headers $Headers `
                           -Body $Body
   If ($Res.StatusCode -ne "200") {
      ApiError($Res)
   }
   # { "accounts": [...]}
   Write-Host "Accounts added" -ForegroundColor Green
}

If ($Interactive) {
   Write-Output "`r`nStep 3 - Generating connect URL ..."
   $Body = @{
      "partnerId" = "$PartnerId";
      "customerId" = "$CustomerId"
   } | ConvertTo-Json
   $Res = Invoke-WebRequest -Uri https://api.finicity.com/connect/v2/generate `
                           -Method POST `
                           -Headers $Headers `
                           -Body $Body
   If ($Res.StatusCode -ne "200") {
      ApiError($Res)
   }
   # {"link":"https://..."}
   $Link = ($Res.Content | ConvertFrom-Json).link
   Write-Host "URL created" -ForegroundColor Green

   Write-Host "`r`nStep 4 - Ctrl+click on the URL below, search for 'FinBank Profiles - A', then sign in with 'profile_03'/'profile_03' and add all available accounts"  -ForegroundColor Yellow
   Write-Host "`r`n$Link"
   Write-Host "`r`nAfter you see 'Your submission was successful. Thank you!', press Enter to continue ..." -ForegroundColor Yellow
   Pause
}

Write-Output "`r`nStep 5  - Refreshing accounts ..."
$Res = Invoke-WebRequest -Uri https://api.finicity.com/aggregation/v1/customers/$CustomerId/accounts `
                         -Method POST `
                         -Headers $Headers `
                         -Body $Body
If ($Res.StatusCode -ne "200") {
   ApiError($Res)
}
# { "accounts": [...]}
Write-Host "Accounts refreshed" -ForegroundColor Green

Write-Output `r`n"APP_KEY: $AppKey"
Write-Output "CUSTOMER_ID: $CustomerId"
Write-Output "PARTNER_ID: $PartnerId"
Write-Output "PARTNER_SECRET: $PartnerSecret"

Write-Host "`r`nTo run the API client tests:`r`n" -ForegroundColor Yellow
Write-Output "git clone https://github.com/Mastercard/open-banking-us-openapi"
Write-Output "cd open-banking-us-openapi-openapi/tests"
Write-Output "mvn clean test -DpartnerId=$PartnerId -DpartnerSecret=$PartnerSecret -DappKey=$AppKey -DcustomerId=$CustomerId"

Write-Host "`r`nTo run the Postman Collection:`r`n" -ForegroundColor Yellow
Write-Output "git clone https://github.com/Mastercard/open-banking-us-postman"
Write-Output "cd open-banking-us-postman"
Write-Output "npx newman run openbanking-us.postman_collection.json --env-var partnerId=$PartnerId --env-var partnerSecret=$PartnerSecret --env-var appKey=$AppKey --env-var customerId=$CustomerId --folder 'All APIs'"
EnterToExit