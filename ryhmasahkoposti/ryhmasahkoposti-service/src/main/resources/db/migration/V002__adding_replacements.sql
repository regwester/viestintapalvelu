CREATE TABLE raportoitavavastaanottajakorvauskentat (
 id bigint NOT NULL,
 version bigint NOT NULL,
 raportoitavavastaanottaja_id bigint NOT NULL,
 nimi character varying(255),
 oletus_arvo character varying(3000),
 aikaleima timestamp without time zone,
 CONSTRAINT raportoitavavastaanottajakorvauskentat_pk PRIMARY KEY (id),
 CONSTRAINT raportoitavavastaanottajakorvauskentat_raportoitavavastaanottaja_id_fkey FOREIGN KEY (raportoitavavastaanottaja_id)
     REFERENCES raportoitavavastaanottaja (id) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION
 );
     
ALTER TABLE raportoitavaviesti ADD COLUMN tyyppi character varying (5);