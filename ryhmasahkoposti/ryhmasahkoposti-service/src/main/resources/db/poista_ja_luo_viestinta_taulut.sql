SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

-- Poistetaan sekvenssit ja taulut
DROP SEQUENCE hibernate_sequence;
DROP TABLE raportoitavaviesti_raportoitavaliite; 
DROP TABLE raportoitavaliite;
DROP TABLE raportoitavavastaanottaja;
DROP TABLE raportoitavaviesti;

-- Luodaan sekvenssi avaimien gemerointia varten
CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.hibernate_sequence OWNER TO oph;

SET default_tablespace = '';
SET default_with_oids = false;

-- Luoddan tietokannan taulut
CREATE TABLE raportoitavaliite (
    id bigint NOT NULL,
	version bigint NOT NULL,
    liitetiedoston_nimi character varying(255),
    liitetiedosto bytea,
    sisaltotyyppi character varying(255),
    aikaleima timestamp without time zone
);

ALTER TABLE public.raportoitavaliite OWNER TO oph;

CREATE TABLE raportoitavavastaanottaja (
    id bigint NOT NULL,
    version bigint NOT NULL,
    lahetettyviesti_id bigint,
    vastaanottajan_oid character varying(50),
    vastaanottajan_oid_tyyppi character varying(50),
    vastaanottajan_sahkopostiosoite character varying(255),
	henkilotunnus character varying(11),
    kielikoodi character varying(2),
    hakunimi character varying(255) NOT NULL,
    lahetysalkoi timestamp without time zone,
    lahetysonnistui character varying(1),
    lahetyspaattyi timestamp without time zone,
    epaonnistumisensyy character varying(255),
    aikaleima timestamp without time zone
);

ALTER TABLE public.raportoitavavastaanottaja OWNER TO oph;

CREATE TABLE raportoitavaviesti (
    id bigint NOT NULL,
	version bigint NOT NULL,
    prosessi character varying(255),
    lahettajan_oid character varying(50),
    lahettajan_oid_tyyppi character varying(50),
    lahettajan_sahkopostiosoite character varying(255),
    vastauksensaajan_oid character varying(50),
    vastauksensaajan_oid_tyyppi character varying(50),
    vastauksensaajan_sahkopostiosoite character varying(255),
    aihe character varying(255),
	viesti text,
    htmlviesti character varying(4),
    merkisto character varying(50),
    lahetysalkoi timestamp without time zone,
    lahetyspaattyi timestamp without time zone,
    aikaleima timestamp without time zone
);

ALTER TABLE public.raportoitavaviesti OWNER TO oph;

CREATE TABLE raportoitavaviesti_raportoitavaliite (
    id bigint NOT NULL,
    version bigint NOT NULL,
    raportoitavaliite_id bigint,
    lahetettyviesti_id bigint,
    aikaleima timestamp without time zone
);

ALTER TABLE public.raportoitavaviesti_raportoitavaliite OWNER TO oph;

-- Asetetaan avaimmukset tietokannan tauluihin
ALTER TABLE ONLY raportoitavaliite
    ADD CONSTRAINT lahatetynviestinliite_pk PRIMARY KEY (id);

ALTER TABLE ONLY raportoitavaviesti
    ADD CONSTRAINT lahetettyviesti_pk PRIMARY KEY (id);

ALTER TABLE ONLY raportoitavavastaanottaja
    ADD CONSTRAINT lahetetynviestinvastaanottaja_pk PRIMARY KEY (id);

ALTER TABLE ONLY raportoitavaviesti_raportoitavaliite
    ADD CONSTRAINT raportoitavaviesti_raportoitavaliite_pk PRIMARY KEY (id);

-- Asetetaan käyttöoikeudet
REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM oph;
GRANT ALL ON SCHEMA public TO oph;
GRANT ALL ON SCHEMA public TO PUBLIC;

REVOKE ALL ON SEQUENCE hibernate_sequence FROM PUBLIC;
REVOKE ALL ON SEQUENCE hibernate_sequence FROM oph;
GRANT ALL ON SEQUENCE hibernate_sequence TO oph;
