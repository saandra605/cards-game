import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Player implements Runnable {

    private final int playerNumber;
    private final int preferredValue;
    private final List<Card> hand;
    private final Deck leftDeck;
    private final Deck rightDeck;
    private final AtomicInteger winner;

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck, AtomicInteger winner) {
        this.playerNumber = playerNumber;
        this.preferredValue = playerNumber;
        this.hand = new ArrayList<>();
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.winner = winner;
    }

    public synchronized void setInitialHand(List<Card> cards) {
        if (cards.size() != 4) {
            throw new IllegalArgumentException("A hand must have exactly 4 cards");
        }

        hand.clear();
        hand.addAll(cards);
    }

    public synchronized void addCard(Card card) {
        hand.add(card);
    }

    public synchronized List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public synchronized boolean hasWinningHand() {
        if (hand.size() != 4) {
            return false;
        }

        int firstValue = hand.get(0).getValue();

        for (Card card : hand) {
            if (card.getValue() != firstValue) {
                return false;
            }
        }

        return true;
    }

    public synchronized int getPlayerNumber() {
        return playerNumber;
    }

    private synchronized String handText() {
        StringBuilder text = new StringBuilder();

        for (Card card : hand) {
            if (text.length() > 0) {
                text.append(" ");
            }
            text.append(card);
        }

        return text.toString();
    }

    private synchronized boolean takeTurn(PrintWriter output) {
        Deck first;
        Deck second;

        if (leftDeck.getIndex() < rightDeck.getIndex()) {
            first = leftDeck;
            second = rightDeck;
        } else {
            first = rightDeck;
            second = leftDeck;
        }

        synchronized (first) {
            synchronized (second) {
                Card drawn = leftDeck.drawFromTop();

                if (drawn == null) {
                    return false;
                }

                hand.add(drawn);
                output.println("player " + playerNumber + " draws a " + drawn
                        + " from deck " + leftDeck.getIndex());

                int discardIndex = 0;

                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i).getValue() != preferredValue) {
                        discardIndex = i;
                        break;
                    }
                }

                Card discarded = hand.remove(discardIndex);
                rightDeck.addToBottom(discarded);

                output.println("player " + playerNumber + " discards a " + discarded
                        + " to deck " + rightDeck.getIndex());
                output.println("player " + playerNumber + " current hand is " + handText());

                return true;
            }
        }
    }

    @Override
    public void run() {
        String fileName = "player" + playerNumber + "_output.txt";

        try (PrintWriter output = new PrintWriter(fileName)) {
            output.println("player " + playerNumber + " initial hand " + handText());

            while (winner.get() == 0) {
                if (!takeTurn(output)) {
                    break;
                }

                if (hasWinningHand() && winner.compareAndSet(0, playerNumber)) {
                    System.out.println("Player " + playerNumber + " wins");
                }
            }

            int winningPlayer = winner.get();

            if (winningPlayer == playerNumber) {
                output.println("player " + playerNumber + " wins");
                output.println("player " + playerNumber + " exits");
                output.println("player " + playerNumber + " final hand: " + handText());
            } else {
                output.println("player " + winningPlayer + " has informed player "
                        + playerNumber + " that player " + winningPlayer + " has won");
                output.println("player " + playerNumber + " exits");
                output.println("player " + playerNumber + " hand: " + handText());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not write " + fileName);
        }
    }
}