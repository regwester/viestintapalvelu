
ALTER TABLE  kirjeet.kirjelahetys DROP CONSTRAINT kasittelyn_tilat;
ALTER TABLE  kirjeet.kirjelahetys ADD CONSTRAINT kasittelyn_tilat CHECK (
      kasittelyn_tila IN ( 'created', 'processing', 'waiting_for_ipost_processing',
          'processing_ipost', 'ready', 'error')
);

ALTER TABLE  kirjeet.kirjelahetys ADD COLUMN ipost_kasittely_aloitettu timestamp;
ALTER TABLE  kirjeet.kirjelahetys ADD COLUMN ipost_kasittely_valmis timestamp;
