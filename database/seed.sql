USE shopsphere;
INSERT IGNORE INTO categories(name,description,status) VALUES('Electronics','Electronic products',TRUE),('Books','Books and study material',TRUE),('Fashion','Clothing and accessories',TRUE);
INSERT INTO products(category_id,name,brand,description,price,discount,stock,status) SELECT category_id,'Java Programming','ShopSphere','Advanced Java reference book',799.00,10,25,TRUE FROM categories WHERE name='Books' LIMIT 1;
INSERT INTO products(category_id,name,brand,description,price,discount,stock,status) SELECT category_id,'Wireless Mouse','ShopSphere','USB wireless mouse',599.00,5,50,TRUE FROM categories WHERE name='Electronics' LIMIT 1;

-- Demo coupon for checkout testing
INSERT INTO coupons(code,discount_type,discount_value,minimum_order,maximum_discount,expiry_date,status)
VALUES ('WELCOME10','PERCENT',10,500,200,DATE_ADD(CURRENT_DATE,INTERVAL 90 DAY),TRUE)
ON DUPLICATE KEY UPDATE status=TRUE, expiry_date=DATE_ADD(CURRENT_DATE,INTERVAL 90 DAY);
