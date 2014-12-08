-- Tämä vielä puuttui (olisi tehokkaampi toiseen suuntaan tämä viittaus, kun näitä nyt on vain yksi,
-- jolloin voisi tehdä indexin vastaanottajaan, jossa on kirjelahetys_id ja vastaanottajakirje_id):
create index kirjeet_vastaanottajakirje_vastaanottaja_index on kirjeet.vastaanottajakirje(vastaanottaja_id);
-- Tämä ei laajennettua vastaanottajahakua varten:
create index kirjeet_vastaanottajaosoite_nimi_vastaanottaja_index on kirjeet.vastaanottajaosoite(sukunimi, etunimi, vastaanottaja_id);
