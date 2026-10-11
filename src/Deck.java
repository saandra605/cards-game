import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;
import java.util.List;

public class Deck {

    private final int index;
    private final Deque<Card> cards;

    public Deck(int index) {
        this.index = index;
        this.cards = new ArrayDeque<>();
    }

    public int getIndex() {
        return index;
    }

    public synchronized void addToBottom(Card card) {
        cards.addLast(card);
    }

    public synchronized Card drawFromTop() {
        return cards.pollFirst();
    }

    public synchronized int size() {
        return cards.size();
    }

    public synchronized List<Card> getContents() {
        return new ArrayList<> (cards);
    }
}