ALTER TABLE  kirjeet.kirjelahetys ADD COLUMN kasittelyn_tila varchar(64) not null;
ALTER TABLE  kirjeet.kirjelahetys ADD CONSTRAINT kasittelyn_tilat CHECK (
      kasittelyn_tila IN ( 'processing', 'ready', 'error' )
);