ALTER TABLE kirjeet.kirjelahetys add CONSTRAINT kirjelahetys_pk PRIMARY KEY (id);
ALTER TABLE kirjeet.kirjelahetys add COLUMN kielikoodi character varying(5);


-- Table: kirjeet.lahetyskorvauskentat

-- DROP TABLE kirjeet.lahetyskorvauskentat;

CREATE TABLE kirjeet.lahetyskorvauskentat
(
  id bigint NOT NULL,
  version bigint,
  kirjelahetys_id bigint,
  nimi character varying(255),
  oletus_arvo character varying(3000),
  aikaleima timestamp without time zone,
  pakollinen boolean,
  CONSTRAINT lahetyskorvauskentat_pk PRIMARY KEY (id),
  CONSTRAINT lahetyskorvauskentat_kirjelahetys_id_fkey FOREIGN KEY (kirjelahetys_id)
      REFERENCES kirjeet.kirjelahetys (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE kirjeet.lahetyskorvauskentat
  OWNER TO oph;

  
-- Table: kirjeet.vastaanottaja

-- DROP TABLE kirjeet.vastaanottaja;

CREATE TABLE kirjeet.vastaanottaja
(
  id bigint NOT NULL,
  version bigint,
  kirjelahetys_id bigint,
  aikaleima timestamp without time zone,
  CONSTRAINT vastaanottaja_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottaja_kirjelahetys_id_fkey FOREIGN KEY (kirjelahetys_id)
      REFERENCES kirjeet.kirjelahetys (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE kirjeet.vastaanottaja
  OWNER TO oph;

  
-- Table: kirjeet.vastaanottajakorvauskentat

-- DROP TABLE kirjeet.vastaanottajakorvauskentat;

CREATE TABLE kirjeet.vastaanottajakorvauskentat
(
  id bigint NOT NULL,
  version bigint,
  vastaanottaja_id bigint,
  nimi character varying(255),
  oletus_arvo character varying(3000),
  pakollinen boolean,
  aikaleima timestamp without time zone,
  CONSTRAINT vastaanottajakorvauskentat_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottajakorvauskentat_vastaanottaja_id_fkey FOREIGN KEY (vastaanottaja_id)
      REFERENCES kirjeet.vastaanottaja (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE kirjeet.vastaanottajakorvauskentat
  OWNER TO oph;
