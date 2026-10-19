$project = Split-Path -Parent $MyInvocation.MyCommand.Path
Push-Location $project
try {
    if (-not (Test-Path cards.jar)) {
        Write-Error "Run build.ps1 first to create cards.jar."
        exit 1
    }

    java -jar cards.jar
}
finally {
    Pop-Location
}