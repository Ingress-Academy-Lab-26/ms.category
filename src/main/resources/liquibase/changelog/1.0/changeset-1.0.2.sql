-- 1. Insert the categories in a tree structure
-- Top-level category
INSERT INTO categories (name, parent_id) VALUES ('Electronics', NULL);

-- Subcategories of Electronics
INSERT INTO categories (name, parent_id) VALUES ('Mobile Phones', 1);    -- id = 2
INSERT INTO categories (name, parent_id) VALUES ('Televisions', 1);     -- id = 3
INSERT INTO categories (name, parent_id) VALUES ('Laptops', 1);         -- id = 4
INSERT INTO categories (name, parent_id) VALUES ('Cameras', 1);         -- id = 5

-- Subcategories of Mobile Phones
INSERT INTO categories (name, parent_id) VALUES ('Smartphones', 2);     -- id = 6
INSERT INTO categories (name, parent_id) VALUES ('Feature Phones', 2);  -- id = 7

-- Subcategories of Televisions
INSERT INTO categories (name, parent_id) VALUES ('LED TVs', 3);         -- id = 8
INSERT INTO categories (name, parent_id) VALUES ('Smart TVs', 3);       -- id = 9
INSERT INTO categories (name, parent_id) VALUES ('OLED TVs', 3);        -- id = 10

-- Subcategories of Laptops
INSERT INTO categories (name, parent_id) VALUES ('Gaming Laptops', 4);  -- id = 11
INSERT INTO categories (name, parent_id) VALUES ('Ultrabooks', 4);      -- id = 12
INSERT INTO categories (name, parent_id) VALUES ('Business Laptops', 4); -- id = 13

-- Subcategories of Cameras
INSERT INTO categories (name, parent_id) VALUES ('DSLR', 5);            -- id = 14
INSERT INTO categories (name, parent_id) VALUES ('Mirrorless', 5);      -- id = 15
INSERT INTO categories (name, parent_id) VALUES ('Point and Shoot', 5); -- id = 16

-- Top-level category: Hone Appliances
INSERT INTO categories (name, parent_id) VALUES ('Home Appliances', NULL); -- id = 17

-- Subcategories of Home Appliances
INSERT INTO categories (name, parent_id) VALUES ('Refrigerators', 17);   -- id = 18
INSERT INTO categories (name, parent_id) VALUES ('Washing Machines', 17); -- id = 19
INSERT INTO categories (name, parent_id) VALUES ('Microwaves', 17);      -- id = 20
INSERT INTO categories (name, parent_id) VALUES ('Air Conditioners', 17); -- id = 21


-- Top-level category: Books
INSERT INTO categories (name, parent_id) VALUES ('Books', NULL);           -- id = 22

-- Subcategories of Books
INSERT INTO categories (name, parent_id) VALUES ('Fiction', 22);           -- id = 23
INSERT INTO categories (name, parent_id) VALUES ('Non-Fiction', 22);       -- id = 24
INSERT INTO categories (name, parent_id) VALUES ('Children Books', 22); -- id = 25
INSERT INTO categories (name, parent_id) VALUES ('Educational', 22);       -- id = 26

-- Subcategories of Fiction
INSERT INTO categories (name, parent_id) VALUES ('Mystery', 23);           -- id = 27
INSERT INTO categories (name, parent_id) VALUES ('Romance', 23);           -- id = 28
INSERT INTO categories (name, parent_id) VALUES ('Science Fiction', 23);   -- id = 29
INSERT INTO categories (name, parent_id) VALUES ('Fantasy', 23);           -- id = 30

-- Subcategories of Non-Fiction
INSERT INTO categories (name, parent_id) VALUES ('Biographies', 24);       -- id = 31
INSERT INTO categories (name, parent_id) VALUES ('Self-Help', 24);         -- id = 32
INSERT INTO categories (name, parent_id) VALUES ('Health & Wellness', 24); -- id = 33
INSERT INTO categories (name, parent_id) VALUES ('Business', 24);          -- id = 34

-- Subcategories of Children's Books
INSERT INTO categories (name, parent_id) VALUES ('Picture Books', 25);     -- id = 35
INSERT INTO categories (name, parent_id) VALUES ('Young Adult', 25);       -- id = 36
INSERT INTO categories (name, parent_id) VALUES ('Early Readers', 25);     -- id = 37

-- Subcategories of Educational
INSERT INTO categories (name, parent_id) VALUES ('Textbooks', 26);         -- id = 38
INSERT INTO categories (name, parent_id) VALUES ('Reference Books', 26);   -- id = 39
INSERT INTO categories (name, parent_id) VALUES ('Dictionaries', 26);
