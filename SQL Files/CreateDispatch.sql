create table DISPATCH
(
	DISPATCH_ID int identity not null unique,
	DISPATCH_TIME datetime not null,
	DISPATCH_ADDITIONAL varchar(40) not null,
	DISPATCH_EMERGENCY int references EMERGENCY(EMERGENCY_ID),
	primary key(DISPATCH_ID)
);