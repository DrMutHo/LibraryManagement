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

-- Example structure for storing file paths
CREATE TABLE IF NOT EXISTS Book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    genre VARCHAR(100),
    language VARCHAR(50),
    description TEXT,
    publication_year INT,
    image_path VARCHAR(255),  
    average_rating DECIMAL(3, 2) DEFAULT NULL,
    review_count INT DEFAULT 0
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

-- Table for Book Reviews
CREATE TABLE IF NOT EXISTS BookReview (
    review_id INT AUTO_INCREMENT PRIMARY KEY,        -- Unique ID for each review
    book_id INT,                                     -- Foreign key referencing the book being reviewed
    client_id INT,                                   -- Foreign key referencing the client who wrote the review
    rating DECIMAL(2, 1) CHECK (rating BETWEEN 1 AND 5), -- Rating out of 5
    comment TEXT,                                    -- Comment on the book
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,  -- Date when the review was submitted
    FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES Client(client_id) ON DELETE CASCADE
);

ALTER TABLE BookReview
ADD CONSTRAINT unique_book_client UNIQUE (book_id, client_id);



-- Insert sample data into Client with securely hashed passwords
-- Passwords are hashed using bcrypt

INSERT INTO Client (name, library_card_number, email, phone_number, address, username, password_hash, outstanding_fees)
VALUES 
('John Doe', 'LIB00001', 'johndoe@gmail.com', '1234567890', '123 Elm Street, Springfield', 'johndoe', '$2a$10$60/lFatFhuWE/4h0kChDWe7b8pUkvfcd0mwYX6eWZXPH4cI4OPB12', 0.00), -- original password: "password123"
('Jane Smith', 'LIB00002', 'janesmith@yahoo.com', '0987654321', '456 Maple Street, Shelbyville', 'janesmith', '$2a$10$rCI5.vG.7.SLzf6eWEQzrO9sH09r5NtX1BgwgOICJqhbPL4NGaeae', 10.50), -- original password: "mypassword456"
('Alice Johnson', 'LIB00003', 'alicej@outlook.com', '5678901234', '789 Oak Street, Capital City', 'alicejohnson', '$2a$10$4zeJWH.2uA.fBcLZKZV/FuhFo98FbSUIwd0Zlv/4m1jL7JlBeP7o.', 0.00), -- original password: "alice2023"
('Michael Brown', 'LIB00004', 'michaelb@gmail.com', '6789012345', '123 Pine Street, Gotham', 'michaelb', '$2a$10$3KqWeDlw1cp2V2bH20l7LuUefsZhm679QzeVN0UklPBmed1OPjRoi', 5.00), -- original password: "brownpassword"
('Emily White', 'LIB00005', 'emilywhite@hotmail.com', '7890123456', '456 Cedar Street, Metropolis', 'emilywhite', '$2a$10$PqIMZpqpMu4eF75EyzO2m.ehgt5eUVYXo2uXW6Oa6tzIhK6hL/8nS', 0.00), -- original password: "emilyW123"
('James Green', 'LIB00006', 'jamesg@gmail.com', '8901234567', '789 Birch Street, Star City', 'jamesgreen', '$2a$10$8IR4riYihCEtZ0pWWq/zu.Qqze.s39P.HDDsQ3MtsGm5wv6LjZikO', 20.00), -- original password: "jgreen2023"
('Linda Black', 'LIB00007', 'lindablack@gmail.com', '9012345678', '101 Elm Street, Central City', 'lindablack', '$2a$10$PMfA9xPtIb871mjZ5OCzc.aTCYefb7oQJrzhixBFqz8NrkF9bi.g6', 0.00), -- original password: "blacklinda"
('David Wilson', 'LIB00008', 'davidw@hotmail.com', '1123456789', '202 Oak Street, Smallville', 'davidwilson', '$2a$10$v77NUqXDlokqptZidXwqJuAsCArzT7.lujuTOJqHaoml6A/Fc201m', 10.00), -- original password: "dwilson"
('Sophia Miller', 'LIB00009', 'sophiam@yahoo.com', '2234567890', '303 Maple Street, Emerald City', 'sophiamiller', '$2a$10$NnHZfBHU8h8eHXQ1cVvsou/SK0/MQuemtoCWZwvVt/VMJ2Sb1Hk0a', 0.00), -- original password: "sophiam123"
('Daniel Martinez', 'LIB00010', 'danielm@gmail.com', '3345678901', '404 Cedar Street, Westview', 'danielmartinez', '$2a$10$lNQn/Bgh10D2qW529XnV..DiI26mrQh5euR.E4Hgak0EVutEAppWy', 7.50), -- original password: "danmartinez"
('Olivia Davis', 'LIB00011', 'oliviad@gmail.com', '4456789012', '505 Birch Street, Hill Valley', 'oliviadavis', '$2a$10$PrfB.YVd7z5E/.q1xjM7IuNpg.Ae9kTnj/Ylg8OQiYneogTbJlYq.', 0.00), -- original password: "olivia2023"
('Ethan Garcia', 'LIB00012', 'ethang@gmail.com', '5567890123', '606 Pine Street, Riverdale', 'ethangarcia', '$2a$10$ZMEsg0s4Z/5GV2VZ5N8O7u.eZh8zI4mchEd1sv9ql2rzQiuZcaumm', 5.00), -- original password: "ethanpass"
('Isabella Lee', 'LIB00013', 'isabellal@hotmail.com', '6678901234', '707 Elm Street, Pleasantville', 'isabellalee', '$2a$10$rNRn86UP2U9n.RO771wg2ebmHy1VaD33Y4NvrJLfAF5SxClnfcNLW', 0.00), -- original password: "isaleepass"
('Alexander Thompson', 'LIB00014', 'alexandert@gmail.com', '7789012345', '808 Maple Street, Whoville', 'alexthompson', '$2a$10$YNZbLdYuoQ5q1MvUSE3ReuXSLfQZFW2ypU1.llHQzyxkuZxMWIPxO', 2.00), -- original password: "alexpass123"
('Mia Robinson', 'LIB00015', 'miarobinson@yahoo.com', '8890123456', '909 Cedar Street, Atlantis', 'miarobinson', '$2a$10$zbApWQ/FaF63TtyCTyediuU1jMV.hXtBr1tde1Ymql6uItfMsiL7q', 10.00), -- original password: "miarobinson"
('Benjamin Clark', 'LIB00016', 'benjaminclark@gmail.com', '9901234567', '101 Elm Street, Arendelle', 'benjaminclark', '$2a$10$aEInb9xPDTGhDt3/jLAv4Oj2gn7PzboaN2Kk9C5BpulTOiQKUtLvq', 3.50), -- original password: "benjaminpass"
('Ava Wright', 'LIB00017', 'avawright@hotmail.com', '1234567891', '202 Oak Street, Berk', 'avawright', '$2a$10$EEwjlPV6nCeiXiM7AL5wGuYB3cA1Z..zQNdnSwBHgFEW4lQKaS9Pu', 0.00), -- original password: "avawright"
('Logan Walker', 'LIB00018', 'loganwalker@gmail.com', '2345678902', '303 Birch Street, Asgard', 'loganwalker', '$2a$10$JKh4CrjAljQnCH734O3squZ3fCCWTNYbmKSYXNKlBdNMl20H6t54q', 5.75), -- original password: "logan123"
('Charlotte Harris', 'LIB00019', 'charlotteh@gmail.com', '3456789013', '404 Maple Street, Wakanda', 'charlotteharris', '$2a$10$C9h.CwA42GOd.WZiyzoMlOwZ6dLmEyDZAbWPYgjwBzGJipGz9CNfm', 8.00), -- original password: "charris2023"
('Mason Young', 'LIB00020', 'masonyoung@gmail.com', '4567890124', '505 Pine Street, Neverland', 'masonyoung', '$2a$10$m6rrutRFEYoBQmNJpIEV6.qOxYwcajkRTi259WU2YlCDgvuluyVwO', 0.00); -- original password: "masonyoung"


-- Insert sample data into Admin with hashed passwords
INSERT INTO Admin (username, password_hash, email)
VALUES 
('admin1', '$2a$10$Ei3D2hj3KSZKmQeQx8tShu1Q/tfiN/FzXzPTpRC.vhh1AqPnLGmHG', 'admin1@library.com'),  -- password123
('admin2', '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.O.IJhhJIGtvn8u8FVbONZXxrZqPB8Pe', 'admin2@library.com');  -- mypassword456

-- Insert sample data into the Book table
INSERT INTO Book (title, author, isbn, genre, language, description, publication_year, image_path, average_rating, review_count) 
VALUES
('The Catcher in the Rye', 'J.D. Salinger', '9780316769488', 'Fiction', 'English', 'A story about adolescent Holden Caulfield\'s disillusionment.', 1951, '/resources/Images/Books/1.jpg', 0, 0),
('1984', 'George Orwell', '9780451524935', 'Dystopian', 'English', 'A novel set in a dystopian future with totalitarian control.', 1949, '/resources/Images/Books/2.jpg', 0, 0),
('Pride and Prejudice', 'Jane Austen', '9781503290563', 'Romance', 'English', 'A romance novel centered around Elizabeth Bennet.', 1813, '/resources/Images/Books/3.jpg', 0, 0),
('Moby-Dick', 'Herman Melville', '9781503280786', 'Adventure', 'English', 'A sailor\'s quest for revenge on the white whale, Moby Dick.', 1851, '/resources/Images/Books/4.jpg', 0, 0),
('War and Peace', 'Leo Tolstoy', '9780307266934', 'Historical', 'Russian', 'A sweeping epic of Russian life during the Napoleonic Wars.', 1869, '/resources/Images/Books/5.jpg', 0, 0),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Fiction', 'English', 'A story of racial injustice in the Deep South.', 1960, '/resources/Images/Books/6.jpg', 0, 0),
('Great Expectations', 'Charles Dickens', '9780141439563', 'Fiction', 'English', 'The journey of Pip from a young orphan to a gentleman.', 1861, '/resources/Images/Books/7.jpg', 0, 0),
('The Hobbit', 'J.R.R. Tolkien', '9780345339683', 'Fantasy', 'English', 'Bilbo Baggins\' adventure with dwarves and a dragon.', 1937, '/resources/Images/Books/8.jpg', 0, 0),
('Crime and Punishment', 'Fyodor Dostoevsky', '9780140449136', 'Philosophical', 'Russian', 'The psychological journey of Raskolnikov after a murder.', 1866, '/resources/Images/Books/9.jpg', 0, 0),
('The Divine Comedy', 'Dante Alighieri', '9780140448955', 'Epic', 'Italian', 'Dante\'s journey through Hell, Purgatory, and Heaven.', 1320, '/resources/Images/Books/10.jpg', 0, 0),
('Brave New World', 'Aldous Huxley', '9780060850524', 'Dystopian', 'English', 'A futuristic society driven by technology and conditioning.', 1932, '/resources/Images/Books/11.jpg', 0, 0),
('Ulysses', 'James Joyce', '9780199535675', 'Modernist', 'English', 'A day in the life of Leopold Bloom in Dublin.', 1922, '/resources/Images/Books/12.jpg', 0, 0),
('The Iliad', 'Homer', '9780140275360', 'Epic', 'Greek', 'An epic tale of the Trojan War and the hero Achilles.', -750, '/resources/Images/Books/13.jpg', 0, 0),
('The Odyssey', 'Homer', '9780140268867', 'Epic', 'Greek', 'Odysseus\' journey home from the Trojan War.', -720, '/resources/Images/Books/14.jpg', 0, 0),
('Anna Karenina', 'Leo Tolstoy', '9780140449174', 'Romance', 'Russian', 'A tragic love story set in Russian society.', 1878, '/resources/Images/Books/15.jpg', 0, 0),
('Don Quixote', 'Miguel de Cervantes', '9780060934347', 'Adventure', 'Spanish', 'The adventures of a self-declared knight-errant.', 1605, '/resources/Images/Books/16.jpg', 0, 0),
('Madame Bovary', 'Gustave Flaubert', '9780199535651', 'Romance', 'French', 'A story of desire and disillusionment.', 1856, '/resources/Images/Books/17.jpg', 0, 0),
('The Brothers Karamazov', 'Fyodor Dostoevsky', '9780374528379', 'Philosophical', 'Russian', 'A philosophical exploration of faith, doubt, and morality.', 1880, '/resources/Images/Books/18.jpg', 0, 0),
('Wuthering Heights', 'Emily Brontë', '9780141439556', 'Gothic', 'English', 'A story of intense love and revenge.', 1847, '/resources/Images/Books/19.jpg', 0, 0),
('Jane Eyre', 'Charlotte Brontë', '9780141441146', 'Romance', 'English', 'A young woman\'s resilience in love and life.', 1847, '/resources/Images/Books/20.jpg', 0, 0),
('The Grapes of Wrath', 'John Steinbeck', '9780143039433', 'Historical', 'English', 'A tale of hardship during the Great Depression.', 1939, '/resources/Images/Books/21.jpg', 0, 0),
('Frankenstein', 'Mary Shelley', '9780141439471', 'Gothic', 'English', 'A scientist\'s creation goes awry, with horrific results.', 1818, '/resources/Images/Books/22.jpg', 0, 0),
('One Hundred Years of Solitude', 'Gabriel Garcia Marquez', '9780060883287', 'Magic Realism', 'Spanish', 'A family saga with magical realism.', 1967, '/resources/Images/Books/23.jpg', 0, 0),
('The Metamorphosis', 'Franz Kafka', '9781557427663', 'Existential', 'German', 'A man wakes up transformed into a giant insect.', 1915, '/resources/Images/Books/24.jpg', 0, 0),
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Fiction', 'English', 'A tale of wealth, love, and the American Dream.', 1925, '/resources/Images/Books/25.jpg', 0, 0),
('Lolita', 'Vladimir Nabokov', '9780679723165', 'Literature', 'English', 'A controversial novel about forbidden obsession.', 1955, '/resources/Images/Books/26.jpg', 0, 0),
('The Trial', 'Franz Kafka', '9780805209990', 'Existential', 'German', 'A man is arrested and prosecuted without knowing why.', 1925, '/resources/Images/Books/27.jpg', 0, 0),
('Catch-22', 'Joseph Heller', '9780684833392', 'War', 'English', 'A satirical novel about WWII airmen and bureaucracy.', 1961, '/resources/Images/Books/28.jpg', 0, 0),
('Beloved', 'Toni Morrison', '9781400033416', 'Historical', 'English', 'A ghost haunts a former slave in post-Civil War America.', 1987, '/resources/Images/Books/29.jpg', 0, 0),
('Slaughterhouse-Five', 'Kurt Vonnegut', '9780385333849', 'Science Fiction', 'English', 'A surreal story about WWII and time travel.', 1969, '/resources/Images/Books/30.jpg', 0, 0);

-- Insert sample data into BookCopy
INSERT INTO BookCopy (book_id, is_available, book_condition)
VALUES 
(1, TRUE, 'Good'),
(1, FALSE, 'Fair'),
(2, TRUE, 'New'),
(2, FALSE, 'Poor'),
(3, TRUE, 'Good'),
(3, TRUE, 'New'),
(3, FALSE, 'Fair'),
(4, TRUE, 'Good'),
(4, FALSE, 'Poor'),
(5, TRUE, 'New'),
(5, TRUE, 'Good'),
(6, TRUE, 'Fair'),
(6, FALSE, 'Poor'),
(7, TRUE, 'New'),
(7, TRUE, 'Good'),
(8, FALSE, 'Fair'),
(8, TRUE, 'Good'),
(9, FALSE, 'Poor'),
(9, TRUE, 'New'),
(10, TRUE, 'Good'),
(10, FALSE, 'Fair'),
(11, TRUE, 'Good'),
(11, FALSE, 'New'),
(12, TRUE, 'Poor'),
(12, FALSE, 'Fair'),
(13, TRUE, 'New'),
(13, TRUE, 'Good'),
(14, FALSE, 'Fair'),
(14, TRUE, 'Good'),
(15, FALSE, 'Poor'),
(15, TRUE, 'New');

-- Insert sample data into BorrowTransaction
INSERT INTO BorrowTransaction (client_id, copy_id, borrow_date, return_date, status)
VALUES
(1, 1, '2024-01-15', '2024-01-22', 'Done'),
(2, 2, '2024-02-10', '2024-02-17', 'Done'),
(3, 3, '2024-03-01', '2024-03-08', 'Done'),
(4, 4, '2024-03-15', '2024-03-22', 'Done'),
(5, 5, '2024-04-05', '2024-04-12', 'Done'),
(1, 6, '2024-04-10', '2024-04-17', 'Done'),
(2, 7, '2024-05-01', '2024-05-08', 'Done'),
(3, 8, '2024-05-20', '2024-05-27', 'Done'),
(4, 9, '2024-06-10', '2024-06-17', 'Done'),
(5, 10, '2024-06-15', '2024-06-22', 'Done'),
(6, 11, '2024-07-01', '2024-07-08', 'Done'),
(7, 12, '2024-07-10', '2024-07-17', 'Done'),
(8, 13, '2024-08-01', '2024-08-08', 'Done'),
(9, 14, '2024-08-15', '2024-08-22', 'Done'),
(10, 15, '2024-09-01', '2024-09-08', 'Done'),
(6, 16, '2024-09-10', '2024-09-17', 'Done'),
(7, 17, '2024-10-01', '2024-10-08', 'Done'),
(8, 18, '2024-10-15', '2024-10-22', 'Done'),
(9, 19, '2024-11-01', '2024-11-08', 'Done'),
(10, 20, '2024-11-10', '2024-11-17', 'Done'),
(11, 21, '2024-12-01', '2024-12-08', 'Done'),
(12, 22, '2024-12-15', '2024-12-22', 'Done'),
(13, 23, '2025-01-01', '2025-01-08', 'Done'),
(14, 24, '2025-01-10', '2025-01-17', 'Done'),
(15, 25, '2025-02-01', '2025-02-08', 'Done'),
(11, 26, '2025-02-15', '2025-02-22', 'Done'),
(12, 27, '2025-03-01', '2025-03-08', 'Done'),
(13, 28, '2025-03-15', '2025-03-22', 'Done'),
(14, 29, '2025-04-01', '2025-04-08', 'Done'),
(15, 30, '2025-04-15', '2025-04-22', 'Done');


-- Insert sample data into NotificationRequest
INSERT INTO NotificationRequest (client_id, book_id, request_date)
VALUES
(1, 1, '2024-01-10 14:30:00'),
(2, 2, '2024-01-15 09:15:00'),
(3, 3, '2024-02-01 11:45:00'),
(4, 4, '2024-02-10 16:00:00'),
(5, 5, '2024-03-05 08:20:00'),
(6, 6, '2024-03-12 14:10:00'),
(7, 7, '2024-03-25 10:30:00'),
(8, 8, '2024-04-01 15:45:00'),
(9, 9, '2024-04-15 17:05:00'),
(10, 10, '2024-05-01 12:25:00'),
(1, 2, '2024-05-10 10:15:00'),
(2, 3, '2024-05-20 13:45:00'),
(3, 4, '2024-06-01 09:30:00'),
(4, 5, '2024-06-10 14:50:00'),
(5, 6, '2024-07-01 16:30:00'),
(6, 7, '2024-07-15 11:40:00'),
(7, 8, '2024-08-01 08:55:00'),
(8, 9, '2024-08-10 18:15:00'),
(9, 10, '2024-09-01 14:25:00'),
(10, 1, '2024-09-15 09:05:00'),
(1, 3, '2024-09-30 13:35:00'),
(2, 4, '2024-10-10 10:50:00'),
(3, 5, '2024-10-20 15:30:00'),
(4, 6, '2024-11-01 16:10:00'),
(5, 7, '2024-11-15 08:20:00'),
(6, 8, '2024-12-01 17:40:00'),
(7, 9, '2024-12-10 10:30:00'),
(8, 10, '2025-01-01 13:15:00'),
(9, 1, '2025-01-10 11:55:00'),
(10, 2, '2025-01-20 16:20:00');


-- Insert sample data into BorrowReceipt
INSERT INTO BorrowReceipt (transaction_id, issued_date, issued_by)
VALUES
(1, '2024-01-10 10:30:00', 1),
(2, '2024-01-15 09:45:00', 2),
(3, '2024-02-01 14:20:00', 1),
(4, '2024-02-10 11:05:00', 2),
(5, '2024-03-05 08:10:00', 1),
(6, '2024-03-12 13:30:00', 2),
(7, '2024-03-25 15:25:00', 1),
(8, '2024-04-01 16:50:00', 2),
(9, '2024-04-15 10:15:00', 1),
(10, '2024-05-01 11:40:00', 2),
(11, '2024-05-10 12:35:00', 1),
(12, '2024-05-20 14:10:00', 2),
(13, '2024-06-01 09:05:00', 1),
(14, '2024-06-10 16:30:00', 2),
(15, '2024-07-01 08:45:00', 1),
(16, '2024-07-15 10:50:00', 2),
(17, '2024-08-01 13:20:00', 1),
(18, '2024-08-10 11:30:00', 2),
(19, '2024-09-01 17:25:00', 1),
(20, '2024-09-15 15:55:00', 2),
(21, '2024-09-30 14:05:00', 1),
(22, '2024-10-10 09:40:00', 2),
(23, '2024-10-20 13:50:00', 1),
(24, '2024-11-01 10:10:00', 2),
(25, '2024-11-15 12:30:00', 1),
(26, '2024-12-01 15:05:00', 2),
(27, '2024-12-10 14:55:00', 1),
(28, '2025-01-01 09:30:00', 2),
(29, '2025-01-10 11:20:00', 1),
(30, '2025-01-20 16:00:00', 2);


-- Insert sample data into ReturnReceipt
INSERT INTO ReturnReceipt (transaction_id, return_date, received_by, condition_on_return)
VALUES
(1, '2024-01-20 10:00:00', 1, 'Good'),
(2, '2024-01-25 09:30:00', 2, 'Fair'),
(3, '2024-02-10 13:45:00', 1, 'Good'),
(4, '2024-02-20 11:00:00', 2, 'New'),
(5, '2024-03-10 08:25:00', 1, 'Poor'),
(6, '2024-03-15 13:00:00', 2, 'Good'),
(7, '2024-03-30 15:45:00', 1, 'Fair'),
(8, '2024-04-05 17:10:00', 2, 'Good'),
(9, '2024-04-25 10:20:00', 1, 'New'),
(10, '2024-05-10 11:50:00', 2, 'Fair'),
(11, '2024-05-20 12:45:00', 1, 'Poor'),
(12, '2024-05-30 14:30:00', 2, 'Good'),
(13, '2024-06-05 09:15:00', 1, 'Fair'),
(14, '2024-06-20 16:00:00', 2, 'Good'),
(15, '2024-07-05 08:30:00', 1, 'Poor'),
(16, '2024-07-20 10:45:00', 2, 'Good'),
(17, '2024-08-05 13:10:00', 1, 'New'),
(18, '2024-08-15 11:15:00', 2, 'Good'),
(19, '2024-09-05 17:00:00', 1, 'Fair'),
(20, '2024-09-20 16:25:00', 2, 'Poor'),
(21, '2024-09-30 14:35:00', 1, 'Good'),
(22, '2024-10-15 09:10:00', 2, 'New'),
(23, '2024-10-25 13:45:00', 1, 'Good'),
(24, '2024-11-05 10:00:00', 2, 'Poor'),
(25, '2024-11-20 12:25:00', 1, 'Fair'),
(26, '2024-12-05 15:35:00', 2, 'Good'),
(27, '2024-12-15 14:20:00', 1, 'New'),
(28, '2025-01-05 09:40:00', 2, 'Fair'),
(29, '2025-01-15 11:30:00', 1, 'Good'),
(30, '2025-01-25 16:15:00', 2, 'Poor');



-- Insert sample data into Notification
INSERT INTO Notification (recipient_id, recipient_type, notification_type, message)
VALUES 
(1, 'Admin', 'ReturnReceiptIssued', 'A return receipt has been issued for Transaction ID 1.'),
(2, 'Admin', 'ReturnReceiptIssued', 'A return receipt has been issued for Transaction ID 2.'),
(1, 'Admin', 'OverdueAlert', 'A book with Transaction ID 3 is overdue.'),
(3, 'Admin', 'OverdueAlert', 'A book with Transaction ID 4 is overdue.'),
(2, 'Client', 'BorrowRequestConfirmed', 'Your borrow request for Book ID 201 has been confirmed.'),
(4, 'Client', 'BorrowRequestConfirmed', 'Your borrow request for Book ID 202 has been confirmed.'),
(1, 'Client', 'ReturnReminder', 'Reminder: Return the borrowed book by the due date for Transaction ID 5.'),
(3, 'Client', 'ReturnReminder', 'Reminder: Return the borrowed book by the due date for Transaction ID 6.'),
(2, 'Admin', 'BorrowReceiptIssued', 'A borrow receipt has been issued for Transaction ID 7.'),
(1, 'Client', 'BorrowRequestConfirmed', 'Your borrow request for Book ID 203 has been confirmed.'),
(2, 'Admin', 'ReturnReceiptIssued', 'A return receipt has been issued for Transaction ID 8.'),
(4, 'Client', 'OverdueAlert', 'Your borrowed book with Transaction ID 9 is overdue.'),
(3, 'Admin', 'PurchaseReceiptIssued', 'A new purchase receipt has been issued for Book ID 204.'),
(5, 'Client', 'ReturnReminder', 'Reminder: Return the borrowed book by the due date for Transaction ID 10.'),
(1, 'Admin', 'OverdueAlert', 'A book with Transaction ID 11 is overdue.'),
(6, 'Client', 'BorrowRequestConfirmed', 'Your borrow request for Book ID 205 has been confirmed.'),
(2, 'Admin', 'BorrowReceiptIssued', 'A borrow receipt has been issued for Transaction ID 12.'),
(3, 'Client', 'ReturnReminder', 'Reminder: Return the borrowed book by the due date for Transaction ID 13.'),
(1, 'Admin', 'PurchaseReceiptIssued', 'A new purchase receipt has been issued for Book ID 206.'),
(7, 'Client', 'OverdueAlert', 'Your borrowed book with Transaction ID 14 is overdue.'),
(8, 'Client', 'BorrowRequestConfirmed', 'Your borrow request for Book ID 207 has been confirmed.'),
(4, 'Admin', 'ReturnReceiptIssued', 'A return receipt has been issued for Transaction ID 15.'),
(9, 'Client', 'ReturnReminder', 'Reminder: Return the borrowed book by the due date for Transaction ID 16.'),
(2, 'Admin', 'BorrowReceiptIssued', 'A borrow receipt has been issued for Transaction ID 17.'),
(5, 'Client', 'OverdueAlert', 'Your borrowed book with Transaction ID 18 is overdue.'),
(6, 'Admin', 'PurchaseReceiptIssued', 'A new purchase receipt has been issued for Book ID 208.'),
(1, 'Client', 'ReturnReminder', 'Reminder: Return the borrowed book by the due date for Transaction ID 19.'),
(10, 'Admin', 'BorrowRequestConfirmed', 'Borrow request for Book ID 209 has been confirmed.'),
(3, 'Admin', 'OverdueAlert', 'A book with Transaction ID 20 is overdue.'),
(7, 'Client', 'BorrowRequestConfirmed', 'Your borrow request for Book ID 210 has been confirmed.');

