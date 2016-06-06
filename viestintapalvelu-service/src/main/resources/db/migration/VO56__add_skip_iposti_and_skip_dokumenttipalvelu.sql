ALTER TABLE kirjeet.vastaanottaja ADD COLUMN ohita_iposti boolean DEFAULT false NOT NULL;
ALTER TABLE kirjeet.kirjelahetys ADD COLUMN ohita_dokumenttipalvelu boolean DEFAULT false NOT NULL;