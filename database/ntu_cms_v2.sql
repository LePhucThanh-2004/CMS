-- Create database
CREATE DATABASE IF NOT EXISTS cms
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
USE cms;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name NVARCHAR(100),
    role ENUM('ADMIN', 'EDITOR', 'USER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- User Roles table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tags table
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Articles table
CREATE TABLE IF NOT EXISTS articles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title NVARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Article_Tags table (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS article_tags (
    article_id BIGINT,
    tag_id BIGINT,
    PRIMARY KEY (article_id, tag_id),
    FOREIGN KEY (article_id) REFERENCES articles(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);

-- Comments table
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    article_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES articles(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Media table
CREATE TABLE IF NOT EXISTS media (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    uploaded_by BIGINT NOT NULL,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
);

-- Search History table
CREATE TABLE IF NOT EXISTS search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(255) NOT NULL,
    user_id BIGINT,
    search_count BIGINT DEFAULT 1,
    last_searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Insert sample users (password is 'password' encoded with BCrypt)
INSERT INTO users (username, password, email, full_name, role) VALUES
('admin', '$2a$10$ZLhnHxdpHETcxmtUz8Kpt.KeKxZFGWL1U5YZrQrwqEGGgU1J97h4O', 'admin@ntu.edu.vn', 'Admin User', 'ADMIN'),
('editor', '$2a$10$ZLhnHxdpHETcxmtUz8Kpt.KeKxZFGWL1U5YZrQrwqEGGgU1J97h4O', 'editor@ntu.edu.vn', 'Editor User', 'EDITOR'),
('user', '$2a$10$ZLhnHxdpHETcxmtUz8Kpt.KeKxZFGWL1U5YZrQrwqEGGgU1J97h4O', 'user@ntu.edu.vn', 'Normal User', 'USER');

-- Insert user roles
INSERT INTO user_roles (user_id, role) VALUES
(1, 'ADMIN'),
(2, 'EDITOR'),
(3, 'USER');

-- Insert sample categories
INSERT INTO categories (name, description) VALUES
('Tin tức', 'Các tin tức mới nhất'),
('Thông báo', 'Các thông báo quan trọng'),
('Sự kiện', 'Các sự kiện sắp diễn ra');

-- Insert sample tags
INSERT INTO tags (name) VALUES
('Tuyển sinh'),
('Học bổng'),
('Nghiên cứu'),
('Đào tạo');

-- Insert sample articles
INSERT INTO articles (title, content, author_id, category_id, status) VALUES
('Thông báo tuyển sinh năm 2024', 'Nội dung chi tiết về thông báo tuyển sinh năm 2024...', 1, 2, 'PUBLISHED'),
('Lễ tốt nghiệp khóa 2023', 'Nội dung chi tiết về lễ tốt nghiệp khóa 2023...', 2, 3, 'PUBLISHED');

-- Insert article tags
INSERT INTO article_tags (article_id, tag_id) VALUES
(1, 1), -- Thông báo tuyển sinh - Tag tuyển sinh
(2, 4); -- Lễ tốt nghiệp - Tag đào tạo

-- Insert sample comments
INSERT INTO comments (content, article_id, user_id) VALUES
('Bao giờ có thông tin chi tiết về các ngành tuyển sinh?', 1, 3),
('Chúc mừng các tân cử nhân!', 2, 2); 