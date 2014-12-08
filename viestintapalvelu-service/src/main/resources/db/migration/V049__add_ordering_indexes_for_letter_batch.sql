-- Indexit järjestykselle, muutoin join räjähtää käsiin:
create index kirjeet_kirjelahetys_aikaleima_index on kirjeet.kirjelahetys(aikaleima);
create index kirjeet_kirjelahetys_haku_index on kirjeet.kirjelahetys(haku);
create index kirjeet_kirjelahetys_hakukohde_index on kirjeet.kirjelahetys(hakukohde);
create index kirjeet_kirjelahetys_tunniste_index on kirjeet.kirjelahetys(tunniste);
create index kirjeet_kirjelahetys_kirjepohja_index on kirjeet.kirjelahetys(template_name);
