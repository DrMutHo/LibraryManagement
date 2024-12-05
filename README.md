<center>

# LIBRARY MANAGEMENT PROJECT 
### Bài Tập Lớn 2425I_INT2204_18

</center>


## Thành Viên Nhóm

- Nguyễn Xuân Bách
  <details>
  <summary>Phụ trách </summary>
  notification, browsing, bookdetail và các logic, database, phụ trách fix sau merge, dashboard, interface lắng nghe thay đổi.
</details>

- Bùi Anh Chiến
  <details>
  <summary>Phụ trách </summary>
  login signup forgotpassword,  phần setting,  đa luồng.
  card, borrowtransaction, trending books, profile, report.
</details>

- Bùi Phúc Bình
  <details>
  <summary>Phụ trách </summary>
  card, borrowtransaction, trending books, profile, report.
</details>

- Nguyễn Văn Tiền
  <details>
  <summary>Phụ trách </summary>
  Chuyển các phần từ client sang admin, gợi ý sách.
</details>
  

## Table Of Content

- [Giới Thiệu](#introduction)


- [Các Chức Năng](#feature) 
   - [Quản lý](#feature1)
   - [Mượn và trả sách](#feature2)
   - [Tìm kiếm và gợi ý](#feature3)
   - [Đánh giá và bình luận](#feature4)
   - [Thống kê và báo cáo](#feature5)
   - [Bảo mật](#feature5)
 - [Các công nghệ sử dụng](#Tech)
    - [Javafx](#Tech1)
    - [GoogleBook API](#Tech2)
    - [Thuật toán Cosine  Simmilarity](#Tech3)
  
  
 


## I. Giới Thiệu <a name="introduction"></a>

  - Ứng dụng thư viện được thiết kế với giao diện thân thiện, kho sách đa dạng và tích hợp hệ thống gợi ý hỗ trợ người dùng tìm kiếm và khám phá theo sở thích và nhu cầu cá nhân.


## II. Chức Năng <a name="feature"></a>

### Quản Lý <a name="feature1"></a>
| **Chức năng**          | **Người dùng**                                     | **Người quản trị**                                    |
|-------------------------|---------------------------------------------------|------------------------------------------------------|
| **Quản lý sách**        | Tìm kiếm sách, xem thông tin sách.                | Thêm, sửa, xóa sách, quản lý số lượng.              |
| **Quản lý người dùng**  |                                     |  Theo dõi và xuất file người dùng.              |
| **Quản lý trang cá nhân** | Chỉnh sửa thông tin cá nhân.                      |             |
| **Quản lý thông báo**   | Nhận thông báo về sách mới, nhắc nhở.             | Nhận thông báo về người dùng, sách, giao dịch mới và lỗi được người dùng Report            |
| **Quản lý giao dịch**   | Đặt mượn sách, kiểm tra lịch sử giao dịch.        | Theo dõi và xử lý các giao dịch mượn, trả sách.     |

<details>
  <summary>Các chức năng quản lý </summary>
  <div style="display: flex; gap: 10px;">
    <img src= pictures/adminbookvie.png alt="Ảnh 1" width="150" style="border-radius: 15px;">
  </div>



  <div style="display: flex; gap: 10px;">
    <img src= pictures/dashboard.png alt="Ảnh 2" width="150" style="border-radius: 15px;">
  </div>

</details>


---
### Mượn và trả sách <a name="feature2"></a>

- Người dùng có thể mượn và trả sách với giao diện dễ hiểu.

<details>
  <summary>Mượn và trả sách </summary>
  <div style="display: flex; gap: 10px;">
    <img src= pictures/borrow.png alt="Ảnh 1" width="400" style="border-radius: 15px;">
  </div>



  <div style="display: flex; gap: 10px;">
    <img src= pictures/dashboard.png alt="Ảnh 2" width="400" style="border-radius: 15px;">
  </div>

</details>

---

### Tìm kiếm và gợi ý <a name="feature3"></a>

- Hệ thống gợi ý và tìm phù hợp người dùng dễ dàng khám phá sách phù hợp với sở thích và nhu cầu cá nhân 
- Kết hợp với khả năng thêm đa dạng tựa sách từ GoogleBook API.
- Chi tiết về hệ thống sẽ được đề cập tại phần  [Các công nghệ sử dụng](#Tech).
- 
<details>
  <summary>Tìm kiếm và gợi ý </summary>
  <div style="display: flex; gap: 10px;">
    <img src= pictures/borrow.png alt="Ảnh 1" width="400" style="border-radius: 15px;">
  </div>



  <div style="display: flex; gap: 10px;">
    <img src= pictures/rec.png alt="Ảnh 2" width="400" style="border-radius: 15px;">
  </div>

</details>

---
  
### Đánh giá và bình luận <a name="feature4"></a>

- Người dùng có thể tùy ý đánh giá và bình luận về nội dung sách giúp tăng tương tác giữa hệ thống và người dùng cũng như để cải thiện khả năng gợi ý của hệ thống.


<details>
  <summary>Đánh giá và bình luận</summary>
  <div style="display: flex; gap: 10px;">
    <img src= pictures/review.png alt="Ảnh 1" width="400" style="border-radius: 15px;">
 </div>

</details>

---

### Thống kê và báo cáo <a name="feature4"></a>

| **Chức năng**         | **Người dùng**                        | **Người quản trị**                          |
|------------------------|---------------------------------------|---------------------------------------------|
| **Thống kê giao dịch** | Xem lại và xuất file tất cả giao dịch của tài khoản           | Theo dõi và xuất file tất cả giao dịch.     |
| **Thống kê người dùng**|                        | Theo dõi và xuất file tất cả giao dịch người dùng.  |
| **Báo cáo lỗi**        | Gửi báo cáo lỗi hoặc sự cố gặp phải. | Xem và xử lý các báo cáo lỗi từ người dùng. |
| **Thống kê số lượng sách**| Xem tổng số sách mượn theo thời gian.      | Xem tổng số sách mượn theo thời gian của tất cả tài khoản. |
| **Thống kê tiền**         |                            | Theo dõi và quản lý các khoản thu.      |
| **Thống kê thể loại**     | Xem các thể loại sách phổ biến.            | Phân tích mức độ phổ biến của các thể loại sách.  |


---
### Bảo mật <a name="feature5"></a>

- Mật khẩu của người dùng được hash trước khi được đưa vào Database.


## III. Công nghệ sử dụng <a name="Tech"></a>

### JavaFx <a name="Tech1"></a>

- Hệ thống sử dụng JavaFx và Scenebuilder.
  
---

### GoogleBook API <a name="Tech2"></a>

- Hệ thống sử dụng kho lưu trữ đa dạng của Google để tải sách về kho thư viện.
  
--- 
### Thuật toán Cosine Simmilarity (C/S) <a name="Tech3"></a>

- Hệ thống gợi ý sử dụng thuật toán **C/S** để đưa ra gợi ý sách cho người dùng.
- Thuật toán hoạt động bằng cách coi từng phần trong bộ dữ liệu là các Vector và lấy giá trị Cosine góc giữa chúng. Thuật toán được áp dụng bộ {Người dùng, Sách, Rating}.
- Thuật toán được chia làm 2 phần: tính tương quan giữa người và người, tính tương quan giữa sách và sách sau đó đưa ra tổng hợp. 




>>>>>>> Stashed changes
