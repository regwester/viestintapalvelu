CREATE TYPE kirjelahetyksentila AS ENUM ('processing', 'ready', 'error');
ALTER TABLE  kirjeet.kirjelahetys ADD COLUMN kasittelyn_tila kirjelahetyksentila not null;