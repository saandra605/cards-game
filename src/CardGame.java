import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.PrintWriter;

public class CardGame {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = readPlayerCount(in);
        List<Card> pack = readPack(in, n);

        AtomicInteger winner = new AtomicInteger(0);
        List<Deck> decks = createDecks(n);
        List<Player> players = createPlayers(n, decks, winner);
        deal(pack, players, decks, n);
        checkImmediateWin(players, winner);
        playGame(players);
        writeDeckFiles(decks);
    }

    public static int readPlayerCount(Scanner in) {
        int n = 0;
        boolean valid = false;

        while (valid == false) {
            System.out.println("Please enter the number of players:");
            String line = in.nextLine();
            line = line.trim();

            try {
                n = Integer.parseInt(line);

                if (n > 0) {
                    valid = true;
                } else {
                    System.out.println("The number of players must be 1 or more.");
                }
            } catch (NumberFormatException e) {
                System.out.println("That is not a whole number. Please try again.");
            }
        }
        return n;
    }

    public static List<Card> readPack(Scanner in, int n) {
        List<Card> pack = null;

        while (pack == null) {
            System.out.println("Please enter the location of the pack file:");
            String path = in.nextLine();
            path = path.trim();
            pack = loadPack(path, n);
        }
        return pack;
    }

    public static List<Card> loadPack(String path, int n) {
        List<String> lines = new ArrayList<>();

        try {
            Scanner fileScanner = new Scanner(new File(path));
            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("That file could not be found. Please try again");
            return null;
        }

        if (lines.size() != 8 * n) {
            System.out.println("The pack must have exactly " + (8 * n) + " lines.");
            return null;
        }

        List<Card> cards = new ArrayList<>();
        for (String text : lines) {
            int value;
            try {
                value = Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                System.out.println("Every line must be a whole number, with no blank lines.");
                return null;
            }

            if (value < 0) {
                System.out.println("The pack contains a negative number.");
                return null;
            }

            cards.add(new Card(value));
        }
        return cards;
    }

    public static List<Deck> createDecks(int n) {
        List<Deck> decks = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            decks.add(new Deck(i));
        }
        return decks;
    }

    public static List<Player> createPlayers(int n, List<Deck> decks, AtomicInteger winner) {
        List<Player> players = new ArrayList<>();

        for (int i = 1; i <=n; i++) {
            Deck left = decks.get(i - 1);
            Deck right;

            if (i == n) {
                right = decks.get(0);
            } else {
                right = decks.get(i);
            }

            players.add(new Player(i, left, right, winner));
        }
        return players;
    }

    public static void deal(List<Card> pack, List<Player> players, List<Deck> decks, int n) {
        int nextCard = 0;

        int playerPosition = 0;
        for (int count = 0; count < 4 * n; count++) {
            players.get(playerPosition).addCard(pack.get(nextCard));
            nextCard++;
            playerPosition++;
            if (playerPosition == n) {
                playerPosition = 0;
            }
        }

        int deckPosition = 0;
        while (nextCard < pack.size()) {
            decks.get(deckPosition).addToBottom(pack.get(nextCard));
            nextCard++;
            deckPosition++;
            if (deckPosition == n) {
                deckPosition = 0;
            }
        }
    }

    public static void playGame(List<Player> players) {
        List<Thread> threads = new ArrayList<>();

        for (Player player:players) {
            Thread thread = new Thread(player);
            threads.add(thread);
        }

        for (Thread thread:threads) {
            thread.start();
        }

        for (Thread thread:threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("The game was interrupted.");
            }
        }
    }

    public static void writeDeckFiles(List<Deck> decks) {
        for (Deck deck:decks) {
            int number = deck.getIndex();
            List<Card> cards = deck.getContents();

            String cardsText = "";
            for (Card card:cards) {
                cardsText = cardsText + card + " ";
            }
            cardsText = cardsText.trim();

            String fileName = "deck" + number + "_output.txt";

            try {
                PrintWriter writer = new PrintWriter(fileName);
                writer.println("deck" + number + " contents: " + cardsText);
                writer.close();
            } catch (FileNotFoundException e) {
                System.out.println("Could not write " + fileName);
            }
        }
    }

    public static void checkImmediateWin(List<Player> players, AtomicInteger winner) {
    for (Player player : players) {
        if (player.hasWinningHand()
                && winner.compareAndSet(0, player.getPlayerNumber())) {
            System.out.println("Player " + player.getPlayerNumber() + " wins");
            return;
        }
    }
}
}
