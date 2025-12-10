-- Insert Categories
INSERT INTO categories (name, sort_order) VALUES 
('Shabu Sets', 1),
('Meat & Pork', 2),
('Vegetables', 3),
('Drinks', 4);

-- Insert Products
-- Shabu Sets
INSERT INTO products (category_id, name, description, price, image_url, is_available) 
SELECT id, 'Premium Beef Set', 'Wagyu beef, vegetables, and egg', 599.00, 'https://images.unsplash.com/photo-1555126634-323283e090fa?auto=format&fit=crop&w=500&q=60', true 
FROM categories WHERE name = 'Shabu Sets';

INSERT INTO products (category_id, name, description, price, image_url, is_available) 
SELECT id, 'Pork Set', 'Kurobuta pork, vegetables, and egg', 399.00, 'https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?auto=format&fit=crop&w=500&q=60', true 
FROM categories WHERE name = 'Shabu Sets';

-- Meat
INSERT INTO products (category_id, name, description, price, image_url, is_available) 
SELECT id, 'Sliced Wagyu Beef', 'Premium A4 Wagyu', 299.00, 'https://images.unsplash.com/photo-1615937657715-bc7b4b7962c1?auto=format&fit=crop&w=500&q=60', true 
FROM categories WHERE name = 'Meat & Pork';

INSERT INTO products (category_id, name, description, price, image_url, is_available) 
SELECT id, 'Sliced Pork Belly', 'Thinly sliced pork belly', 129.00, 'https://images.unsplash.com/photo-1615937691194-97dbd3f3dc29?auto=format&fit=crop&w=500&q=60', true 
FROM categories WHERE name = 'Meat & Pork';

-- Veggies
INSERT INTO products (category_id, name, description, price, image_url, is_available) 
SELECT id, 'Mixed Vegetable Set', 'Cabbage, Morning Glory, Corn, Mushroom', 89.00, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=500&q=60', true 
FROM categories WHERE name = 'Vegetables';

-- Drinks
INSERT INTO products (category_id, name, description, price, image_url, is_available) 
SELECT id, 'Coke', 'Can 325ml', 25.00, 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=500&q=60', true 
FROM categories WHERE name = 'Drinks';
