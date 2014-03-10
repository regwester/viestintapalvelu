
    create table raportoitavaliite (
        id int8 not null unique,
        version int8 not null,
        liitetiedosto bytea,
        liitetiedoston_nimi varchar(255) not null,
        lahetettyviesti_id int8,
        primary key (id)
    );

    create table raportoitavavastaanottaja (
        id int8 not null unique,
        version int8 not null,
        epaonnistumisensyy varchar(255),
        lahetysonnistui boolean,
        lahetysalkoi timestamp,
        lahetyspaattyi timestamp,
        vastaanottaja_oid varchar(255) not null,
        vastaanottaja_oid_tyyppi varchar(255) not null,
        vastaanottajan_sahkoposti varchar(255) not null,
        lahetettyviesti_id int8,
        primary key (id)
    );

    create table raportoitavaviesti (
        id int8 not null unique,
        version int8 not null,
        aihe varchar(255) not null,
        lahettajan_oid varchar(255) not null,
        lahettajan_oid_tyyppi varchar(255) not null,
        lahettajan_sahkopostiosoite varchar(255) not null,
        lahetysalkoi timestamp not null,
        lahetyspaattyi timestamp,
        prosessi varchar(255) not null,
        vastauksensaajan_oid varchar(255),
        vastauksensaajan_oid_tyyppi varchar(255),
        vastauksensaajan_sahkopostiosoite varchar(255),
        viesti bytea not null,
        primary key (id)
    );

    alter table raportoitavaliite 
        add constraint FKB050815B43878DE1 
        foreign key (lahetettyviesti_id) 
        references raportoitavaviesti;

    alter table raportoitavavastaanottaja 
        add constraint FKFF438A2943878DE1 
        foreign key (lahetettyviesti_id) 
        references raportoitavaviesti;

    create sequence hibernate_sequence;
