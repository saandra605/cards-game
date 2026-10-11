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

    public synchronized boolean takeTurn(Deck leftDeck, Deck rightDeck) {

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

                int discardIndex = -1;

                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i).getValue() != preferredValue) {
                        discardIndex = i;
                        break;
                    }
                }

                if (discardIndex == -1) {
                    discardIndex = 0;
                }

                rightDeck.addToBottom(hand.remove(discardIndex));
                return true;
            }
        }
    }

    public synchronized int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public void run() {

    }
}