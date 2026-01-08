package es.uca.dss;
import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {
    @Test
    public void testProductCreation() {
        Product p = new Product("Laptop", 1200.00);
        assertEquals("Laptop", p.getName());
        assertEquals(1200.00, p.getPrice(), 0.01);
    }
    @Test
    public void testProductEquality() {
        Product p1 = new Product("Phone", 800.00);
        Product p2 = new Product("Phone", 900.00);
        assertEquals(p1, p2);
    }
}