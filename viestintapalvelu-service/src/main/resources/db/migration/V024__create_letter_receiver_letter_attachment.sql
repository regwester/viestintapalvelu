
create table vastaanottajakirje_liite(
    id int8 primary key,
    version int8 not null default 0,
    vastaanottajakirje int8 not null references vastaanottajakirje(id)
        on delete cascade, -- these are temporal by definition, so delete among kirjees
    sisalto bytea not null,
    nimi varchar(256) not null,
    tyyppi varchar(128) not null,
    luontiaika timestamp not null default now()
);
