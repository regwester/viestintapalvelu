ALTER TABLE kirjeet.kirjelahetys DROP COLUMN ipost;
-- Table: kirjeet.iposti

-- DROP TABLE kirjeet.iposti;

CREATE TABLE kirjeet.iposti
(
  id bigint NOT NULL,
  version bigint,
  kirjelahetys_id bigint,
  aineisto bytea,
  sisaltotyyppi varchar(255),
  luotu time without time zone,
  lahetetty time without time zone,

  CONSTRAINT iposti_pk PRIMARY KEY (id),
  CONSTRAINT iposti_kirjelahetys_id_key FOREIGN KEY (kirjelahetys_id)
      REFERENCES kirjeet.kirjelahetys (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE kirjeet.iposti
  OWNER TO oph;

  