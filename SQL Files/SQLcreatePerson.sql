CREATE TABLE PERSON(
	PERSON_ID int identity not null unique,
	NAME varchar(40) NOT NULL,
	SURNAME varchar(40) NOT NULL,
	GENDER varchar(20) NOT NULL,
	EMAIL varchar(40) NOT NULL,
	PERSON_PASS nvarchar(40) NOT NULL,
	PERSON_CELL_NO int NOT NULL, 
	DATE_OF_BIRTH varchar(40) NOT NULL,
	
);