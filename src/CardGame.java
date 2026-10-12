import java.util.Scanner;

public class CardGame {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = readPlayerCount(in);
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
}