CREATE ROLE oph;
ALTER ROLE oph WITH login;
GRANT ALL ON SCHEMA public TO oph;

/* after this comes the flyway migration */