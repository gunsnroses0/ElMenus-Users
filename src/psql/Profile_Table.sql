	CREATE TABLE Profiles
	(
	id BIGSERIAL PRIMARY KEY NOT NULL,
	-- username VARCHAR REFERENCES "Users" (username) ON DELETE CASCADE,
	name VARCHAR (100) NOT NULL UNIQUE,
	birthdate VARCHAR(100) ,
	bio VARCHAR(300),
	phone_number VARCHAR(200),
	address VARCHAR(200)
	);
