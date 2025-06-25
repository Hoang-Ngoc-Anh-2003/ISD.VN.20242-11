package aimsfinal.Model;

public class OrderSummary {
    private double subtotal;
    private double shippingFee;
    private double vat;
    private double total;

    // Getters
    public double getSubtotal() { return subtotal; }
    public double getShippingFee() { return shippingFee; }
    public double getVat() { return vat; }
    public double getTotal() { return total; }

    // Setters
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }
    public void setVat(double vat) { this.vat = vat; }
    public void setTotal(double total) { this.total = total; }
}