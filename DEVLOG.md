# Development Log

## 8 Oct 2026 [10:55] (20 min)
Driver: Sandra
Navigator: Kamil
Done: Set up project folders, dev log and Git.

## 8 Oct 2026 [15:30] (40 min)
Driver: Sandra
Navigator: Kamil
Done: Wrote Card class.

## 9 Oct 2026 [16:15] (2hr)
Driver: Kamil
Navigator: Sandra
Done: Wrote the first versions Player and Deck classes.

## 11 Oct 2026 [10:50] (35 min)
Driver: Sandra
Navigator: Kamil
Done: Deck: added index, size, getContents and null on an empty deck.

## 11 Oct 2026 [12:15] (45 min)
Driver: Sandra
Navigator: Kamil
Done: Player: added a null check on drawing, locked both decks (lower number first) and made takeTurn return a boolean.

## 11 Oct 2026 [17:40] (50 min)
Driver: Sandra
Navigator: Kamil
Done: Player: implemented Runnable and added the AtomicInteger winner.

## 12 Oct 2026 [02:45] (20 min)
Driver: Sandra
Navigator: Kamil
Done: CardGame: read and validate the number of players.

## 12 Oct 2026 [19:13] (30 min)
Driver: Sandra
Navigator: Kamil
Done: CardGame: read and validate the pack file.

## 13 Oct 2026 [18:05] (25 min)
Driver: Sandra
Navigator: Kamil
Done: CardGame: created the decks and players and dealt the cards.

## 14 Oct 2026 [20:50] (45 min)
Driver: Sandra
Navigator: Kamil
Done: CardGame: added playGame and the deck output files.

## 15 Oct 2026 [12:55] (15 min)
Driver: Sandra
Navigator: Kamil
Done: CardGame: added checkImmediateWin

## 15 Oct 2026 [17:20] (1hr 45 min)
Driver: Kamil
Navigator: Sandra
Done: Checked all the code and fixed problems with it (commit "Finalising code"). Then ran the game for first time and double checked everything worked (commit "Ran and double checked everything worked").

## 17 Oct 2026 [00:35] (45 min)
Driver: Sandra
Navigator: Kamil
Done: Added FIFO, empty deck, size, copy and thread tests to DeckTest class.

## 17 Oct 2026, approx. 12:48 (1hr)
Driver: Sandra
Navigator: Kamil
Done: Added detecting wins, hand size, discard rule and no turns after winning to PlayerTest.

## 18 Oct 2026 [14:45] (1hr 50 min)
Driver: Kamil
Navigator: Sandra
Done: Made tests for Card.java and CardGame.java in CardGameTest.java and CardTest.java. Then fixed behaviour tests in Player.

## Significant commits

### 2214228 (11 Oct 2026)
Made Player able to run on its own thread and added the shared winner box so all players can see when someone wins.

### 1caf98d (12 Oct 2026)
Added CardGame pack validation. It checks that the file exists, has 8n lines and that every line is a whole non-negative number.

### 33b8fa9 (14 Oct 2026)
CardGame now starts all the player threads, waits for them to finish then writes the deck files.

### f54311d (15 Oct 2026)
Ran the whole game for the first time, checked it against the spec and fixed the problems we found.

### 0519072 (18 Oct 2026)
Fixed the Player tests so they test the right behaviour.