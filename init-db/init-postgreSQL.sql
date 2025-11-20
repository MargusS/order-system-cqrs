CREATE USER margus WITH PASSWORD 'margus123';
CREATE DATABASE command_service_db OWNER margus;
GRANT ALL PRIVILEGES ON DATABASE command_service_db TO margus;
GRANT CONNECT ON DATABASE command_service_db TO margus;
GRANT USAGE ON SCHEMA public TO margus;