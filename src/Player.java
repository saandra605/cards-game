import java.util.ArrayList;
import java.util.List;

public class Player {

    private final int playerNumber;
    private final int preferredValue;
    private final List<Card> hand;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.preferredValue = playerNumber;
        this.hand = new ArrayList<>();
    }

    public synchronized void setInitialHand(List<Card> cards) {
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

    public synchronized void takeTurn(Deck leftDeck, Deck rightDeck) {
        hand.add(leftDeck.drawFromTop());

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
    }

    public synchronized int getPlayerNumber() {
        return playerNumber;
    }
}