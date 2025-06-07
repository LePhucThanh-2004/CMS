-- Drop database if exists
DROP DATABASE IF EXISTS cms;

-- Create database with UTF-8 encoding
CREATE DATABASE cms
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE cms;

-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name NVARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- User Roles table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Categories table
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tags table
CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Articles table
CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    content TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    excerpt TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') NOT NULL DEFAULT 'DRAFT',
    featured_image VARCHAR(255),
    author_id BIGINT NOT NULL,
    category_id BIGINT,
    view_count BIGINT DEFAULT 0,
    published_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Article_Tags table
CREATE TABLE article_tags (
    article_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comments table
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    article_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_id BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Media table
CREATE TABLE media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name NVARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL,
    uploaded_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Search History table
CREATE TABLE search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    query NVARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Persistent Logins table
CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) PRIMARY KEY,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample users (password: admin123 encoded with BCrypt)
INSERT INTO users (username, password, email, full_name, enabled) VALUES
('admin', '$2a$10$rS.FGQXq8JMhB5ElnlPG8.g9fXJLzPqh9F5ZVqCXTYKJHxwNVa3.e', 'admin@ntu.edu.vn', N'Quản trị viên', true),
('editor', '$2a$10$rS.FGQXq8JMhB5ElnlPG8.g9fXJLzPqh9F5ZVqCXTYKJHxwNVa3.e', 'editor@ntu.edu.vn', N'Biên tập viên', true),
('user', '$2a$10$rS.FGQXq8JMhB5ElnlPG8.g9fXJLzPqh9F5ZVqCXTYKJHxwNVa3.e', 'user@ntu.edu.vn', N'Người dùng', true);

-- Insert user roles
INSERT INTO user_roles (user_id, role) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_EDITOR'),
(3, 'ROLE_USER');

-- Insert sample categories
INSERT INTO categories (name, description) VALUES
(N'Tin tức', N'Tin tức chung của khoa'),
(N'Thông báo', N'Thông báo từ khoa'),
(N'Sự kiện', N'Các sự kiện của khoa');

-- Insert sample tags
INSERT INTO tags (name) VALUES
(N'Tuyển sinh'),
(N'Đào tạo'),
(N'Nghiên cứu'),
(N'Sinh viên'),
(N'Giảng viên');

-- Insert sample articles
INSERT INTO articles (title, slug, content, excerpt, status, author_id, category_id, published_at) VALUES
(N'Thông báo tuyển sinh năm 2025', 'thong-bao-tuyen-sinh-2025', 
N'Khoa Công nghệ Thông tin - Trường Đại học Nha Trang thông báo tuyển sinh năm 2025. 
Các ngành đào tạo bao gồm:
1. Công nghệ thông tin
2. Kỹ thuật phần mềm
3. An toàn thông tin', 
N'Thông tin tuyển sinh năm 2025 của Khoa CNTT', 
'PUBLISHED', 1, 2, CURRENT_TIMESTAMP),

(N'Lễ tốt nghiệp khóa 2024', 'le-tot-nghiep-2024', 
N'Lễ tốt nghiệp của sinh viên khóa 2024 sẽ được tổ chức vào ngày 30/06/2024 tại Hội trường lớn.
Sinh viên cần chuẩn bị:
1. Lễ phục tốt nghiệp
2. Thẻ sinh viên
3. Giấy tờ tùy thân', 
N'Thông tin về lễ tốt nghiệp khóa 2024', 
'PUBLISHED', 2, 3, CURRENT_TIMESTAMP);

-- Insert article tags
INSERT INTO article_tags (article_id, tag_id) VALUES
(1, 1), -- Bài viết tuyển sinh - tag Tuyển sinh
(1, 2), -- Bài viết tuyển sinh - tag Đào tạo
(2, 4), -- Bài viết tốt nghiệp - tag Sinh viên
(2, 5); -- Bài viết tốt nghiệp - tag Giảng viên

-- Insert sample comments
INSERT INTO comments (content, article_id, user_id) VALUES
(N'Thông tin rất hữu ích cho các bạn sinh viên mới!', 1, 3),
(N'Chúc mừng các tân kỹ sư, cử nhân!', 2, 2); 