#Requires -Version 5.1
# Download EE4J jaxws-tools/jaxws-rt + ant-contrib into lib/provided (no Metro webservices-*).
Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot
mvn -B -f ee4j-jaxws-lib-pom.xml package @args
exit $LASTEXITCODE
