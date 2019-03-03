--ADD Restaurant--
CREATE OR REPLACE FUNCTION Add_Profile (_id INT = NULL, _name VARCHAR (100) = NULL, _birthdate VARCHAR(100) = NULL, _bio VARCHAR(300) = NULL, _phone_number VARCHAR(200) = NULL, _address VARCHAR(200) = NULL)
RETURNS VOID
AS
$BODY$
BEGIN
INSERT INTO Profiles(
  name,
	birthdate  ,
	bio ,
	phone_number ,
	address 
)values(
  _name,
	_birthdate  ,
	_bio ,
	_phone_number ,
	_address 
);
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;

--GET Restaurant--
CREATE OR REPLACE FUNCTION Get_Profile_By_Id (_id INT = NULL)
RETURNS refcursor AS
$BODY$
DECLARE
ref refcursor;
BEGIN
OPEN ref FOR SELECT * FROM Profiles WHERE id = _id;
RETURN ref;
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;

--UPDATE Restaurant--
CREATE OR REPLACE FUNCTION Update_Profile_By_Id (_id INT = NULL, _name VARCHAR (100) = NULL, _birthdate VARCHAR(100) = NULL, _bio VARCHAR(300) = NULL, _phone_number VARCHAR(200) = NULL, _address VARCHAR(200) = NULL)
RETURNS integer AS
$BODY$
DECLARE
  a_count integer;
BEGIN
UPDATE Profiles
SET name = _name, birthdate = _birthdate, bio = _bio, phone_number = _phone_number, address = _address
WHERE id = _id;
GET DIAGNOSTICS a_count = ROW_COUNT;
RETURN a_count;
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;

--DELETE Restaurant--
CREATE OR REPLACE FUNCTION Delete_Profile (_id INT = NULL)
RETURNS integer AS
$BODY$
DECLARE
  a_count integer;
BEGIN
DELETE FROM Profiles
WHERE id = _id;
GET DIAGNOSTICS a_count = ROW_COUNT;
RETURN a_count;
END;
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
