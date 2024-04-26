public class Item {
    private String name;
    private double price;
    private String category;
    private String description;
    private int amount=1;

    public Item(String name, double price, String category, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
    }
    public int getAmount() {
    	return amount;
    }
    public void setAmount(int x) {
    	this.amount=x;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
    	this.price=price;
        
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " - $" + price + " - Category: " + category + " - Description: " + description;
    }
}
