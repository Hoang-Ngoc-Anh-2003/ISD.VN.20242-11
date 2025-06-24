package aimsfinal.Util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class VnpayCallbackServer {

    private HttpServer server;
    private final int port;
    private static final List<PaymentStatusListener> listeners = new ArrayList<>();

    public VnpayCallbackServer(int port) {
        this.port = port;
    }

    public void addListener(PaymentStatusListener listener) {
        listeners.add(listener);
    }

    public void start() throws IOException {
        if (server != null && server.getAddress() != null) {
            System.out.println("Callback server is already running on port " + port);
            return;
        }
        
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/vnpay_return", new VnpayReturnHandler());
        server.setExecutor(Executors.newSingleThreadExecutor()); // Runs in a background thread
        server.start();
        System.out.println("VNPay Callback Server started on port " + port);

        // Add a shutdown hook to stop the server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        if (server != null) {
            server.stop(0); // Stop immediately
            System.out.println("VNPay Callback Server stopped.");
        }
    }

    static class VnpayReturnHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Only handle GET requests
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            // Parse query parameters
            Map<String, String> params = new HashMap<>();
            String query = exchange.getRequestURI().getQuery();
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    if (idx > 0) {
                         String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                         String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                         params.put(key, value);
                    }
                }
            }
            
            String vnp_ResponseCode = params.getOrDefault("vnp_ResponseCode", "");
            boolean isSuccessful = "00".equals(vnp_ResponseCode);
            
            // Notify listeners
            if (isSuccessful) {
                for (PaymentStatusListener listener : listeners) {
                    listener.onPaymentSuccess(
                        params.getOrDefault("vnp_TxnRef", ""),
                        params.getOrDefault("vnp_Amount", ""),
                        params.getOrDefault("vnp_OrderInfo", "")
                    );
                }
            } else {
                 for (PaymentStatusListener listener : listeners) {
                    listener.onPaymentFailure(
                        params.getOrDefault("vnp_TxnRef", ""),
                        vnp_ResponseCode
                    );
                }
            }

            String htmlResponse = generateHtmlResponse(isSuccessful, params);
            
            sendResponse(exchange, 200, htmlResponse);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }

        private String generateHtmlResponse(boolean isSuccessful, Map<String, String> params) {
            String title = isSuccessful ? "Thanh toán thành công!" : "Thanh toán thất bại!";
            String cardHeaderBg = isSuccessful ? "bg-success" : "bg-danger";
            String icon = isSuccessful ? "✅" : "❌";
            String message = isSuccessful 
                ? "Đơn hàng của bạn đã được xử lý thành công.<br>Cảm ơn bạn đã mua sắm tại AIMS!" 
                : "Rất tiếc, giao dịch của bạn không thể hoàn tất.<br>Vui lòng thử lại hoặc liên hệ hỗ trợ.";

            StringBuilder details = new StringBuilder();
            details.append("Mã đơn hàng: <strong>").append(params.getOrDefault("vnp_TxnRef", "N/A")).append("</strong><br>");
            
            String amountStr = params.getOrDefault("vnp_Amount", "0");
            try {
                double amount = Double.parseDouble(amountStr) / 100;
                details.append("Số tiền: <strong>").append(String.format("%,.0f", amount)).append(" VNĐ</strong><br>");
            } catch(Exception e) {
                 details.append("Số tiền: <strong>").append(amountStr).append("</strong><br>");
            }
            
            details.append("Nội dung: <strong>").append(params.getOrDefault("vnp_OrderInfo", "N/A")).append("</strong><br>");
            details.append("Mã phản hồi: <strong>").append(params.getOrDefault("vnp_ResponseCode", "N/A")).append("</strong><br>");
            details.append("Thời gian: <strong>").append(new java.util.Date()).append("</strong>");

            return "<!DOCTYPE html>"
                + "<html><head><title>AIMS - VNPay Response</title>"
                + "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css' rel='stylesheet'>"
                + "<style> .icon { font-size: 4rem; } body { text-align: center; padding: 40px 0; background: #EBF0F5; } </style>"
                + "</head><body>"
                + "<div class='card' style='background: white; padding: 60px; border-radius: 4px; box-shadow: 0 2px 3px #C8D0D8; display: inline-block; margin: 0 auto;'>"
                + "  <div class='icon mb-4' style='border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;'>"
                + "    <span style='color: " + (isSuccessful ? "#9ABC66" : "#D9534F") + "; font-size: 100px; line-height: 200px; margin-left:-15px;'>"+ (isSuccessful ? "✓" : "✗") +"</span>"
                + "  </div>"
                + "  <h1 style='color: " + (isSuccessful ? "#88B04B" : "#D9534F") + "; font-weight: 900; font-size: 40px; margin-bottom: 10px;'>" + title + "</h1>"
                + "  <p style='color: #404F5E; font-size:20px; margin: 0;'>Cửa sổ này sẽ tự động đóng sau vài giây.<br/>Nếu không, vui lòng đóng nó theo cách thủ công.</p>"
                + "  <div class='alert alert-secondary text-start mt-4'>" + details.toString() + "</div>"
                + "  <button class='btn btn-primary mt-4' onclick='closeWindow()'>Đóng ngay</button>"
                + "</div>"
                + "<script>"
                + "function closeWindow() { window.top.close(); }"
                + "setTimeout(closeWindow, 5000);"
                + "</script>"
                + "</body></html>";
        }
    }
}