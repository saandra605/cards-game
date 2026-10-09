import java.util.ArrayDeque;
import java.util.Deque;

public class Deck {

    private final Deque<Card> cards;

    public Deck() {
        cards = new ArrayDeque<>();
    }

    public synchronized void addToBottom(Card card) {
        cards.addLast(card);
    }

    public synchronized Card drawFromTop() {
        return cards.removeFirst();
    }
}