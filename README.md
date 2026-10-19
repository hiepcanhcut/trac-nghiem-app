# 📚 Trắc Nghiệm Pro (Ứng Dụng Thi & Ôn Luyện Trắc Nghiệm Android - Java)

Ứng dụng làm bài thi và luyện tập trắc nghiệm được xây dựng hoàn toàn bằng **Java thuần** và **XML Layout (Material Components)** trên **Android Studio**.

---

## ✨ Tính Năng Nổi Bật

### 1. 📝 Quản Lý Ngân Hàng Câu Hỏi (CRUD Đầy Đủ)
- **Thêm câu hỏi mới**: Nhập nội dung câu hỏi, chọn chủ đề/môn học (Toán, Lịch sử, Khoa học, Công nghệ...), nhập 4 phương án A, B, C, D, tích chọn đáp án đúng và thêm giải thích chi tiết.
- **Chỉnh sửa & Xóa**: Chỉnh sửa nhanh chóng các câu hỏi đã có, xóa câu hỏi có hộp thoại cảnh báo an toàn.
- **Tìm kiếm & Bộ lọc**: Tìm kiếm câu hỏi và câu trả lời theo từ khóa, lọc theo danh mục môn học.
- **Khôi phục câu hỏi mẫu**: Tự động nạp sẵn các câu hỏi mẫu phong phú khi cài đặt lần đầu và cho phép khôi phục khi cần.

### 2. 🎯 Chế Độ Thi & Luyện Tập Trắc Nghiệm
- **Tùy biến bài thi**: Chọn môn học và số lượng câu hỏi (5, 10, 20 câu hoặc tất cả).
- **Đồng hồ đếm giờ**: Hiển thị thời gian làm bài trực tiếp theo thời gian thực (MM:SS).
- **Đánh dấu (Bookmark/Flag)**: Gắn cờ các câu hỏi khó để xem lại trước khi nộp bài.
- **Bảng câu hỏi nhanh (Question Grid Dialog)**: Nhảy trực tiếp đến câu bất kỳ, nhận biết trực quan câu đã làm, câu đánh dấu và câu chưa làm.
- **Cảnh báo nộp bài**: Nhắc nhở người thi nếu vẫn còn câu hỏi chưa chọn đáp án.

### 3. 📊 Kết Quả & Xem Lại Đáp Án Chi Tiết
- Đánh giá tổng điểm, tỷ lệ % và xếp loại (*Xuất sắc*, *Khá tốt*, *Đạt yêu cầu*, *Cần cố gắng*).
- **Chi tiết từng câu**: Đối chiếu đáp án bạn đã chọn với đáp án chính xác (màu xanh/đỏ rõ ràng) kèm **lời giải thích chi tiết** lý do vì sao đáp án đó đúng.
- Làm lại đề thi hoặc trở về màn hình chính.

### 4. 🕒 Lịch Sử Bài Thi
- Lưu trữ mọi lần làm bài thi (thời gian làm, điểm số, thời lượng) để theo dõi tiến trình học tập.

---

## 🛠️ Công Nghệ & Kiến Trúc Dự Án

- **Ngôn ngữ**: **Java** (JDK 11 / Java 8+)
- **Giao diện**: **XML Layouts** với **Google Material Components**
- **Kiến trúc**: MVC/MVVM chuẩn Android truyền thống (Activity, Adapter, SQLiteOpenHelper)
- **Lưu trữ dữ liệu**: SQLite thuần (`SQLiteOpenHelper`) - Offline 100%, không cần kết nối mạng
- **Danh sách hiển thị**: `RecyclerView` và `CardView`
- **Công cụ phát triển**: Android Studio (hỗ trợ mở, chỉnh sửa và chạy trực tiếp)

---

## 📂 Cấu Trúc Mã Nguồn (Java)

```
com.example.tracnghiemapp/
│
├── MainActivity.java               # Màn hình chính (Thống kê, Menu chức năng)
├── QuizActivity.java               # Màn hình làm bài thi (Timer, Chuyển câu, Đánh dấu, Nộp bài)
├── ResultActivity.java             # Màn hình kết quả & Xem lại đáp án giải thích
├── ManageQuestionsActivity.java    # Màn hình quản lý ngân hàng câu hỏi (Tìm kiếm, Lọc, Xóa)
├── AddEditQuestionActivity.java    # Màn hình Thêm mới và Chỉnh sửa câu hỏi
├── HistoryActivity.java            # Màn hình lịch sử các lần thi
│
├── adapter/
│   ├── QuestionAdapter.java        # Adapter hiển thị danh sách câu hỏi trong quản lý
│   ├── ReviewAdapter.java          # Adapter hiển thị xem lại câu đúng/sai & giải thích
│   └── HistoryAdapter.java         # Adapter hiển thị lịch sử làm bài
│
├── database/
│   └── QuizDbHelper.java           # SQLite Database Helper (CRUD câu hỏi & lịch sử)
│
└── model/
    ├── Question.java               # Model đối tượng câu hỏi
    └── QuizHistory.java            # Model đối tượng lịch sử thi
```

---

## 🚀 Hướng Dẫn Mở & Chạy Trên Android Studio

1. Mở **Android Studio**.
2. Chọn **Open** và dẫn tới thư mục `tracnghiemapp`.
3. Chờ Gradle sync hoàn tất.
4. Nhấn **Run ▶** (hoặc `Shift + F10`) để chạy trên máy ảo hoặc thiết bị thật.

---

## 📦 Xuất Bản Lên Google Play Store

1. Trong Android Studio: **Build** -> **Generate Signed Bundle / APK...**
2. Chọn **Android App Bundle (.aab)**.
3. Tạo Keystore ký mã Release.
4. Tải file `.aab` trong `app/release/` lên **Google Play Console**.
