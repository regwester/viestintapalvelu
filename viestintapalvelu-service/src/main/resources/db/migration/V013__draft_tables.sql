-- Add 'IPOST send' -flag

ALTER TABLE kirjeet.kirjelahetys ADD COLUMN ipost boolean DEFAULT false;


-- Table: kirjeet.luonnos

-- DROP TABLE kirjeet.luonnos;

CREATE TABLE kirjeet.luonnos
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  kirjepohjan_nimi character varying(255) NOT NULL,
  kirjepohjan_kielikoodi character varying(5) NOT NULL,
  aikaleima timestamp without time zone,
  oid_tallentaja character varying(255),
  oid_organisaatio character varying(255),
  haku character varying(255),
  hakukohde character varying(255),
  tunniste character varying(255),
  CONSTRAINT luonnos_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE kirjeet.luonnos
  OWNER TO oph;

  
-- Table: kirjeet.luonnoskorvauskentat

-- DROP TABLE kirjeet.luonnoskorvauskentat;

CREATE TABLE kirjeet.luonnoskorvauskentat
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  nimi character varying(255),
  oletus_arvo character varying(3000),
  aikaleima timestamp without time zone,
  pakollinen boolean,
  luonnos_id bigint,
  CONSTRAINT luonnoskorvauskentat_pk PRIMARY KEY (id),
  CONSTRAINT korvauskentat_luonnos_id_fkey FOREIGN KEY (luonnos_id)
      REFERENCES kirjeet.luonnos (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE kirjeet.luonnoskorvauskentat
  OWNER TO oph;
