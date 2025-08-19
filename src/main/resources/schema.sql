CREATE TABLE IF NOT EXISTS reviews (
    id INT PRIMARY KEY AUTO_INCREMENT,
    version INT NOT NULL,
    review_id INT NOT NULL,
    product_id INT NOT NULL,
    author VARCHAR(255),
    content TEXT,
    subject VARCHAR(255)
);