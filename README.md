# PropTech Platform

Nền tảng bất động sản sử dụng kiến trúc microservices.

## Cấu trúc dự án

### Backend
- **api-gateway**: API Gateway sử dụng Spring Cloud Gateway
- **config-server**: Configuration Server quản lý cấu hình tập trung
- **discovery-server**: Service Discovery với Eureka
- **listing-service**: Dịch vụ quản lý danh sách bất động sản

### Frontend
- Giao diện người dùng xây dựng với công nghệ web hiện đại

## Cài đặt và chạy dự án

### Yêu cầu
- JDK 17+
- Maven
- Docker và Docker Compose (để chạy các container)

### Khởi động với Docker
```bash
cd backend/docker-compose
docker-compose up -d
```

## Phát triển
- Backend: Java Spring Boot, Spring Cloud
- Frontend: React