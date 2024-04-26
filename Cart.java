import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Item> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        try {
            items.add(item);
            System.out.println("Item added to cart: " + item.getName());
        } catch (NullPointerException e) {
            System.err.println("Error: Null item cannot be added to cart.");
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        try {
            for (Item item : items) {
                totalPrice += item.getPrice() * item.getAmount();
            }
        } catch (NullPointerException e) {
            System.err.println("Error: Null item found while calculating total price.");
        }
        return totalPrice;
    }

    public void removeItem(Item item) {
        try {
            if (items.remove(item)) {
                System.out.println("Item removed from cart: " + item.getName());
            } else {
                System.out.println("Item not found in cart.");
            }
        } catch (NullPointerException e) {
            System.err.println("Error: Null item cannot be removed from cart.");
        }
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            System.out.println("Items in cart:");
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }


    public void clearCart() {
        items.clear();
        System.out.println("Cart cleared.");
    }
}
