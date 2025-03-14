package es.uca.dss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShoppingCartTest {
  private ShoppingCart bookCart;
  private Product defaultBook;
  
  @Before
  public void setUp() {
      bookCart = new ShoppingCart();
      defaultBook = new Product("Extreme Programming", 23.95);
      bookCart.addItem(defaultBook);
  }

  @After
  public void tearDown() {
      bookCart = null;
  }

  @Test
  public void testEmpty() {
      bookCart.empty();
      assertTrue(bookCart.isEmpty());
  }

  @Test
  public void testProductAdd() {
      Product book = new Product("Refactoring", 53.95);
      bookCart.addItem(book);
      double expectedBalance = defaultBook.getPrice() + book.getPrice();
      assertEquals(expectedBalance, bookCart.getBalance(), 0.0);
      assertEquals(2, bookCart.getItemCount());
  }

  @Test
  public void testProductRemove() {
      try {
          bookCart.removeItem(defaultBook);
          assertEquals(0, bookCart.getItemCount());
          assertEquals(0.0, bookCart.getBalance(), 0.0);
      } catch (ProductNotFoundException e) {
          fail("Product not found");
      }
  }
  
  @Test //(expected = ProductNotFoundException.class)
  public void testProductNotFound() throws ProductNotFoundException {
      Product book = new Product("Ender's Game", 4.95);
      try {
        bookCart.removeItem(book);
      } catch (ProductNotFoundException e) {
         // Test passes if correct exception is thrown
         assertNotNull(e);
      } catch (Exception e) {
        // Test fails if any other exception is thrown
        fail("Should raise a ProductNotFoundException");
      }
  }

}
