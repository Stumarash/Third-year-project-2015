create table DRIVER
(
	DRIVER_ID Varchar(40) unique not null,
	DRIVER_STATUS varchar(40) not null,
	DRIVER_ASSIGNED bit not null,
	DRIVER_EMERGENCY int references DISPATCH(DISPATCH_ID),
	REG_NO varchar(40) references VEHICLE_LIST(Reg_No),
	PERSON_ID int references PERSON(PERSON_ID),
	primary key(DRIVER_ID)
	);