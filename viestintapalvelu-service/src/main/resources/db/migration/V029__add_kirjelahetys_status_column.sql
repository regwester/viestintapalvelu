ALTER TABLE kirjeet.kirjelahetys ADD COLUMN kasittelyn_tila varchar(64) DEFAULT 'created';
UPDATE kirjeet.kirjelahetys SET kasittelyn_tila = 'ready';
ALTER TABLE  kirjeet.kirjelahetys ADD CONSTRAINT kasittelyn_tilat CHECK (
      kasittelyn_tila IN ( 'created', 'processing', 'ready', 'error')
);
ALTER TABLE  kirjeet.kirjelahetys ALTER COLUMN kasittelyn_tila SET NOT NULL;
