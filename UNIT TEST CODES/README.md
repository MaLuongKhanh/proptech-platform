Hướng Dẫn Chạy Unit Test cho PropTech Platform
Thư mục này (UNIT TEST CODES) chứa các bài test đơn vị cho các service của nền tảng PropTech, được xây dựng bằng pytest và requests để kiểm tra API tại http://localhost:8080. Các bài test bao quát các controller như Auth, Property, Wallet, v.v., tương ứng với các microservice trong dự án.
Tổng Quan Dự Án
PropTech Platform là một nền tảng bất động sản hiện đại sử dụng kiến trúc microservices, với các tính năng như quản lý danh sách bất động sản, xử lý thanh toán, quản lý người dùng, và giao dịch thuê/mua bán. Thư mục này cung cấp các bài test để đảm bảo chất lượng của các API backend.
Công Nghệ Sử Dụng

Framework Test: pytest
HTTP Client: requests
Môi Trường: Tích hợp với Spring Boot và Docker
Cơ Sở Dữ Liệu: MongoDB (dùng trong các service backend)
Quản Lý Dịch Vụ: Docker Swarm

Cấu Trúc Thư Mục
UNIT TEST CODES/
├── README.md              # Hướng dẫn này
├── run_all_tests.py       # Script chạy tất cả các bài test
├── test_auth_controller.py   # Test cho AuthController
├── test_contract_controller.py  # Test cho ContractController
├── test_listing_controller.py   # Test cho ListingController
├── test_property_controller.py  # Test cho PropertyController
├── test_rental_contract_controller.py  # Test cho RentalContractController
├── test_rental_transaction_controller.py  # Test cho RentalTransactionController
├── test_role_controller.py     # Test cho RoleController
├── test_transaction_controller.py  # Test cho TransactionController
├── test_user_controller.py     # Test cho UserController
├── test_wallet_controller.py   # Test cho WalletController
└── .pytest_cache/           # Cache của pytest (tự động tạo)

Điều Kiện Thực Thi

Python 3.x: Đảm bảo Python 3 đã được cài đặt.python --version


pip: Trình quản lý gói của Python.
Docker và Docker Compose: Để chạy các microservice backend.
Máy Chủ API: Spring Boot phải đang chạy tại http://localhost:8080.

Biến Môi Trường

Không yêu cầu biến môi trường đặc biệt trong thư mục test. Tuy nhiên, đảm bảo các service backend (như Config Server, API Gateway) đã được cấu hình đúng trong backend/docker-compose/.env.

Hướng Dẫn Khởi Chạy
Thiết Lập Môi Trường

Di Chuyển Vào Thư Mục:

Bạn đã ở trong thư mục UNIT TEST CODES. Nếu chưa, hãy vào:cd UNIT TEST CODES




Cài Đặt Các Thư Viện:

Cài đặt các gói Python cần thiết:pip install pytest requests


Tùy chọn: Cài đặt pytest-xdist để chạy test song song:pip install pytest-xdist




Khởi Động API:

Chuyển đến thư mục backend và chạy script deploy:cd ../../backend/docker-compose
deploy-swarm.bat  # Trên Windows
# Hoặc
chmod +x deploy-swarm.sh
./deploy-swarm.sh  # Trên Linux/Mac


Script sẽ khởi động tất cả các service (API Gateway tại cổng 8080, Config Server tại 8888, v.v.).
Đảm bảo API Gateway khả dụng tại http://localhost:8080.



Chạy Các Bài Test

Thực Thi Tất Cả Các Bài Test:

Sử dụng script run_all_tests.py để chạy tất cả các bài test với chế độ chi tiết và thống kê pass/fail:python run_all_tests.py


Script này chạy python -m pytest . -v và hiển thị kết quả như sau:


Tùy Chọn: Chạy Song Song:

Để tăng tốc độ, chỉnh sửa run_all_tests.py bằng cách thêm -n auto vào danh sách cmd:cmd = [sys.executable, "-m", "pytest", test_dir, "-v", "-n", "auto"]


Sau đó chạy:python run_all_tests.py




Ví Dụ Kết Quả:

Nếu tất cả pass:Found 10 test files: ['test_auth_controller.py', ...]
Running command: python -m pytest . -v
=== Pytest Output ===
test_auth_controller.py::test_login PASSED
...
test_wallet_controller.py::test_delete_wallet PASSED
==================== 50 passed in 5.32s ====================
=== Test Conclusion ===
Total Passed: 50
Total Failed: 0
All tests completed successfully.


Nếu một số fail:Found 10 test files: ['test_auth_controller.py', ...]
Running command: python -m pytest . -v
=== Pytest Output ===
test_auth_controller.py::test_login FAILED
...
test_wallet_controller.py::test_delete_wallet PASSED
==================== 45 passed, 5 failed in 5.67s ====================
=== Errors ===
E   AssertionError: Expected 200, got 401
=== Test Conclusion ===
Total Passed: 45
Total Failed: 5
Some tests failed. Check the output above for details.





Khắc Phục Sự Cố

Lỗi 401 Unauthorized:
Kiểm tra thông tin đăng nhập trong fixture auth_token (mặc định là admin/password) trong các file test. Cập nhật nếu cần.


API Không Chạy:
Đảm bảo Docker Swarm và các service đã khởi động. Kiểm tra log bằng docker logs <container_name> hoặc truy cập http://localhost:8080.


Không Tìm Thấy File Test:
Chạy ls *.py để kiểm tra các file test. Đảm bảo bạn ở trong thư mục UNIT TEST CODES.


Lỗi Thiếu Thư Viện:
Chạy lại lệnh cài đặt: pip install pytest requests.



Ghi Chú Thêm

Môi Trường Test: Các bài test giả định cơ sở dữ liệu MongoDB đã được cấu hình trong các service backend. Các fixture trong test (như test_property_controller.py) sẽ tạo dữ liệu test khi cần.
Cache Pytest: Thư mục .pytest_cache được tạo tự động. Xóa nếu cần bằng:pytest --cache-clear


Tùy Chỉnh: Có thể thêm tùy chọn pytest (ví dụ: --maxfail=1 để dừng khi gặp lỗi đầu tiên) bằng cách chỉnh sửa cmd trong run_all_tests.py.
