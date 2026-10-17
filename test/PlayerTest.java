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

        player.addCard(new Card(5));
        player.addCard(new Card(5));
        player.addCard(new Card(5));
        player.addCard(new Card(5));

        assertTrue(player.hasWinningHand());
    }
}