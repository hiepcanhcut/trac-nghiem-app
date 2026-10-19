# 📚 Trắc Nghiệm Pro (Multiple-Choice Quiz Android App)

Ứng dụng làm bài thi và luyện tập trắc nghiệm hiện đại được xây dựng hoàn toàn bằng **Kotlin** và **Jetpack Compose (Material 3)** trên **Android Studio**.

---

## ✨ Tính Năng Nổi Bật

### 1. 📝 Quản Lý Ngân Hàng Câu Hỏi (CRUD Đầy Đủ)
- **Thêm câu hỏi mới**: Hỗ trợ nhập nội dung câu hỏi, chọn chủ đề/môn học (Toán, Lịch sử, Khoa học, Công nghệ...), 4 lựa chọn A, B, C, D, tích chọn đáp án đúng trực quan và giải thích chi tiết.
- **Chỉnh sửa & Xóa**: Chỉnh sửa nhanh chóng các câu hỏi đã có, xóa câu hỏi có hộp thoại xác nhận.
- **Tìm kiếm & Bộ lọc**: Tìm kiếm câu hỏi và câu trả lời theo từ khóa, lọc theo danh mục môn học.
- **Khôi phục câu hỏi mẫu**: Tự động nạp sẵn các câu hỏi mẫu phong phú khi cài đặt lần đầu và cho phép khôi phục khi cần.

### 2. 🎯 Chế Độ Thi & Luyện Tập Trắc Nghiệm
- **Tùy biến bài thi**: Chọn môn học và số lượng câu hỏi (5, 10, 20 câu hoặc tất cả).
- **Đồng hồ đếm giờ**: Hiển thị thời gian làm bài trực tiếp theo thời gian thực.
- **Đánh dấu (Bookmark/Flag)**: Gắn cờ các câu hỏi khó để xem lại trước khi nộp bài.
- **Bảng câu hỏi nhanh (Question Grid Modal)**: Nhảy trực tiếp đến câu bất kỳ, nhận biết trực quan câu đã làm, câu đánh dấu và câu chưa làm.
- **Cảnh báo nộp bài**: Nhắc nhở người thi nếu vẫn còn câu hỏi chưa chọn đáp án.

### 3. 📊 Kết Quả & Xem Lại Đáp Án Chi Tiết
- Đánh giá tổng điểm, tỷ lệ % và xếp loại (*Xuất sắc*, *Khá tốt*, *Cần cố gắng*).
- **Chi tiết từng câu**: Đối chiếu đáp án bạn đã chọn với đáp án chính xác (màu xanh/đỏ rõ ràng) kèm **lời giải thích chi tiết** lý do vì sao đáp án đó đúng.
- Làm lại đề thi hoặc trở về màn hình chính.

### 4. 🕒 Lịch Sử Bài Thi
- Lưu trữ mọi lần làm bài thi (thời gian làm, điểm số, thời lượng) để theo dõi tiến trình học tập.

---

## 🛠️ Công Nghệ Sử Dụng

- **Ngôn ngữ**: [Kotlin](https://kotlinlang.org/) (Kotlin 2.x)
- **Giao diện**: [Jetpack Compose](https://developer.android.com/jetpack/compose) với [Material 3](https://m3.material.io/)
- **Kiến trúc**: MVVM (Model - View - ViewModel) + Repository Pattern
- **Lưu trữ dữ liệu**: SQLite thuần (Offline-first, không phụ thuộc internet)
- **Bất đồng bộ**: Kotlin Coroutines & StateFlow
- **Công cụ phát triển**: Android Studio (AGP 9.x, Target SDK 35/37)

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

1. Clone repository về máy:
   ```bash
   git clone https://github.com/hiepcanhcut/trac-nghiem-app.git
   ```
2. Mở thư mục dự án bằng **Android Studio**.
3. Chờ Gradle đồng bộ (Sync Project with Gradle Files).
4. Nhấn **Run ▶** (hoặc `Shift + F10`) để chạy trên thiết bị thật hoặc máy ảo Android.

---

## 📦 Xuất Bản Lên Google Play Store

1. Trong Android Studio: **Build** -> **Generate Signed Bundle / APK...**
2. Chọn **Android App Bundle (.aab)**.
3. Chọn Keystore và cấu hình khóa ký Release.
4. Tải file `.aab` trong `app/release/` lên **Google Play Console**.
