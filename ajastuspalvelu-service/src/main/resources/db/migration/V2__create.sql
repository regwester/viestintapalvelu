
create table tehtava (
  id serial8 primary key,
  versio int8 not null default 0,
  nimi varchar(512) not null,
  bean_nimi varchar(256) not null,
  kirjepohja_nimi varchar(128)
);

create table ajastettu_tehtava (
  id serial8 primary key,
  versio int8 not null default 0,
  tehtava_id int8 not null references tehtava(id) not null,
  luoja_oid varchar(128),
  luoja_organisaatio_oid varchar(128),
  luontiaika timestamp not null default now(),
  muokkausaika timestamp,
  haku_oid varchar(128),
  poistettu timestamp,
  yksittaisen_ajohetki timestamp
);

create table ajastettu_ajo(
  id serial8 primary key,
  versio int8 not null default 0,
  ajastettu_tehtava_id int8 references ajastettu_ajo(id) not null,
  ajo_aloitettu timestamp not null,
  ajo_paattynyt timestamp,
  tila varchar(64) not null,
  check ( tila in ('STARTED', 'FINISHED', 'ERROR')),
  virheviesti text
);
