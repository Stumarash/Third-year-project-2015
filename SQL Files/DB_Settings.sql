--DB SETTINGS--
--we need to create the Queue and the Service broker and assign the privileges to the SQL user.->
USE TarDatabase;
CREATE QUEUE NameChangeQueue;
CREATE SERVICE NameChangeService ON QUEUE NameChangeQueue([http://schemas.microsoft.com/SQL/Notifications/PostQueryNotification]);

--To subscribe query notification, we need to give permission to IIS service account--
--GRANT SUBSCRIBE QUERY NOTIFICATIONS TO Tumelo;

ALTER DATABASE TarDatabase SET ENABLE_BROKER WITH ROLLBACK IMMEDIATE;