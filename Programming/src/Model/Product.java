package Model;

public abstract class Product {
    protected int productId;
    protected String title;
    protected String category;
    protected double value;
    protected double price;
    protected String barcode;
    protected String description;
    protected int quantity;
    protected String weight;
    protected String dimensions;
    protected String warehouseEntryDate;

    public Product(){
    }

    public Product(int productId, String title, String category, double value, double price, String barcode,
            String description, int quantity, String weight, String dimensions, String warehouseEntryDate) {
        this.productId = productId;
        this.title = title;
        this.category = category;
        this.value = value;
        this.price = price;
        this.barcode = barcode;
        this.description = description;
        this.quantity = quantity;
        this.weight = weight;
        this.dimensions = dimensions;
        this.warehouseEntryDate = warehouseEntryDate;
    }

    // Getters & Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {  
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getWarehouseEntryDate() {
        return warehouseEntryDate;
    }

    public void setWarehouseEntryDate(String warehouseEntryDate) {
        this.warehouseEntryDate = warehouseEntryDate;
    }

    public String getSellingPrice() {
        return String.valueOf(value);
    }

    public void setSellingPrice(String sellingPrice) {
        this.value = Double.parseDouble(sellingPrice);
    }

    public String getImportPrice() {
        return String.valueOf(price);
    }

    public void setImportPrice(String importPrice) {
        this.price = Double.parseDouble(importPrice);
    }
}
