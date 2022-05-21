package hanu.a2_1901040197.models;

public class Product {
    private long id;
    private String thumbnail;
    private String name;
    private long unitPrice;
    private long quantity;

    // data from database
    public Product(long id, String thumbnail, String name, long unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }
    public Product(long id, String thumbnail, String name, long unitPrice, long quantity) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }
    // data from forms
    public Product(String thumbnail, String name, long unitPrice) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
