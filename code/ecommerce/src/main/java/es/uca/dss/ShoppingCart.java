package es.uca.dss;
import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Product> items;
    public ShoppingCart() {
        items = new ArrayList<>();
    }
    public double getBalance() { 
        double balance = 0.0;
        for (int i = 0; i < items.size(); i++) {
            Product p = items.get(i);
            balance += p.getPrice();
        }
        return balance;
    }
    public void addItem(Product p) {
        items.add(p);
    }
    public void removeItem(Product p) throws ProductNotFoundException {
        try {
            items.remove(p);
        } catch (Exception e) {
            throw new ProductNotFoundException("Product not found");
        }
    }
    public int getItemCount() {
        return items.size();
    }
    public void empty() {
        items.clear();
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
  