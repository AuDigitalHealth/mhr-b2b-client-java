#Requires -Version 5.1
# Plain mvn keeps your ~/.m2/settings.xml. Override WSDL tree with MHR_WSDL_RESOURCES_ROOT or
# -Dmhr.wsdl.resources.root=...; see README.md. Optional: $env:MVN_SETTINGS = path to settings.xml
param(
    [switch]$Shaded
)
Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot
foreach ($a in $args) {
    if ($a -match '^(?i)(shaded|-shaded|--shaded)$') {
        $Shaded = $true
    }
    else {
        [Console]::Error.WriteLine("Unknown argument: $a")
        [Console]::Error.WriteLine("Optional: -Shaded | shaded | -shaded | --shaded")
        exit 2
    }
}
$mvnArgs = @('-B')
if ($null -ne $env:MVN_SETTINGS -and $env:MVN_SETTINGS.Trim().Length -gt 0) {
    $mvnArgs += @('-s', $env:MVN_SETTINGS.Trim())
}
if ($Shaded) {
    Write-Output 'Building mhr-b2b-client FAT/UBER JAR'
    $mvnArgs += @('-Pfat-jar')
}
else {
    Write-Output 'Building mhr-b2b-client JAR'
}
$mvnArgs += @('-Dgpg.skip=true', 'clean', 'verify')
& mvn @mvnArgs
exit $LASTEXITCODE
