
CREATE TABLE lahetysjono (
    id int8 primary key,
    version bigint not null default 0,
    luotu timestamp not null default now(),
    tila varchar(64) not null default 'CREATED',
    viimeksi_kasitelty timestamp,
    suoritettu timestamp
);
ALTER TABLE raportoitavavastaanottaja ADD COLUMN jono int8 REFERENCES lahetysjono(id);

ALTER TABLE lahetysjono ADD CONSTRAINT lahetysjono_tila_constraint
  CHECK( tila in ('CREATED', 'WAITING_FOR_HANDLER', 'PROCESSING', 'FAILED', 'READY') );

-- Attach a READY laheystjono for every already handled vastaanottoja
INSERT INTO lahetysjono (id, tila, viimeksi_kasitelty, suoritettu)
  VALUES (1, 'READY', now(), now());
UPDATE raportoitavavastaanottaja SET jono = 1 WHERE lahetysalkoi is not null;

-- Attach a WAITING_FOR_HANDLER for every vastaanottaja not already processed (assuming none):
INSERT INTO lahetysjono (id, tila)
  VALUES (2, 'WAITING_FOR_HANDLER');
UPDATE raportoitavavastaanottaja SET jono = 2 WHERE lahetysalkoi is null;
