CREATE TABLE kirjeet.kaytetytpohjat
(
  id bigint NOT NULL,
  version bigint,
  kirjelahetys_id bigint NOT NULL,
  kirjepohja_id bigint NOT NULL,
  aikaleima time without time zone,
  CONSTRAINT kaytetytpohjat_pk PRIMARY KEY (id),
  CONSTRAINT kirjelahetys_id_fkey FOREIGN KEY (kirjelahetys_id)
      REFERENCES kirjeet.kirjelahetys (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT kirjepohja_id_fkey FOREIGN KEY (kirjepohja_id)
      REFERENCES kirjeet.kirjepohja (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);