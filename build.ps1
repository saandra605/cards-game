$ErrorActionPreference = "Stop"
$project = Split-Path -Parent $MyInvocation.MyCommand.Path
$appOutput = Join-Path $project "build\app"
$testOutput = Join-Path $project "build\tests"

Push-Location $project
try {
    New-Item -ItemType Directory -Force -Path $appOutput, $testOutput | Out-Null

    javac -d $appOutput src\Card.java src\Deck.java src\Player.java src\CardGame.java
    if ($LASTEXITCODE -ne 0) {
        throw "Game compilation failed."
    }

    $junit = Join-Path $project "lib\junit-4.13.2.jar"
    $hamcrest = Join-Path $project "lib\hamcrest-core-1.3.jar"
    $libraries = "$junit;$hamcrest"
    $testClasspath = "$appOutput;$testOutput;$libraries"

    javac -cp "$appOutput;$libraries" -d $testOutput test\CardTest.java test\CardGameTest.java test\DeckTest.java test\PlayerTest.java
    if ($LASTEXITCODE -ne 0) {
        throw "Test compilation failed."
    }

    $testRun = Join-Path $env:TEMP ("cards-test-run-" + [guid]::NewGuid().ToString("N"))
    New-Item -ItemType Directory -Path $testRun | Out-Null
    Push-Location $testRun
    try {
        java -cp $testClasspath org.junit.runner.JUnitCore CardTest CardGameTest DeckTest PlayerTest
        $testExitCode = $LASTEXITCODE
    }
    finally {
        Pop-Location
        Remove-Item -LiteralPath $testRun -Recurse -Force
    }

    if ($testExitCode -ne 0) {
        throw "Tests failed."
    }

    jar cfe cards.jar CardGame -C $appOutput . -C src Card.java -C src Deck.java -C src Player.java -C src CardGame.java
    if ($LASTEXITCODE -ne 0) {
        throw "JAR creation failed."
    }

    $stage = Join-Path $env:TEMP ("cards-test-package-" + [guid]::NewGuid().ToString("N"))
    $classOutput = Join-Path $stage "classes"
    $sourceStage = Join-Path $stage "src"
    $testStage = Join-Path $stage "test"
    $libraryStage = Join-Path $stage "lib"
    New-Item -ItemType Directory -Force -Path $classOutput, $sourceStage, $testStage, $libraryStage | Out-Null
    Copy-Item src\*.java $sourceStage
    Copy-Item test\*.java $testStage
    Copy-Item $appOutput\*.class $classOutput
    Copy-Item $testOutput\*.class $classOutput
    Copy-Item lib\*.jar $libraryStage
    Copy-Item README.md, build.ps1, run.ps1 $stage
    Compress-Archive -Path (Join-Path $stage "*") -DestinationPath cardsTest.zip -Force
    Remove-Item -LiteralPath $stage -Recurse -Force

    Compress-Archive -Path cards.jar,cardsTest.zip,DESIGN_REPORT.html,DEVLOG.md -DestinationPath cardsSubmission.zip -Force
}
finally {
    Pop-Location
}