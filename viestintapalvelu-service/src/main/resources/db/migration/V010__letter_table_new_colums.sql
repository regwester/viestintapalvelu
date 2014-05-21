ALTER TABLE kirjeet.vastaanottajakirje DROP COLUMN kirje;  
ALTER TABLE kirjeet.vastaanottajakirje ADD COLUMN kirje bytea;  

ALTER TABLE kirjeet.vastaanottajakirje DROP COLUMN aikaleima;  
ALTER TABLE kirjeet.vastaanottajakirje ADD COLUMN aikaleima timestamp without time zone;  

ALTER TABLE kirjeet.vastaanottajakirje ADD COLUMN sisaltotyyppi varchar(255);
ALTER TABLE kirjeet.vastaanottajakirje ADD COLUMN alkuperainensisaltotyyppi varchar(255);
