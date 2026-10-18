import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class CardGameTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void acceptsPackWithEightNonNegativeValuesForOnePlayer() throws IOException {
        File packFile = createPack("1", "1", "1", "1", "2", "3", "4", "5");

        List<Card> pack = CardGame.loadPack(packFile.getAbsolutePath(), 1);

        assertNotNull(pack);
        assertEquals(8, pack.size());
        assertEquals(1, pack.get(0).getValue());
        assertEquals(5, pack.get(7).getValue());
    }

    @Test
    public void rejectsPackWithWrongNumberOfLines() throws IOException {
        File packFile = createPack("1", "2", "3");

        assertNull(CardGame.loadPack(packFile.getAbsolutePath(), 1));
    }

    @Test
    public void rejectsPackWithNonNumericValue() throws IOException {
        File packFile = createPack("1", "1", "1", "1", "2", "3", "4", "x");

        assertNull(CardGame.loadPack(packFile.getAbsolutePath(), 1));
    }

    @Test
    public void rejectsPackWithNegativeValue() throws IOException {
        File packFile = createPack("1", "1", "1", "1", "2", "3", "4", "-5");

        assertNull(CardGame.loadPack(packFile.getAbsolutePath(), 1));
    }

    @Test
    public void retriesUntilPlayerCountIsPositive() {
        Scanner input = new Scanner("0\n2\n");

        assertEquals(2, CardGame.readPlayerCount(input));
    }

    @Test
    public void dealsCardsToPlayersAndDecksInRoundRobinOrder() {
        int playerCount = 2;
        List<Card> pack = new ArrayList<>();

        for (int value = 0; value < 16; value++) {
            pack.add(new Card(value));
        }

        List<Deck> decks = CardGame.createDecks(playerCount);
        List<Player> players = CardGame.createPlayers(
                playerCount, decks, new AtomicInteger(0));

        CardGame.deal(pack, players, decks, playerCount);

        assertCardValues(players.get(0).getHand(), 0, 2, 4, 6);
        assertCardValues(players.get(1).getHand(), 1, 3, 5, 7);
        assertCardValues(decks.get(0).getContents(), 8, 10, 12, 14);
        assertCardValues(decks.get(1).getContents(), 9, 11, 13, 15);
    }

    @Test
    public void detectsAnImmediateWinningHand() {
        List<Deck> decks = CardGame.createDecks(1);
        AtomicInteger winner = new AtomicInteger(0);
        List<Player> players = CardGame.createPlayers(1, decks, winner);
        Player player = players.get(0);

        player.addCard(new Card(7));
        player.addCard(new Card(7));
        player.addCard(new Card(7));
        player.addCard(new Card(7));

        CardGame.checkImmediateWin(players, winner);

        assertEquals(1, winner.get());
    }

    private File createPack(String... values) throws IOException {
        File packFile = temporaryFolder.newFile("pack.txt");

        try (PrintWriter writer = new PrintWriter(packFile)) {
            for (String value : values) {
                writer.println(value);
            }
        }

        return packFile;
    }

    private void assertCardValues(List<Card> cards, int... values) {
        assertEquals(values.length, cards.size());

        for (int index = 0; index < values.length; index++) {
            assertEquals(values[index], cards.get(index).getValue());
        }
    }
}