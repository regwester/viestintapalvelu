ALTER TABLE kirjeet.iposti DROP COLUMN luotu;
ALTER TABLE kirjeet.iposti DROP COLUMN lahetetty;
ALTER TABLE kirjeet.iposti ADD COLUMN luotu timestamp with time zone;
ALTER TABLE kirjeet.iposti ADD COLUMN lahetetty timestamp with time zone;