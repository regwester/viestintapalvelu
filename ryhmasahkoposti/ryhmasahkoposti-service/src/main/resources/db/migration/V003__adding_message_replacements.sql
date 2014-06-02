CREATE TABLE raportoitavaviestikorvauskentat (
 id bigint NOT NULL,
 version bigint NOT NULL,
 raportoitavaviesti_id bigint NOT NULL,
 nimi character varying(255),
 oletus_arvo character varying(3000),
 aikaleima timestamp without time zone,
 CONSTRAINT raportoitavaviestikorvauskentat_pk PRIMARY KEY (id),
 CONSTRAINT raportoitavaviestikorvauskentat_raportoitavaviesti_id_fkey FOREIGN KEY (raportoitavaviesti_id)
     REFERENCES raportoitavaviesti (id) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION
 );
 