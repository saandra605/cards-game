import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void storesItsValue() {
        Card card = new Card(6);

        assertEquals(6, card.getValue());
    }

    @Test
    public void displaysItsValueAsText() {
        Card card = new Card(6);

        assertEquals("6", card.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNegativeValues() {
        new Card(-1);
    }
}