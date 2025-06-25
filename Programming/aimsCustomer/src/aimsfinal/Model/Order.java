package aimsfinal.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private double totalAmount;
    private String status;
    private Timestamp orderDate;
    private List<OrderItem> orderItems;
    private boolean rushOrder;
    private double deliveryFee;
    private double vat;
    private double totalPayment;
    
    public Order() {
        this.orderItems = new ArrayList<>();
    }
    
    public Order(int orderId, int userId, double totalAmount, String status, Timestamp orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.orderItems = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    public boolean isRushOrder() { return rushOrder; }
    public void setRushOrder(boolean rushOrder) { this.rushOrder = rushOrder; }
    
    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
    
    public double getVat() { return vat; }
    public void setVat(double vat) { this.vat = vat; }
    
    public double getTotalPayment() { return totalPayment; }
    public void setTotalPayment(double totalPayment) { this.totalPayment = totalPayment; }

    // Additional method for GUI compatibility
    public int getId() {
        return orderId;
    }

    public void setId(int id) {
        this.orderId = id;
    }
}