CREATE DATABASE IF NOT EXISTS shopsphere;
USE shopsphere;

CREATE TABLE users (
 user_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL,
 email VARCHAR(150) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL,
 mobile VARCHAR(20), role ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
 status BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE addresses (
 address_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT NOT NULL,
 address_line VARCHAR(255) NOT NULL, city VARCHAR(100) NOT NULL,
state VARCHAR(100) NOT NULL, pincode VARCHAR(10) NOT NULL, address_type VARCHAR(30),
FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
CREATE TABLE categories (
category_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL UNIQUE,
description VARCHAR(500), status BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE products (
product_id INT PRIMARY KEY AUTO_INCREMENT, category_id INT NOT NULL,
name VARCHAR(200) NOT NULL, brand VARCHAR(100), description TEXT,
price DECIMAL(12,2) NOT NULL, discount DECIMAL(5,2) DEFAULT 0,
stock INT NOT NULL DEFAULT 0, image_url VARCHAR(500),
 status BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
CREATE TABLE cart (
 cart_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT NOT NULL, product_id INT NOT NULL,
 quantity INT NOT NULL DEFAULT 1, UNIQUE KEY uq_cart_user_product (user_id, product_id),
 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
 FOREIGN KEY (product_id) REFERENCES products(product_id)
);
CREATE TABLE wishlist (
 wishlist_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT NOT NULL, product_id INT NOT NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 UNIQUE KEY uq_wishlist_user_product (user_id, product_id),
 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
 FOREIGN KEY (product_id) REFERENCES products(product_id)
);
CREATE TABLE orders (
 order_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT NOT NULL, address_id INT NOT NULL,
 total_amount DECIMAL(12,2) NOT NULL, discount DECIMAL(12,2) DEFAULT 0,
 payment_method VARCHAR(30), payment_status VARCHAR(30) DEFAULT 'PENDING',
 order_status VARCHAR(30) DEFAULT 'PLACED', created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (user_id) REFERENCES users(user_id), FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);
CREATE TABLE order_items (
 order_item_id INT PRIMARY KEY AUTO_INCREMENT, order_id INT NOT NULL, product_id INT NOT NULL,
 quantity INT NOT NULL, price DECIMAL(12,2) NOT NULL,
 FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
 FOREIGN KEY (product_id) REFERENCES products(product_id)
);
CREATE TABLE payments (
 payment_id INT PRIMARY KEY AUTO_INCREMENT, order_id INT NOT NULL,
 payment_method VARCHAR(30), transaction_reference VARCHAR(100),
 amount DECIMAL(12,2) NOT NULL, payment_status VARCHAR(30) DEFAULT 'PENDING',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);
CREATE TABLE reviews (
 review_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT NOT NULL, product_id INT NOT NULL,
 rating INT NOT NULL, review_text TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (user_id) REFERENCES users(user_id), FOREIGN KEY (product_id) REFERENCES products(product_id)
);
CREATE TABLE coupons (
 coupon_id INT PRIMARY KEY AUTO_INCREMENT, code VARCHAR(50) NOT NULL UNIQUE,
 discount_type VARCHAR(20) NOT NULL, discount_value DECIMAL(10,2) NOT NULL,
 minimum_order DECIMAL(12,2) DEFAULT 0, maximum_discount DECIMAL(12,2),
 expiry_date DATE, status BOOLEAN DEFAULT TRUE
);
CREATE TABLE coupon_usage (
 usage_id INT PRIMARY KEY AUTO_INCREMENT, coupon_id INT NOT NULL, user_id INT NOT NULL,
 order_id INT NOT NULL, used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id),
 FOREIGN KEY (user_id) REFERENCES users(user_id), FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
