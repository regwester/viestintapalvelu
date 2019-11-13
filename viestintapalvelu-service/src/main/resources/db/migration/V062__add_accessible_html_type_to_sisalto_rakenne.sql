alter table sisalto_rakenne drop constraint sisalto_rakenne_tyyppi;
alter table sisalto_rakenne add constraint sisalto_rakenne_tyyppi check (
        (tyyppi)::text = ANY (ARRAY[('email'::character varying)::text, ('letter'::character varying)::text, ('asiointitili'::character varying)::text, ('accessibleHtml'::character varying)::text])
    );
