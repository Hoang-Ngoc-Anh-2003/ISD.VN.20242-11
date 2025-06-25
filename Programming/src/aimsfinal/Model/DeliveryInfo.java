package aimsfinal.Model;

public class DeliveryInfo {
    private int deliveryId;
    private int orderId;
    private String receiverName;
    private String phone;
    private String address;
    private String province;
    private String city;
    private String district;
    private String deliveryMethod;
    private String deliveryStatus;
    private String email;
    private int rushDeliveryHours;
    
    public DeliveryInfo() {}
    
    public DeliveryInfo(int deliveryId, int orderId, String receiverName, String phone, 
                       String address, String province, String city, String district) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.receiverName = receiverName;
        this.phone = phone;
        this.address = address;
        this.province = province;
        this.city = city;
        this.district = district;
    }
    
    // Getters and Setters
    public int getDeliveryId() { return deliveryId; }
    public void setDeliveryId(int deliveryId) { this.deliveryId = deliveryId; }
    
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    
    public String getDeliveryMethod() { return deliveryMethod; }
    public void setDeliveryMethod(String deliveryMethod) { this.deliveryMethod = deliveryMethod; }
    
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getShippingMethod() { return deliveryMethod; }
    public void setShippingMethod(String shippingMethod) { this.deliveryMethod = shippingMethod; }
    
    public int getRushDeliveryHours() { return rushDeliveryHours; }
    public void setRushDeliveryHours(int rushDeliveryHours) { this.rushDeliveryHours = rushDeliveryHours; }
} 