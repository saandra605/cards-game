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

        assertFalse(player.hasWinningHand());
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
        AtomicInteger winner = new AtomicInteger(0);
        Player player = new Player(1, left, right, winner);

        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(2));
        left.addToBottom(new Card(1));

        player.run();

        assertEquals(4, player.getHand().size());
        assertEquals(1, winner.get());
        assertEquals(0, left.size());
        assertEquals(1, right.size());
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

    @Test
    public void immediateWinIsCheckedByAllPlayerThreadsBeforeTurns() throws InterruptedException {
        List<Deck> decks = CardGame.createDecks(2);
        AtomicInteger winner = new AtomicInteger(0);
        List<Player> players = CardGame.createPlayers(2, decks, winner);
        Player winningPlayer = players.get(0);
        Player otherPlayer = players.get(1);

        winningPlayer.addCard(new Card(7));
        winningPlayer.addCard(new Card(7));
        winningPlayer.addCard(new Card(7));
        winningPlayer.addCard(new Card(7));
        otherPlayer.addCard(new Card(1));
        otherPlayer.addCard(new Card(2));
        otherPlayer.addCard(new Card(3));
        otherPlayer.addCard(new Card(4));
        decks.get(0).addToBottom(new Card(8));
        decks.get(1).addToBottom(new Card(9));

        CardGame.playGame(players);

        assertEquals(1, winner.get());
        assertTrue(winningPlayer.hasWinningHand());
        assertEquals(4, otherPlayer.getHand().size());
        assertEquals(1, decks.get(0).size());
        assertEquals(1, decks.get(1).size());
    }
}