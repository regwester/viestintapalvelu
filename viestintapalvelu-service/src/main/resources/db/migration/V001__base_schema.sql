CREATE SEQUENCE kirjeet.kirje_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE kirjeet.kirje_sequence OWNER TO oph;


CREATE TABLE kirjeet.kirjepohja (
    id bigint NOT NULL,
    version bigint NOT NULL,
    nimi character varying(255),
    tyylit character varying(3000),
    kielikoodi character varying(5),
    aikaleima timestamp without time zone,
    oid_tallentaja character varying(255),
    oid_organisaatio character varying(255)
);


ALTER TABLE kirjeet.kirjepohja OWNER TO oph;

--
-- TOC entry 169 (class 1259 OID 18138)
-- Dependencies: 7
-- Name: kirjepohja_sequence; Type: SEQUENCE; Schema: kirjeet; Owner: oph
--

CREATE SEQUENCE kirjeet.kirjepohja_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE kirjeet.kirjepohja_sequence OWNER TO oph;

--
-- TOC entry 172 (class 1259 OID 18159)
-- Dependencies: 7
-- Name: korvauskentat; Type: TABLE; Schema: kirjeet; Owner: oph; Tablespace: 
--

CREATE TABLE kirjeet.korvauskentat (
    id bigint NOT NULL,
    kirjepohja_id bigint,
    nimi character varying(255),
    oletus_arvo character varying(3000),
    aikaleima timestamp without time zone,
    oid_tallentaja character varying(255),
    version bigint,
    pakollinen boolean
);


ALTER TABLE kirjeet.korvauskentat OWNER TO oph;

--
-- TOC entry 171 (class 1259 OID 18148)
-- Dependencies: 7
-- Name: sisalto; Type: TABLE; Schema: kirjeet; Owner: oph; Tablespace: 
--

CREATE TABLE kirjeet.sisalto (
    id bigint NOT NULL,
    kirjepohja_id bigint,
    nimi character varying(255),
    sisalto character varying(5000),
    aikaleima timestamp without time zone,
    oid_tallentaja character varying(255),
    version bigint,
    jarjestys integer
);


ALTER TABLE kirjeet.sisalto OWNER TO oph;

--
-- TOC entry 1939 (class 0 OID 0)
-- Dependencies: 168
-- Name: kirje_sequence; Type: SEQUENCE SET; Schema: kirjeet; Owner: oph
--

SELECT pg_catalog.setval('kirje_sequence', 1, false);


--
-- TOC entry 1929 (class 0 OID 18140)
-- Dependencies: 170 1932
-- Data for Name: kirjepohja; Type: TABLE DATA; Schema: kirjeet; Owner: oph
--
SELECT pg_catalog.setval('kirjepohja_sequence', 1, false);

--
-- TOC entry 1821 (class 2606 OID 18147)
-- Dependencies: 170 170 1933
-- Name: kirjepohja_pk; Type: CONSTRAINT; Schema: kirjeet; Owner: oph; Tablespace: 
--

ALTER TABLE ONLY kirjeet.kirjepohja
    ADD CONSTRAINT kirjepohja_pk PRIMARY KEY (id);


--
-- TOC entry 1823 (class 2606 OID 18171)
-- Dependencies: 172 172 1933
-- Name: korvauskentat_pk; Type: CONSTRAINT; Schema: kirjeet; Owner: oph; Tablespace: 
--

ALTER TABLE ONLY kirjeet.korvauskentat
    ADD CONSTRAINT korvauskentat_pk PRIMARY KEY (id);


--
-- TOC entry 1825 (class 2606 OID 18165)
-- Dependencies: 172 1820 170 1933
-- Name: korvauskentat_kirjepohja_id_fkey; Type: FK CONSTRAINT; Schema: kirjeet; Owner: oph
--

ALTER TABLE ONLY kirjeet.korvauskentat
    ADD CONSTRAINT korvauskentat_kirjepohja_id_fkey FOREIGN KEY (kirjepohja_id) REFERENCES kirjepohja(id);


--
-- TOC entry 1824 (class 2606 OID 18154)
-- Dependencies: 171 170 1820 1933
-- Name: sisalto_kirjepohja_id_fkey; Type: FK CONSTRAINT; Schema: kirjeet; Owner: oph
--

ALTER TABLE ONLY kirjeet.sisalto
    ADD CONSTRAINT sisalto_kirjepohja_id_fkey FOREIGN KEY (kirjepohja_id) REFERENCES kirjepohja(id);


--
-- TOC entry 1936 (class 0 OID 0)
-- Dependencies: 7
-- Name: kirjeet; Type: ACL; Schema: -; Owner: oph
--

REVOKE ALL ON SCHEMA kirjeet FROM PUBLIC;
REVOKE ALL ON SCHEMA kirjeet FROM oph;
GRANT ALL ON SCHEMA kirjeet TO oph;
GRANT ALL ON SCHEMA kirjeet TO PUBLIC;


--
-- TOC entry 1937 (class 0 OID 0)
-- Dependencies: 168
-- Name: kirje_sequence; Type: ACL; Schema: kirjeet; Owner: oph
--

REVOKE ALL ON SEQUENCE kirjeet.kirje_sequence FROM PUBLIC;
REVOKE ALL ON SEQUENCE kirjeet.kirje_sequence FROM oph;
GRANT ALL ON SEQUENCE kirjeet.kirje_sequence TO oph;


--
-- TOC entry 1938 (class 0 OID 0)
-- Dependencies: 169
-- Name: kirjepohja_sequence; Type: ACL; Schema: kirjeet; Owner: oph
--

REVOKE ALL ON SEQUENCE kirjepohja_sequence FROM PUBLIC;
REVOKE ALL ON SEQUENCE kirjepohja_sequence FROM oph;
GRANT ALL ON SEQUENCE kirjepohja_sequence TO oph;


-- Completed on 2014-03-21 10:49:36 EET

--
-- PostgreSQL database dump complete
--

CREATE SEQUENCE kirjeet.lahetys_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE kirjeet.lahetys_sequence OWNER TO oph;


CREATE TABLE kirjeet.kirjelahetys (
    id bigint NOT NULL,
    version bigint NOT NULL,
    template_id bigint NOT NULL,
    aikaleima timestamp without time zone,
    oid_tallentaja character varying(255),
    oid_organisaatio character varying(255)
);
