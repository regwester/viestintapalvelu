ALTER TABLE kirjeet.vastaanottajakirje ADD COLUMN julkaistavissa boolean DEFAULT false NOT NULL;
ALTER TABLE kirjeet.vastaanottaja ADD COLUMN oid_henkilo CHARACTER VARYING(256);