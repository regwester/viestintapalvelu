create index kirjeet_kirjelahetys_hakuehdot_ja_aikaleima_index on kirjeet.kirjelahetys(haku, hakukohde, template_name, kasittelyn_tila, aikaleima);
create index kirjeet_vastaanottaja_kirjelahetys_index on kirjeet.vastaanottaja(kirjelahetys_id);
create index kirjeet_vastaanottajaosoite_vastaanotta_ja_kirjelahetyshakuehdot_index on kirjeet.vastaanottajaosoite(vastaanottaja_id,
    etunimi, sukunimi, postinumero);
