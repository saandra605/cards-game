# Cards Game

## Requirements

Use JDK 25. The tests use JUnit 4.13.2 and Hamcrest 1.3. Both test libraries are in the `lib` folder.

## Build and test

Open PowerShell in the project folder and run:

```powershell
.\build.ps1
```

This compiles the game and test source, runs all tests, and creates `cards.jar`, `cardsTest.zip` and `cardsSubmission.zip`. The script stops if compilation or a test fails.

## Run the game

Run:

```powershell
.\run.ps1
```

Enter the number of players, then enter the path to a pack file when asked. The pack must contain eight times the number of players in lines. Each line must contain one non negative whole number.

The player and deck output files are created in the project folder.

## Package contents

The executable archive contains the game source and compiled game classes. The test archive contains the game and test source, compiled classes, test libraries, this README, and the build and run scripts. The submission archive contains both code archives, the design report and the development log.