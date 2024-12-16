SET SEARCH_PATH TO ecommerce_schema;

INSERT INTO products (name, category, description, price, image_url)
VALUES
	(
	'iphone 15',
	'tech',
	'The iPhone 15 features USB-C, A16 chip, 48MP camera, and iOS 17, with improved performance.',
	1000,
	'iphone15.jpg'
	),
	(
	'cetics hoodie',
	'clothing',
	'A cozy hoodie with a soft interior, adjustable hood, and a front pocket for comfort.',
	50,
	'celticshoodie.jpg'
	),
	(
	'crispy chicken sandwich',
	'food',
	'A premium handmade delicious crispy chicken sandwich.',
	10,
	'ccsandwich.jpg'
	),
	(
	'sony m5 headphones',
	'tech',
	'Comftorable high quality headphones by sony.',
	300,
	'sonym5.jpg'
	);


