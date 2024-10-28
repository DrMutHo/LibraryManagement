-- Create the database
CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- Table for Clients
CREATE TABLE IF NOT EXISTS Client (
    client_id INT AUTO_INCREMENT PRIMARY KEY,         -- Unique ID for each client
    name VARCHAR(255) NOT NULL,                       -- Name of the client
    library_card_number VARCHAR(255) UNIQUE NOT NULL, -- Unique library card number
    email VARCHAR(255) NOT NULL,                      -- Client's email
    phone_number VARCHAR(15),                         -- Client's phone number
    address VARCHAR(255),                             -- Client's address
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP, -- Date of registration
    outstanding_fees DECIMAL(10, 2) DEFAULT 0,        -- Outstanding fees for the client
    username VARCHAR(255) UNIQUE NOT NULL,            -- Username for client login
    password_hash VARCHAR(255) NOT NULL               -- Password hash for client login
);

-- Table for Admins
CREATE TABLE IF NOT EXISTS Admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,          -- Unique ID for each admin
    username VARCHAR(255) NOT NULL UNIQUE,            -- Username for admin login
    password_hash VARCHAR(255) NOT NULL,              -- Password hash for admin login
    email VARCHAR(255) NOT NULL                       -- Admin's email
);

-- Table for Books
CREATE TABLE IF NOT EXISTS Book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,           -- Unique ID for each book
    title VARCHAR(255) NOT NULL,                      -- Title of the book
    author VARCHAR(255) NOT NULL,                     -- Author of the book
    isbn VARCHAR(13) UNIQUE NOT NULL,                 -- ISBN of the book
    genre VARCHAR(100),                               -- Genre of the book
    language VARCHAR(50),                             -- Language of the book
    description TEXT,                                 -- Description of the book
    publication_year INT,                             -- Year of publication
    image_url VARCHAR(255)                            -- URL for book image
);

-- Table for individual book copies
CREATE TABLE IF NOT EXISTS BookCopy (
    copy_id INT AUTO_INCREMENT PRIMARY KEY,           -- Unique ID for each physical book copy
    book_id INT,                                      -- Foreign key referencing the book this copy belongs to
    is_available BOOLEAN DEFAULT TRUE,                -- Availability status of the book copy
    book_condition ENUM('New', 'Good', 'Fair', 'Poor') DEFAULT 'Good', -- Physical condition of the book copy
    FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE CASCADE
);

-- Table for Borrow Transactions
CREATE TABLE IF NOT EXISTS BorrowTransaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,    -- Unique ID for each transaction
    client_id INT,                                    -- Foreign key referencing the client borrowing the book
    copy_id INT,                                      -- Foreign key referencing the book copy being borrowed
    borrow_date DATE,                                 -- Date when the book was borrowed
    return_date DATE,                                 -- Date when the book was returned
    status ENUM('Processing', 'Done') DEFAULT 'Processing', -- Status of the transaction
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (copy_id) REFERENCES BookCopy(copy_id)
);

-- Table for Notification Requests
CREATE TABLE IF NOT EXISTS NotificationRequest (
    request_id INT AUTO_INCREMENT PRIMARY KEY,        -- Unique ID for each notification request
    client_id INT,                                    -- Foreign key referencing the client requesting notification
    book_id INT,                                      -- Foreign key referencing the book for which notification is requested
    request_date DATETIME DEFAULT CURRENT_TIMESTAMP,  -- Date of notification request
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (book_id) REFERENCES Book(book_id)
);

-- Table for Borrow Receipt (record of borrowing transactions)
CREATE TABLE IF NOT EXISTS BorrowReceipt (
    receipt_id INT AUTO_INCREMENT PRIMARY KEY,        -- Unique ID for each borrow receipt
    transaction_id INT,                               -- Foreign key referencing the borrow transaction
    issued_date DATETIME DEFAULT CURRENT_TIMESTAMP,   -- Date when the receipt was issued
    issued_by INT,                                    -- Admin ID who issued the receipt
    FOREIGN KEY (transaction_id) REFERENCES BorrowTransaction(transaction_id) ON DELETE CASCADE,
    FOREIGN KEY (issued_by) REFERENCES Admin(admin_id)
);

-- Table for Return Receipt (record of return transactions)
CREATE TABLE IF NOT EXISTS ReturnReceipt (
    receipt_id INT AUTO_INCREMENT PRIMARY KEY,        -- Unique ID for each return receipt
    transaction_id INT,                               -- Foreign key referencing the borrow transaction
    return_date DATETIME DEFAULT CURRENT_TIMESTAMP,   -- Date when the book was returned
    received_by INT,                                  -- Admin ID who received the returned book
    condition_on_return ENUM('New', 'Good', 'Fair', 'Poor'), -- Condition of the book when returned
    FOREIGN KEY (transaction_id) REFERENCES BorrowTransaction(transaction_id) ON DELETE CASCADE,
    FOREIGN KEY (received_by) REFERENCES Admin(admin_id)
);

-- Table for Purchase Receipt (record of book purchases)
CREATE TABLE IF NOT EXISTS PurchaseReceipt (
    receipt_id INT AUTO_INCREMENT PRIMARY KEY,        -- Unique ID for each purchase receipt
    book_id INT,                                      -- Foreign key referencing the book being purchased
    copy_id INT,                                      -- Foreign key referencing the purchased book copy
    purchase_date DATETIME DEFAULT CURRENT_TIMESTAMP, -- Date when the book was purchased
    purchased_by INT,                                 -- Admin ID who managed the purchase
    notes TEXT,                                       -- Additional notes or details about the purchase
    FOREIGN KEY (book_id) REFERENCES Book(book_id),
    FOREIGN KEY (copy_id) REFERENCES BookCopy(copy_id),
    FOREIGN KEY (purchased_by) REFERENCES Admin(admin_id)
);

-- Simplified Table for Notifications
CREATE TABLE IF NOT EXISTS Notification (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,  -- Unique ID for each notification
    recipient_id INT NOT NULL,                       -- ID of the recipient (Admin or Client)
    recipient_type ENUM('Admin', 'Client') NOT NULL, -- Type of recipient (indicates whether recipient_id references Admin or Client)
    notification_type ENUM('BorrowReceiptIssued', 'ReturnReceiptIssued', 'PurchaseReceiptIssued', 'OverdueAlert', 'BorrowRequestConfirmed', 'ReturnReminder') NOT NULL, -- Type of notification
    message TEXT NOT NULL,                           -- Message content of the notification
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,   -- Date and time when notification was created
    is_read BOOLEAN DEFAULT FALSE                    -- Read status of the notification
);

-- Insert sample data into Client with hashed passwords
-- Passwords are hashed using bcrypt (for example purposes)
INSERT INTO Client (name, library_card_number, email, phone_number, address, username, password_hash, outstanding_fees)
VALUES 
('John Doe', 'LIB12345', 'johndoe@gmail.com', '1234567890', '123 Elm Street', 'johndoe', '$2a$10$Ei3D2hj3KSZKmQeQx8tShu1Q/tfiN/FzXzPTpRC.vhh1AqPnLGmHG', 0.00),  -- password123
('Jane Smith', 'LIB67890', 'janesmith@yahoo.com', '0987654321', '456 Maple Street', 'janesmith', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.O.IJhhJIGtvn8u8FVbONZXxrZqPB8Pe', 10.50);  -- mypassword456

-- Insert sample data into Admin with hashed passwords
INSERT INTO Admin (username, password_hash, email)
VALUES 
('admin1', '$2a$10$Ei3D2hj3KSZKmQeQx8tShu1Q/tfiN/FzXzPTpRC.vhh1AqPnLGmHG', 'admin1@library.com'),  -- password123
('admin2', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.O.IJhhJIGtvn8u8FVbONZXxrZqPB8Pe', 'admin2@library.com');  -- mypassword456

-- Insert sample data into Book
INSERT INTO Book (title, author, isbn, genre, language, description, publication_year, image_url)
VALUES 
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Fiction', 'English', 'A novel set in the Jazz Age about the mysterious Jay Gatsby.', 1925, 'https://example.com/greatgatsby.jpg'),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Fiction', 'English', 'A story of racial injustice in the Deep South.', 1960, 'https://example.com/mockingbird.jpg');

-- Insert sample data into BookCopy
INSERT INTO BookCopy (book_id, is_available, book_condition)
VALUES 
(1, TRUE, 'Good'),
(1, FALSE, 'Fair'),
(2, TRUE, 'New');

-- Insert sample data into BorrowTransaction
INSERT INTO BorrowTransaction (client_id, copy_id, borrow_date, return_date, status)
VALUES 
(1, 2, '2024-10-01', '2024-10-15', 'Done'),
(2, 3, '2024-10-05', NULL, 'Processing');

-- Insert sample data into NotificationRequest
INSERT INTO NotificationRequest (client_id, book_id, request_date)
VALUES 
(2, 1, '2024-10-10');

-- Insert sample data into BorrowReceipt
INSERT INTO BorrowReceipt (transaction_id, issued_by)
VALUES 
(1, 1);

-- Insert sample data into ReturnReceipt
INSERT INTO ReturnReceipt (transaction_id, received_by, condition_on_return)
VALUES 
(1, 1, 'Good');

-- Insert sample data into PurchaseReceipt
INSERT INTO PurchaseReceipt (book_id, copy_id, purchased_by, notes)
VALUES 
(2, 3, 2, 'Purchased new copy for the library');

-- Sample Insertions for Notifications

-- Notification when a borrow receipt is issued
INSERT INTO Notification (recipient_id, recipient_type, notification_type, message)
VALUES 
(1, 'Admin', 'BorrowReceiptIssued', 'A borrow receipt has been issued.');

-- Notification when a return receipt is issued
INSERT INTO Notification (recipient_id, recipient_type, notification_type, message)
VALUES 
(1, 'Admin', 'ReturnReceiptIssued', 'A return receipt has been issued.');

-- Notification when a purchase receipt is issued
INSERT INTO Notification (recipient_id, recipient_type, notification_type, message)
VALUES 
(2, 'Admin', 'PurchaseReceiptIssued', 'A purchase receipt has been issued for a new book copy.');

-- Notification for an overdue book
INSERT INTO Notification (recipient_id, recipient_type, notification_type, message)
VALUES 
(1, 'Admin', 'OverdueAlert', 'A book is overdue.');

-- Notification when a borrow request is confirmed
INSERT INTO Notification (recipient_id, recipient_type, notification_type, message)
VALUES 
(2, 'Client', 'BorrowRequestConfirmed', 'Your borrow request has been confirmed.');

-- Notification for a return reminder
INSERT INTO Notification (recipient_id, recipient_type, notification_type, message)
VALUES 
(1, 'Client', 'ReturnReminder', 'Reminder: Return the borrowed book by the due date.');
