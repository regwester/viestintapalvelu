
create table raportoitavavastaanottaja_raportoitavaliite(
  id int8 not null primary key,
  version int8 not null default 0,
  vastaanottaja int8 references raportoitavavastaanottaja(id) not null,
  liite  int8 references raportoitavaliite(id) not null,
  aikaleima timestamp not null default now()
);
