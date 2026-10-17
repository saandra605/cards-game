import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerTest {

    @Test
    public void fourCardsOfTheSameValueIsAWinningHand() {
        Deck left = new Deck(1);
        Deck right = new Deck(2);
        Player player = new Player(1, left, right, new AtomicInteger(0));

        player.addCard(new Card(6));
        player.addCard(new Card(6));
        player.addCard(new Card(6));
        player.addCard(new Card(6));

        assertTrue(player.hasWinningHand());
    }

    @Test
    public void mixedHandIsNotAWinningHand() {
        Deck left =  new Deck(1);
        Deck right = new Deck(2);
        Player player = new Player(1, left, right, new AtomicInteger(0));

        player.addCard(new Card(6));
        player.addCard(new Card(6));
        player.addCard(new Card(6));
        player.addCard(new Card(8));
    }

    @Test
    public void startingHandWithThreeCardsIsRejected() {
        Deck left = new Deck(1);
        Deck right = new Deck(2);
        Player player = new Player(1, left, right, new AtomicInteger(0));

        List<Card> threeCards = new ArrayList<>();
        threeCards.add(new Card(1));
        threeCards.add(new Card(2));
        threeCards.add(new Card(3));

        try {
            player.setInitialHand(threeCards);
            fail("Error expected for a hand with 3 cards");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void playerDiscardsTheirNonPreferredCard() {
        Deck left = new Deck(1);
        Deck right = new Deck(2);
        Player player = new Player(1, left, right, new AtomicInteger(0));

        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(2));
        left.addToBottom(new Card(1));

        player.run();

        assertEquals(2, right.drawFromTop().getValue());
    }

    @Test
    public void playerStillHasFourCardsAfterATurn() {
        Deck left = new Deck(1);
        Deck right = new Deck(2);
        Player player = new Player(1, left, right, new AtomicInteger(0));

        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(2));
        left.addToBottom(new Card(1));

        assertEquals(4, player.getHand().size());
    }

    @Test
    public void playerTakesNoTurnsIfSomeoneHasAlreadyWon() {
        Deck left = new Deck(1);
        Deck right = new Deck(2);
        AtomicInteger winner = new AtomicInteger(4);
        Player player = new Player(1, left, right, winner);

        player.addCard(new Card(1));
        player.addCard(new Card(2));
        player.addCard(new Card(3));
        player.addCard(new Card(4));
        left.addToBottom(new Card(9));

        player.run();

        assertEquals(1, left.size());
        assertEquals(0, right.size());
        assertEquals(4, player.getHand().size());
    }
}