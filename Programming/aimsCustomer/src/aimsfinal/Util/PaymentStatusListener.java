package aimsfinal.Util;

public interface PaymentStatusListener {
    /**
     * Called when a VNPay transaction is successfully processed.
     * @param orderId The ID of the order that was paid.
     * @param amount The amount paid.
     * @param orderInfo The description of the order.
     */
    void onPaymentSuccess(String orderId, String amount, String orderInfo);

    /**
     * Called when a VNPay transaction fails.
     * @param orderId The ID of the order.
     * @param errorCode The error code from VNPay.
     */
    void onPaymentFailure(String orderId, String errorCode);
}
