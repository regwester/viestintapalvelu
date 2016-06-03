ALTER TABLE kirjeet.vastaanottajakirje ADD COLUMN julkaistavissa boolean NOT NULL DEFAULT false;
ALTER TABLE kirjeet.vastaanottaja ADD COLUMN oid_person varchar(256);