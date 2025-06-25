<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, java.net.*, java.nio.charset.*, javax.crypto.*, javax.crypto.spec.*, java.sql.*"%>

<%!
    // VNPAY's Secret Key
    private static final String VNP_SECRET_KEY = "3QNH2I3BZ37R6Q6B6CYX8Q6YL3HFXAKW"; // NOTE: Use your actual secret key

    /**
     * Calculates HMAC-SHA512 hash.
     * This method must be inside a JSP declaration block.
     */
    public String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException("Key or data for HMAC is null");
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            final SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac512.init(secretKeySpec);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace(); // Log the error in a real app
            return "";
        }
    }

    /**
     * Creates a hash from a map of fields.
     * This method must be inside a JSP declaration block.
     */
    public String hashAllFields(Map<String, String> fields) {
        // Sort fields by name
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                try {
                     hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch(Exception e) { e.printStackTrace(); }
               
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }
        return hmacSHA512(VNP_SECRET_KEY, hashData.toString());
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>AIMS - VNPay Response</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .result-card {
            border: none;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .success-icon {
            font-size: 4rem;
            color: #28a745;
        }
        .failure-icon {
            font-size: 4rem;
            color: #dc3545;
        }
    </style>
</head>
<body>
<%
    // This block is for request-time logic (a JSP scriptlet)
    
    // 1. Collect all parameters from VNPAY into a map
    Map<String, String> fields = new HashMap<>();
    for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
        String fieldName = params.nextElement();
        String fieldValue = request.getParameter(fieldName);
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
            fields.put(fieldName, fieldValue);
        }
    }

    // 2. Remove hash fields from the map before validating
    String vnp_SecureHash = fields.remove("vnp_SecureHash");
    fields.remove("vnp_SecureHashType"); // Not needed for validation

    // 3. Calculate the signature and validate it
    String calculatedSignature = hashAllFields(fields);
    boolean isValidSignature = calculatedSignature.equals(vnp_SecureHash);

    // 4. Get other important fields for display
    String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
    String vnp_TxnRef = request.getParameter("vnp_TxnRef");
    String vnp_Amount = request.getParameter("vnp_Amount");
    String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
    boolean isSuccessful = "00".equals(vnp_ResponseCode);
    String amountFormatted = "";
    if (vnp_Amount != null && !vnp_Amount.isEmpty()) {
        try {
            double amount = Double.parseDouble(vnp_Amount) / 100;
            amountFormatted = String.format("%,.0f", amount);
        } catch (NumberFormatException e) {
            amountFormatted = vnp_Amount;
        }
    }
%>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card result-card">
                    <% if (isValidSignature) { %>
                        <% if (isSuccessful) { %>
                            <div class="card-header bg-success text-white text-center">
                                <h4 class="mb-0">🎉 Thanh toán thành công!</h4>
                            </div>
                            <div class="card-body p-5 text-center">
                                <div class="success-icon mb-4">✅</div>
                                <h5 class="card-title text-success">Giao dịch VNPay hoàn tất</h5>
                                <p class="card-text">
                                    Đơn hàng của bạn đã được xử lý thành công.<br>
                                    Cảm ơn bạn đã mua sắm tại AIMS!
                                </p>
                        <% } else { %>
                            <div class="card-header bg-danger text-white text-center">
                                <h4 class="mb-0">❌ Thanh toán thất bại!</h4>
                            </div>
                            <div class="card-body p-5 text-center">
                                <div class="failure-icon mb-4">❌</div>
                                <h5 class="card-title text-danger">Giao dịch VNPay không thành công</h5>
                                <p class="card-text">
                                    Rất tiếc, giao dịch của bạn không thể hoàn tất.<br>
                                    Vui lòng thử lại hoặc liên hệ hỗ trợ.
                                </p>
                        <% } %>
                        
                        <div class="alert alert-info">
                            <strong>Thông tin giao dịch:</strong><br>
                            <% if (vnp_TxnRef != null) { %>
                                Mã đơn hàng: <span class="fw-bold"><%= vnp_TxnRef %></span><br>
                            <% } %>
                            <% if (amountFormatted != null && !amountFormatted.isEmpty()) { %>
                                Số tiền: <span class="fw-bold"><%= amountFormatted %> VNĐ</span><br>
                            <% } %>
                            <% if (vnp_OrderInfo != null) { %>
                                Nội dung: <span class="fw-bold"><%= vnp_OrderInfo %></span><br>
                            <% } %>
                            <% if (vnp_ResponseCode != null) { %>
                                Mã phản hồi: <span class="fw-bold"><%= vnp_ResponseCode %></span><br>
                            <% } %>
                            Thời gian: <span class="fw-bold"><%= new java.util.Date() %></span>
                        </div>
                        
                        <div class="mt-4">
                            <button class="btn btn-primary me-2" onclick="closeWindow()">
                                Đóng cửa sổ
                            </button>
                            <% if (isSuccessful) { %>
                                <button class="btn btn-secondary" onclick="refreshAIMS()">
                                    Làm mới AIMS
                                </button>
                            <% } else { %>
                                <button class="btn btn-warning" onclick="retryPayment()">
                                    Thử lại
                                </button>
                            <% } %>
                        </div>
                    <% } else { %>
                        <div class="card-header bg-danger text-white text-center">
                            <h4 class="mb-0">❌ Thanh toán thất bại!</h4>
                        </div>
                        <div class="card-body p-5 text-center">
                            <div class="failure-icon mb-4">❌</div>
                            <h5 class="card-title text-danger">Giao dịch VNPay không thành công</h5>
                            <p class="card-text">
                                Rất tiếc, giao dịch của bạn không thể hoàn tất.<br>
                                Vui lòng thử lại hoặc liên hệ hỗ trợ.
                            </p>
                            <div class="alert alert-info">
                                <strong>Thông tin giao dịch:</strong><br>
                                <% if (vnp_TxnRef != null) { %>
                                    Mã đơn hàng: <span class="fw-bold"><%= vnp_TxnRef %></span><br>
                                <% } %>
                                <% if (amountFormatted != null && !amountFormatted.isEmpty()) { %>
                                    Số tiền: <span class="fw-bold"><%= amountFormatted %> VNĐ</span><br>
                                <% } %>
                                <% if (vnp_OrderInfo != null) { %>
                                    Nội dung: <span class="fw-bold"><%= vnp_OrderInfo %></span><br>
                                <% } %>
                                <% if (vnp_ResponseCode != null) { %>
                                    Mã phản hồi: <span class="fw-bold"><%= vnp_ResponseCode %></span><br>
                                <% } %>
                                Thời gian: <span class="fw-bold"><%= new java.util.Date() %></span>
                            </div>
                            <div class="mt-4">
                                <button class="btn btn-primary me-2" onclick="closeWindow()">
                                    Đóng cửa sổ
                                </button>
                                <button class="btn btn-warning" onclick="retryPayment()">
                                    Thử lại
                                </button>
                            </div>
                        </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Tự động đóng cửa sổ sau 5 giây
        setTimeout(function() {
            closeWindow();
        }, 5000);
        
        function closeWindow() {
            window.close();
        }
        
        function refreshAIMS() {
            // Gửi thông báo đến cửa sổ cha (nếu có)
            if (window.opener) {
                window.opener.postMessage('payment_success', '*');
            }
            closeWindow();
        }
        
        function retryPayment() {
            // Quay lại trang thanh toán
            if (window.opener) {
                window.opener.focus();
            }
            closeWindow();
        }
        
        // Lắng nghe thông báo từ cửa sổ cha
        window.addEventListener('message', function(event) {
            if (event.data === 'payment_success') {
                console.log('Payment success message received');
            }
        });
    </script>
</body>
</html>