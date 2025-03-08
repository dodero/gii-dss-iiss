import org.junit.*;
import static org.junit.Assert.*;

public class CreditCardTest {
    private String number;
    private String expirationDate;
    private String name;
    private int cvv;
    private CreditCard card;

    @Before
    public void setUp()
    {
        number = "1234567890123456";
        expirationDate = "01/23";
        name = "John Doe";
        cvv = 123;
        card = new CreditCard(number, expirationDate, name, cvv);

    }
    @Test
    public void testConstructor() {
        assertEquals("1234567890123456", card.getNumber());
        assertEquals("01/23", card.getExpirationDate());
        assertEquals("John Doe", card.getName());
        assertEquals(123, card.getCvv());
    }
        

    @Test(expected = IllegalArgumentException.class)
    public void ConstructorWithInvalidFormat() {
        CreditCard card = new CreditCard("1234567890123456", "2023/04", "John Doe", 123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructorWithInvalidExpirationDate() {
        CreditCard card = new CreditCard("1234567890123456", "14/2001", "John Doe", 123);
    }
    @Test
    public void getNumber() {
        assertEquals(number, card.getNumber());
    }

    @Test
    public void getExpirationDate()
    {
        assertEquals(expirationDate, card.getExpirationDate());
    }

    @Test
    public void getName()
    {
        assertEquals(name, card.getName());
    }

    @Test
    public void getCvv()
    {
        assertEquals(cvv, card.getCvv());
    }

}
