
alter table kirjeet.kirjelahetysvirhe add column tyyppi varchar(64);
update kirjeet.kirjelahetysvirhe set tyyppi = 'LETTER';
alter table kirjeet.kirjelahetysvirhe alter column tyyppi set not null;
alter table kirjeet.kirjelahetysvirhe add constraint virhe_tyyppi check(
  tyyppi in('LETTER', 'IPOSTI', 'EMAIL')
);
alter table kirjeet.kirjelahetysvirhe alter column vastaanottaja_id drop not null;
alter table kirjeet.kirjelahetysvirhe add constraint vastaanottaja_id_null check(
    (tyyppi = 'LETTER' and vastaanottaja_id_null is not null)
  or (tyyppi != 'LETTER' and vastaanottaja_id_null is null)
);
alter table kirjeet.kirjelahetysvirhe add column iposti_order_number int;
alter table kirjeet.kirjelahetysvirhe add constraint iposti_order_number_null check(
    (tyyppi = 'IPOSTI' and iposti_order_number_null is not null)
  or (tyyppi != 'IPOSTI' and iposti_order_number_null is null)
);