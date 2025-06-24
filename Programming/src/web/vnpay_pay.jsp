<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>AIMS - VNPay Payment</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>

    <body>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="text-center">AIMS - VNPAY Payment</h3>
                        </div>
                        <div class="card-body">
                            <h4>Tạo giao dịch thanh toán</h4>
                            <form action="/aimsfinal/vnpayajax" id="frmCreateOrder" method="post">        
                                <div class="mb-3">
                                    <label for="amount" class="form-label">Số tiền</label>
                                    <input class="form-control" id="amount" name="amount" type="number" value="10000" min="1" max="100000000" required />
                                </div>
                                
                                <h5>Chọn phương thức thanh toán</h5>
                                <div class="mb-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="bankCode1" name="bankCode" value="" checked>
                                        <label class="form-check-label" for="bankCode1">
                                            Cổng thanh toán VNPAYQR
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="bankCode2" name="bankCode" value="VNPAYQR">
                                        <label class="form-check-label" for="bankCode2">
                                            Thanh toán bằng ứng dụng hỗ trợ VNPAYQR
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="bankCode3" name="bankCode" value="VNBANK">
                                        <label class="form-check-label" for="bankCode3">
                                            Thanh toán qua thẻ ATM/Tài khoản nội địa
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="bankCode4" name="bankCode" value="INTCARD">
                                        <label class="form-check-label" for="bankCode4">
                                            Thanh toán qua thẻ quốc tế
                                        </label>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <h5>Chọn ngôn ngữ giao diện thanh toán:</h5>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="language1" name="language" value="vn" checked>
                                        <label class="form-check-label" for="language1">Tiếng việt</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="language2" name="language" value="en">
                                        <label class="form-check-label" for="language2">Tiếng anh</label>
                                    </div>
                                </div>
                                
                                <button type="submit" class="btn btn-primary">Thanh toán</button>
                                <a href="index.jsp" class="btn btn-secondary">Quay lại</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
          
        <script type="text/javascript">
            $("#frmCreateOrder").submit(function () {
                var postData = $("#frmCreateOrder").serialize();
                var submitUrl = $("#frmCreateOrder").attr("action");
                $.ajax({
                    type: "POST",
                    url: submitUrl,
                    data: postData,
                    dataType: 'JSON',
                    success: function (x) {
                        if (x.code === '00') {
                            location.href = x.data;
                            return false;
                        } else {
                            alert(x.message);
                        }
                    },
                    error: function() {
                        alert("Có lỗi xảy ra khi tạo giao dịch!");
                    }
                });
                return false;
            });
        </script>       
    </body>
</html>