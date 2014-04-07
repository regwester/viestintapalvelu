ALTER TABLE kirjeet.kirjepohja ADD COLUMN versionro varchar(10);

ALTER TABLE kirjeet.korvauskentat DROP COLUMN oid_tallentaja;
ALTER TABLE kirjeet.sisalto       DROP COLUMN oid_tallentaja;

ALTER TABLE kirjeet.sisalto ADD COLUMN tyyppi varchar(255);
