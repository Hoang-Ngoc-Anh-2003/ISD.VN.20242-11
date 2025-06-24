package aimsfinal.Model;

import java.sql.Timestamp;

public class RushOrder {
    private int rushOrderId;
    private int userId;
    private int orderId;
    private double totalAmount;
    private Timestamp deliveryTime;
    private Timestamp deliveryDate;
    private String rushStatus;
    private String deliveryAddress;
    
    public RushOrder() {}
    
    public RushOrder(int rushOrderId, int userId, int orderId, double totalAmount, 
                    Timestamp deliveryTime, Timestamp deliveryDate) {
        this.rushOrderId = rushOrderId;
        this.userId = userId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.deliveryTime = deliveryTime;
        this.deliveryDate = deliveryDate;
    }
    
    // Getters and Setters
    public int getRushOrderId() { return rushOrderId; }
    public void setRushOrderId(int rushOrderId) { this.rushOrderId = rushOrderId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public Timestamp getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(Timestamp deliveryTime) { this.deliveryTime = deliveryTime; }
    
    public Timestamp getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(Timestamp deliveryDate) { this.deliveryDate = deliveryDate; }
    
    public String getRushStatus() { return rushStatus; }
    public void setRushStatus(String rushStatus) { this.rushStatus = rushStatus; }
    
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    // Additional methods for GUI compatibility
    public void setCustomerName(String customerName) {
        // This method is for compatibility, customer name is not stored in RushOrder
    }

    public void setRushFee(double rushFee) {
        // Rush fee is part of totalAmount, so this method is for compatibility
    }
} 