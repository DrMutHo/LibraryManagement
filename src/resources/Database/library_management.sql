-- Create the database
CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- Table for Clients 
CREATE TABLE IF NOT EXISTS Client (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    library_card_number VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    address VARCHAR(255),
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    outstanding_fees DECIMAL(10, 2) DEFAULT 0,
    username VARCHAR(255) UNIQUE NOT NULL,  
    password_hash VARCHAR(255) NOT NULL     
);

-- Table for Admins
CREATE TABLE IF NOT EXISTS Admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,    
    email VARCHAR(255) NOT NULL
);

-- Table for Books 
CREATE TABLE IF NOT EXISTS Book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    genre VARCHAR(100),
    language VARCHAR(50)
);

-- Table for individual book copies
CREATE TABLE IF NOT EXISTS BookCopy (
    copy_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE CASCADE
);

-- Table for Borrow Transactions (references Client table)
CREATE TABLE IF NOT EXISTS BorrowTransaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    copy_id INT,
    borrow_date DATE,
    return_date DATE,
    status ENUM('Processing', 'Done') DEFAULT 'Processing',
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (copy_id) REFERENCES BookCopy(copy_id)
);

-- Table for Notification Requests (when a book becomes available)
CREATE TABLE IF NOT EXISTS NotificationRequest (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    book_id INT,
    request_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (book_id) REFERENCES Book(book_id)
);

-- Insert sample data into Client với password đã băm
-- Mật khẩu gốc cho John Doe: password123
-- Mật khẩu gốc cho Jane Smith: mypassword456
INSERT INTO Client (name, library_card_number, email, phone_number, address, username, password_hash, outstanding_fees)
VALUES 
('John Doe', 'LIB12345', 'johndoe@gmail.com', '1234567890', '123 Elm Street', 'johndoe', '$2a$10$Ei3D2hj3KSZKmQeQx8tShu1Q/tfiN/FzXzPTpRC.vhh1AqPnLGmHG', 0.00),  -- password123
('Jane Smith', 'LIB67890', 'janesmith@yahoo.com', '0987654321', '456 Maple Street', 'janesmith', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.O.IJhhJIGtvn8u8FVbONZXxrZqPB8Pe', 10.50);  -- mypassword456

-- Insert sample data into Admin với password đã băm
-- Mật khẩu gốc cho admin1: password123
-- Mật khẩu gốc cho admin2: mypassword456
INSERT INTO Admin (username, password_hash, email)
VALUES 
('admin1', '$2a$10$Ei3D2hj3KSZKmQeQx8tShu1Q/tfiN/FzXzPTpRC.vhh1AqPnLGmHG', 'admin1@library.com'),  -- password123
('admin2', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.O.IJhhJIGtvn8u8FVbONZXxrZqPB8Pe', 'admin2@library.com');  -- mypassword456

-- Insert sample data into Book (không có publish_year và publisher)
INSERT INTO Book (title, author, isbn, genre, language)
VALUES 
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Fiction', 'English'),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Fiction', 'English');

-- Insert sample data into BookCopy
INSERT INTO BookCopy (book_id, is_available)
VALUES 
(1, TRUE),
(1, FALSE),
(2, TRUE);

-- Insert sample data into BorrowTransaction
INSERT INTO BorrowTransaction (client_id, copy_id, borrow_date, return_date, status)
VALUES 
(1, 2, '2024-10-01', '2024-10-15', 'Done'),
(2, 3, '2024-10-05', NULL, 'Processing');

-- Insert sample data into NotificationRequest
INSERT INTO NotificationRequest (client_id, book_id, request_date)
VALUES 
(2, 1, '2024-10-10');
