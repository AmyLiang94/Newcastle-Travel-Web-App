CREATE TABLE attractions (
                             attraction_id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             description TEXT,
                             category VARCHAR(255) NOT NULL,
                             latitude DECIMAL(10, 8) NOT NULL,
                             longitude DECIMAL(11, 8) NOT NULL,
                             opening_hours VARCHAR(255),
                             ticket_price DECIMAL(10, 2),
                             image_url VARCHAR(255) NOT NULL,
                             attr_rating DOUBLE NOT NULL,
                             wheelchair_allow BOOLEAN,
                             pram_allow BOOLEAN,
                             hearing_allow BOOLEAN
);
CREATE TABLE reviews (
                         review_id INT AUTO_INCREMENT PRIMARY KEY,
                         attraction_id INT NOT NULL,
                         user_id INT NOT NULL,
                         rating INT NOT NULL,
                         review_text TEXT,
                         create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

);
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(20),
                       created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);