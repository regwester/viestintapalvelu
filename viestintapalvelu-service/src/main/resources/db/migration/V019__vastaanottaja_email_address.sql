-- Table: kirjeet.vastaanottajaemail

-- DROP TABLE kirjeet.vastaanottajaemail;
CREATE TABLE kirjeet.vastaanottajaemail
(
  id bigint NOT NULL,
  version bigint,
  vastaanottaja_id bigint,
  email varchar(255),
  luotu time without time zone,
  lahetetty time without time zone,

  CONSTRAINT vastaanottajaemail_pk PRIMARY KEY (id),
  CONSTRAINT vastaanottajaemail_vastaanottaja_id FOREIGN KEY (vastaanottaja_id)
      REFERENCES kirjeet.vastaanottaja (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE kirjeet.vastaanottajaemail
  OWNER TO oph;
