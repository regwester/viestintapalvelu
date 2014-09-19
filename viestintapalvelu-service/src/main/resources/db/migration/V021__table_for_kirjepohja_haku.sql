
CREATE TABLE kirjeet.kirjepohja_haku (
  kirjepohja bigint references kirjeet.kirjepohja(id),
  haku_oid varchar(255),
  primary key(kirjepohja, haku_oid)
);
