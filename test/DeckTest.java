import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class DeckTest {

    @Test
    public void drawsCardsInTheOrderTheyWereAdded() {
        Deck deck = new Deck(1);

        deck.addToBottom(new Card(1));
        deck.addToBottom(new Card(2));
        deck.addToBottom(new Card(3));

        Card first = deck.drawFromTop();
        Card second = deck.drawFromTop();
        Card third = deck.drawFromTop();

        assertEquals(1, first.getValue());
        assertEquals(2, second.getValue());
        assertEquals(3, third.getValue());
    }

    @Test
    public void drawingFromAnEmptyDeckReturnsNull() {
        Deck deck = new Deck(1);

        Card drawn = deck.drawFromTop();

        assertNull(drawn);
    }

    @Test
    public void sizeGoesUpWhenAddingAndDownWhenDrawing() {

        Deck deck = new Deck(1);
        deck.addToBottom(new Card(4));
        deck.addToBottom(new Card(6));
        assertEquals(2, deck.size());

        deck.drawFromTop();
        assertEquals(1, deck.size());
    }

    @Test
    public void changingTheReturnedListDoesNotChangeTheDeck() {
        Deck deck = new Deck(1);
        deck.addToBottom(new Card(5));

        List<Card> copy = deck.getContents();
        copy.add(new Card(10));

        assertEquals(1, deck.size());

    }

    @Test
    public void addingTwoThreadsAtOnceLostNoCards() throws InterruptedException {
        Deck deck = new Deck(1);

        Thread first = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                deck.addToBottom(new Card(i));
            }
        });

        Thread second = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                deck.addToBottom(new Card(i));
            }
        });

        first.start();
        second.start();

        first.join();
        second.join();

        assertEquals(2000, deck.size());
    }

}
