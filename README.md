## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).



<center>

# LIBRARY MANAGEMENT PROJECT 
### Bài Tập Lớn 2425I_INT2204_18

</center>


## Thành Viên Nhóm

- Nguyễn Xuân Bách
- Bùi Anh Chiến
- Bùi Phúc Bình
- Nguyễn Văn Tiền
  
  <center>

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

---
### Mượn và trả sách <a name="feature2"></a>

- Người dùng có thể mượn và trả sách với giao diện dễ hiểu.
- ---

### Tìm kiếm và gợi ý <a name="feature3"></a>

- Hệ thống gợi ý và tìm phù hợp người dùng dễ dàng khám phá sách phù hợp với sở thích và nhu cầu cá nhân 
- Kết hợp với khả năng thêm đa dạng tựa sách từ GoogleBook API.
- Chi tiết về hệ thống sẽ được đề cập tại phần  [Các công nghệ sử dụng](#Tech).

---
  
### Đánh giá và bình luận <a name="feature4"></a>

- Người dùng có thể tùy ý đánh giá và bình luận về nội dung sách giúp tăng tương tác giữa hệ thống và người dùng cũng như để cải thiện khả năng gợi ý của hệ thống.
  
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




