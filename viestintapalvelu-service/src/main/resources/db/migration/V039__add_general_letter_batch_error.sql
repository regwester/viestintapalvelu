
alter table kirjeet.kirjelahetysvirhe drop constraint virhe_tyyppi;
alter table kirjeet.kirjelahetysvirhe add constraint virhe_tyyppi check(
  tyyppi in('LETTER', 'IPOSTI', 'EMAIL', 'GENERAL')
);
