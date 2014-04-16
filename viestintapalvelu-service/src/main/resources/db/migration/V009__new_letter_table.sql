-- Table: kirjeet.vastaanottajakirje

-- DROP TABLE kirjeet.vastaanottajakirje;

CREATE TABLE kirjeet.vastaanottajakirje
(
  id bigint NOT NULL,
  version bigint,
  vastaanottaja_id bigint,
  kirje text,
  aikaleima time without time zone,
  CONSTRAINT vastaanottajakirje_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottajakirje_vastaanottaja_id_fkey FOREIGN KEY (vastaanottaja_id)
      REFERENCES kirjeet.vastaanottaja (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE kirjeet.vastaanottajakirje
  OWNER TO oph;
