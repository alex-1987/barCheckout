package fl.alexanderboesch.barcheckout;

import androidx.annotation.NonNull;

public class Drink {
    private long id;
    private String name;
    private double priceNormal;
    private double priceEmployee;

    private int quantity;
    private int imageResource;
    private String imagePath;

    public Drink(String name, double priceNormal, double priceEmployee, int quantity, int id, String imagePath) {
        this.id = id;
        this.name = name;
        this.priceNormal = priceNormal;
        this.priceEmployee = priceEmployee;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceNormal() {
        return priceNormal;
    }

    public void setPriceNormal(double priceNormal) {
        this.priceNormal = priceNormal;
    }

    public double getPriceEmployee() {
        return priceEmployee;
    }

    public void setPriceEmployee(double priceEmployee) {
        this.priceEmployee = priceEmployee;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public String toString() {
        return "Drink ID:" + id + " Name:" + name + " quantity:" + quantity + " image: " + imagePath;
    }
}
