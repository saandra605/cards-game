import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CardGame {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = readPlayerCount(in);
        List<Card> pack = readPack(in, n);
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
}