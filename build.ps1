#Requires -Version 5.1
# Install pcehr-compiled-wsdl-java (java-11-jakarta) before first build; see README.md.
# Optional: $env:MVN_SETTINGS = path to settings.xml
param(
    [switch]$Shaded,
    [switch]$Wsimport
)
Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot
foreach ($a in $args) {
    if ($a -match '^(?i)(shaded|-shaded|--shaded)$') {
        $Shaded = $true
    }
    elseif ($a -match '^(?i)(wsimport|-wsimport|--wsimport)$') {
        $Wsimport = $true
    }
    else {
        [Console]::Error.WriteLine("Unknown argument: $a")
        [Console]::Error.WriteLine("Optional: -Shaded | shaded; -Wsimport | wsimport (validate WSDL codegen)")
        exit 2
    }
}
if ($Shaded -and $Wsimport) {
    [Console]::Error.WriteLine("Use either -Shaded or -Wsimport, not both.")
    exit 2
}
$mvnArgs = @('-B')
if ($null -ne $env:MVN_SETTINGS -and $env:MVN_SETTINGS.Trim().Length -gt 0) {
    $mvnArgs += @('-s', $env:MVN_SETTINGS.Trim())
}
if ($Wsimport) {
    Write-Output 'Building mhr-b2b-client JAR (with in-repo wsimport validation)'
    $mvnArgs += @('-Pwsimport')
}
elseif ($Shaded) {
    Write-Output 'Building mhr-b2b-client FAT/UBER JAR'
    $mvnArgs += @('-Pfat-jar')
}
else {
    Write-Output 'Building mhr-b2b-client JAR'
}
$mvnArgs += @('-Dgpg.skip=true', 'clean', 'verify')
& mvn @mvnArgs
exit $LASTEXITCODE
