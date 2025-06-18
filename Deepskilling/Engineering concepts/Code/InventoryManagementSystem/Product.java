package InventoryManagementSystem;

import java.io.Serializable;

public class Product implements Serializable {
    private String productId, productName;
    private int quantity;
    private float price;

    public Product(String pid, String name, int q, float p) {
        productId = pid;
        productName = name;
        quantity = q;
        price = p;
    }

    public String getProdID() { return productId; }

    public String getName() { return productName; }
    public void setName(String newName) { productName = newName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int newQuantity) { quantity = newQuantity; }

    public float getPrice() { return price; }
    public void setPrice(float newPrice) { price = newPrice; }

    public String toString() {
        return String.format("{Name: %s, Price: %.2f, Quantity: %d}", productName, price, quantity);
    }
}