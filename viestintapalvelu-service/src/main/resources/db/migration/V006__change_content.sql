-- Table: kirjeet.vastaanottajaosoite

-- DROP TABLE kirjeet.vastaanottajaosoite;

CREATE TABLE kirjeet.vastaanottajaosoite
(
  id bigint NOT NULL,
  version bigint,
  vastaanottaja_id bigint,
  etunimi character varying(255),
  sukunimi character varying(255),
  osoite character varying(255),
  osoite2 character varying(255),
  osoite3 character varying(255),
  postinumero character varying(10),
  kaupunki character varying(255),
  maakunta character varying(255),
  maa character varying(255),
  maakoodi character varying(5),
  CONSTRAINT vastaanottajaosoite_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottajaosoite_vastaanottaja_id FOREIGN KEY (vastaanottaja_id)
      REFERENCES kirjeet.vastaanottaja (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE kirjeet.vastaanottajaosoite
  OWNER TO oph;
