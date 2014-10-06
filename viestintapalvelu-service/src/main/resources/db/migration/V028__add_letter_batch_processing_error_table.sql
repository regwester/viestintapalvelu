CREATE TABLE kirjeet.kirjelahetysvirhe(
  id bigint NOT NULL,
  version bigint NOT NULL,
  kirjelahetys_id bigint not null,
  vastaanottaja_id bigint not null,
  virheen_syy varchar not null,
  aika timestamp without time zone not null,
  foreign key (kirjelahetys_id) references kirjeet.kirjelahetys(id),
  foreign key (vastaanottaja_id) references kirjeet.vastaanottaja(id)
);