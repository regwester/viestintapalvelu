
ALTER TABLE kirjeet.kirjepohja ALTER COLUMN tyyppi TYPE varchar(16) USING tyyppi::varchar(16);
