package aimsfinal.Controller;

import aimsfinal.DAO.DeliveryInfoDAO;
import aimsfinal.DAO.OrderDAO;
import aimsfinal.DAO.OrderItemDAO;
import aimsfinal.DAO.PaymentDAO;
import aimsfinal.Model.CartItem;
import aimsfinal.Model.DeliveryInfo;
import aimsfinal.Model.Order;
import aimsfinal.Model.OrderItem;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private DeliveryInfoDAO deliveryInfoDAO;
    private PaymentDAO paymentDAO;

    public OrderController() {
        // Default constructor for GUI compatibility
        try {
            this.orderDAO = new OrderDAO();
            this.orderItemDAO = new OrderItemDAO();
            this.deliveryInfoDAO = new DeliveryInfoDAO();
            this.paymentDAO = new PaymentDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OrderController(Connection connection) {
        this.orderDAO = new OrderDAO(connection);
        this.orderItemDAO = new OrderItemDAO(connection);
        this.deliveryInfoDAO = new DeliveryInfoDAO(connection);
        this.paymentDAO = new PaymentDAO(connection);
    }

    public int placeOrder(Order order, List<OrderItem> items, DeliveryInfo deliveryInfo) throws SQLException {
        orderDAO.insertOrder(order);
        int orderId = order.getOrderId();
        for (OrderItem item : items) {
            item.setOrderId(orderId);
            orderItemDAO.insertOrderItem(item);
        }
        deliveryInfo.setOrderId(orderId);
        deliveryInfoDAO.insertDeliveryInfo(deliveryInfo);
        return orderId;
    }

    public boolean checkOrderValidity(Order order) {
        return order.getUserId() > 0 && order.getTotalAmount() > 0;
    }

    public double calculateShippingFee(DeliveryInfo info) {
        double fee = 20000;
        if (!info.getProvince().equalsIgnoreCase("Hà Nội")) fee += 15000;
        return fee;
    }

    public void sendEmailForCustomer(String email, String subject, String content) {
        System.out.println("Gửi email tới: " + email);
        System.out.println("Tiêu đề: " + subject);
        System.out.println("Nội dung: " + content);
    }

    public boolean createOrder(Order order) {
        if (validateOrder(order)) {
            try {
                orderDAO.insertOrder(order);
                for (OrderItem item : order.getOrderItems()) {
                    item.setOrderId(order.getOrderId());
                    orderItemDAO.insertOrderItem(item);
                }
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public Order getOrderById(int id) {
        try {
            Order order = orderDAO.getOrderById(id);
            if (order != null) {
                List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(id);
                order.setOrderItems(items);
            }
            return order;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Order> getAllOrders() {
        try {
            List<Order> orders = orderDAO.getAllOrders();
            for (Order order : orders) {
                List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(order.getOrderId());
                order.setOrderItems(items);
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean updateOrder(Order order) {
        if (validateOrder(order)) {
            try {
                // TODO: Implement update order functionality
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean deleteOrder(int id) {
        try {
            // Xóa các order items trước
            orderItemDAO.deleteByOrderId(id);
            // Xóa delivery info
            deliveryInfoDAO.deleteByOrderId(id);
            // Xóa order
            return orderDAO.deleteOrder(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        try {
            List<Order> orders = orderDAO.getOrdersByUserId(userId);
            for (Order order : orders) {
                List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(order.getOrderId());
                order.setOrderItems(items);
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    public boolean updatePaymentStatus(int orderId, String status) throws SQLException {
        // The payment status is a column in the 'orders' table.
        // It should be updated via OrderDAO, not PaymentDAO which doesn't exist in the original design.
        return orderDAO.updateOrderStatus(orderId, status);
    }

    private boolean validateOrder(Order order) {
        if (order == null) {
            return false;
        }
        if (order.getUserId() <= 0) {
            return false;
        }
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean placeOrder(int userId, List<CartItem> cartItems, String receiver, String phone, String email, String address, String province, String shippingMethod, double shippingFee, boolean isRush) {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.createOrder(userId, cartItems, receiver, phone, email, address, province, shippingMethod, shippingFee, isRush);
    }

    public int placeCompleteOrder(Order order, List<CartItem> cartItems, DeliveryInfo deliveryInfo) throws SQLException {
        return orderDAO.createCompleteOrder(order, cartItems, deliveryInfo);
    }

    // Có thể bổ sung các hàm kiểm tra hợp lệ, tính phí vận chuyển, gửi email, v.v. theo sơ đồ
} 