package es.uca.dss;

public class Product {
    private String name;
    private double price;
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            Product p = (Product) obj;
            return p.getName().equals(name);
        }
        return false;
    }
    public int hashCode() {
        return name.hashCode();
    }
    public String toString() {
        return name + ":" + price;
    }
}
