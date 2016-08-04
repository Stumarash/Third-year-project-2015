CREATE TABLE EMERGENCY_DIAL
(
Emergency_Dial_ID int IDENTITY(1,1) primary key NOT NULL,
Emergency_Area varchar(40) NOT NULL UNIQUE,
Emergency_Number int NOT NULL
); 