# NTU CMS - Content Management System

Hệ thống quản lý nội dung cho Khoa Công nghệ thông tin - Trường Đại học Nha Trang

## Công nghệ sử dụng

- Java 17
- Spring Boot 3.x
- MySQL 8.0
- Spring Security với JWT
- Swagger UI cho API documentation

## Cài đặt

### Yêu cầu hệ thống

- JDK 17 trở lên
- MySQL 8.0 trở lên
- Maven 3.8 trở lên

### Các bước cài đặt

1. Clone repository:
```bash
git clone <repository-url>
cd cms
```

2. Cấu hình database:
- Tạo database MySQL với tên 'cms'
- Import file `src/main/resources/cms.sql`
- Cập nhật thông tin kết nối trong `application.properties` nếu cần

3. Build project:
```bash
mvn clean install
```

4. Chạy ứng dụng:
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: http://localhost:8080/api

## Tài khoản mẫu

1. Admin:
   - Username: admin
   - Password: admin123

2. Editor:
   - Username: editor
   - Password: admin123

3. User:
   - Username: user
   - Password: admin123

## API Documentation

Swagger UI: http://localhost:8080/api/swagger-ui.html

## Cấu trúc Database

1. Users & Roles:
   - users: Thông tin người dùng
   - user_roles: Phân quyền người dùng

2. Content:
   - articles: Bài viết
   - categories: Danh mục
   - tags: Thẻ
   - article_tags: Liên kết bài viết - thẻ
   - comments: Bình luận

3. Media:
   - media: Quản lý file upload

4. Others:
   - search_history: Lịch sử tìm kiếm
   - persistent_logins: Quản lý phiên đăng nhập

## Contributing

1. Fork repository
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit thay đổi (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Cấu hình môi trường

### Development
1. Chạy trực tiếp:
```bash
mvn spring-boot:run
```

2. Hoặc chạy với profile dev:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production
1. Tạo file `.env` từ file mẫu:
```bash
.\create-env.bat
```

2. Chỉnh sửa file `.env` với các giá trị phù hợp cho môi trường production:
- `DB_PASSWORD`: Mật khẩu database
- `JWT_SECRET`: Khóa bí mật cho JWT
- `REMEMBER_ME_KEY`: Khóa cho tính năng Remember Me
- `SSL_KEYSTORE_PASSWORD`: Mật khẩu cho keystore (nếu sử dụng SSL)
- `CORS_ALLOWED_ORIGINS`: Danh sách domain được phép truy cập API

3. Chạy ứng dụng:
```bash
.\run-prod.bat
```

## Các endpoint chính

- Swagger UI: http://localhost:9090/api/swagger-ui.html
- API Docs: http://localhost:9090/api/v3/api-docs
- Health Check: http://localhost:9090/api/actuator/health
- Metrics: http://localhost:9090/api/actuator/metrics (yêu cầu quyền ADMIN)

## Tài khoản mặc định

- Admin:
  * Username: admin
  * Password: admin123

- Editor:
  * Username: editor
  * Password: editor123

- User:
  * Username: user
  * Password: user123

## Cấu hình SSL/TLS

1. Tạo keystore:
```bash
keytool -genkeypair -alias cms -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore cms.p12 -validity 3650
```

2. Cấu hình trong file `.env`:
```
SSL_ENABLED=true
SSL_KEYSTORE=classpath:keystore/cms.p12
SSL_KEYSTORE_PASSWORD=your-keystore-password
SSL_KEYSTORE_TYPE=PKCS12
SSL_KEY_ALIAS=cms
```

3. Copy file keystore vào thư mục `src/main/resources/keystore/`

## Monitoring

1. Metrics có sẵn:
- JVM metrics
- System metrics
- Application metrics
- Database connection pool metrics
- Cache metrics

2. Prometheus endpoint: http://localhost:9090/api/actuator/prometheus

## Logging

- Log file: `logs/cms.log`
- Log rotation:
  * Kích thước tối đa: 10MB
  * Số file tối đa: 30
  * Tổng dung lượng tối đa: 3GB

## Security

1. Password Policy:
- Độ dài tối thiểu: 8 ký tự
- Yêu cầu chữ hoa
- Yêu cầu chữ thường
- Yêu cầu số
- Yêu cầu ký tự đặc biệt

2. Account Lockout:
- Số lần thử tối đa: 5 lần
- Thời gian khóa: 15 phút

3. Session Security:
- Session timeout: 30 phút
- Secure cookie
- HTTP-only cookie
- SameSite strict

## File Upload

- Thư mục upload: `uploads/`
