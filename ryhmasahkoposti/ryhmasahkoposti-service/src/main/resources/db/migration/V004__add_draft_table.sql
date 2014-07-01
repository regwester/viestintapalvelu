CREATE TABLE public.luonnos (
    id bigint NOT NULL,
    version bigint NOT NULL,
    lahettajan_oid character varying(50),
    lahettajan_osoite character varying(255),
    aihe character varying(255),
    sisalto text,
    html boolean,
    tallennettu timestamp with time zone,
    vastaanottajan_osoite character varying(255)
);

CREATE TABLE public.luonnos_liite (
    id bigint NOT NULL,
    version bigint NOT NULL,
    luonnos_id bigint,
    liite_id bigint
);