# Hướng dẫn cài đặt & chạy AIMS (ISD)

## 1. Cài đặt PostgreSQL

- Cài đặt PostgreSQL (nên dùng bản 13 trở lên).
- Đảm bảo user `postgres` có password là `kai2604`.
- Đảm bảo PostgreSQL chạy trên cổng mặc định 5432.

## 2. Tạo database và các bảng

- Mở **pgAdmin** hoặc **DBeaver** hoặc **psql**.
- Chạy file SQL sau để tạo database và các bảng:

```sh
psql -U postgres -f database.sql
```
- Hoặc copy toàn bộ nội dung file `database.sql` vào Query Tool của pgAdmin và chạy.

## 3. Build ứng dụng

- Mở terminal/cmd tại thư mục gốc dự án.
- Chạy lệnh:
```sh
build.bat
```
- Nếu thành công, file JAR sẽ nằm ở `build/jar/aims-swing.jar`.

## 4. Chạy ứng dụng

- Vào thư mục `build/jar`:
```sh
cd build/jar
```
- Chạy ứng dụng:
```sh
java -jar aims-swing.jar
```

## 5. Đăng nhập và sử dụng

- Đăng nhập bằng tài khoản đã có trong bảng `users` (hoặc tự tạo user mới trong database).
- Bắt đầu trải nghiệm ứng dụng!

---

**Lưu ý:**  
- Nếu gặp lỗi kết nối database, kiểm tra lại user/password/cổng và database đã tạo đúng chưa.
- Nếu cần reset database, chỉ cần xóa database `isd` và chạy lại file SQL.

---

Chúc bạn thành công!
