ALTER TABLE kirjeet.kirjepohja ADD COLUMN tila CHARACTER VARYING(64) DEFAULT 'luonnos' NOT NULL CHECK (tila IN ('suljettu', 'julkaistu', 'luonnos'));

UPDATE kirjeet.kirjepohja SET tila = 'julkaistu';