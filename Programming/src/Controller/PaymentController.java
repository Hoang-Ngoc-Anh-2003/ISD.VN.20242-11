package aimsfinal.Controller;

import aimsfinal.DAO.PaymentDAO;
import aimsfinal.Model.Payment;
import java.sql.SQLException;
import java.util.List;

public class PaymentController {
    private PaymentDAO paymentDAO;

    public PaymentController() {
        this.paymentDAO = new PaymentDAO();
    }

    public boolean createPayment(Payment payment) {
        if (validatePayment(payment)) {
            try {
                return paymentDAO.create(payment);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public Payment getPaymentById(int id) {
        try {
            return paymentDAO.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Payment> getAllPayments() {
        try {
            return paymentDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    public boolean updatePayment(Payment payment) {
        if (validatePayment(payment)) {
            try {
                return paymentDAO.update(payment);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean deletePayment(int id) {
        try {
            return paymentDAO.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Payment getPaymentByOrderId(int orderId) {
        try {
            return paymentDAO.getByOrderId(orderId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean validatePayment(Payment payment) {
        if (payment == null) {
            return false;
        }
        if (payment.getOrderId() <= 0) {
            return false;
        }
        if (payment.getAmount() <= 0) {
            return false;
        }
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().trim().isEmpty()) {
            return false;
        }
        return true;
    }
} 