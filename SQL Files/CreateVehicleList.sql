create table VEHICLE_LIST
(
	REG_NO varchar(40) not null unique,
	VEHICLE_NAME varchar(40) not null,
	primary key(REG_NO)
	);