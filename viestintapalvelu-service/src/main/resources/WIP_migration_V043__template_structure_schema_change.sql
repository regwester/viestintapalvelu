
-- korjaus kaytetytpohjat(aikaleima) tyypistä time -> timestamp (pelkkä kellonaika ei paljon kerro)
alter table kirjeet.kaytetytpohjat alter column aikaleima type timestamp using date(now()) + aikaleima;

-- Style
create table kirjeet.tyyli(
  nimi varchar(127) primary key, -- String name;
  tyyli text not null, -- String cssContent;
  aikaleima timestamp without time zone not null default now() -- Date timestamp = new Date()
);

-- Structure:
create table kirjeet.rakenne (
  id serial8 primary key,
  nimi varchar(511) not null, -- String name;
  kielikoodi varchar(7) not null, -- String language;
  aikaleima timestamp without time zone not null default now(), -- Date timestamp = new Date()
  unique (nimi, kielikoodi)
);

-- ContentStructure
create table kirjeet.sisalto_rakenne(
  id serial8 primary key,
  rakenne int8 references kirjeet.rakenne(id) not null, -- Structure structure
  tyyppi varchar(127) not null, -- ContentStructureType type : enum ContentStructureType { email, letter, asiointitili };
  tyyli varchar(127) not null references kirjeet.tyyli(nimi), -- Style style
  aikaleima timestamp without time zone not null default now(), -- Date timestamp = new Date()
  unique (rakenne, tyyppi)
);
alter table kirjeet.sisalto_rakenne add constraint sisalto_rakenne_tyyppi check(
  tyyppi in ('email', 'letter', 'asiointitili')
);

-- Content:
create table kirjeet.sisalto_uusi (
  id serial8 primary key,
  nimi character varying(255), -- String name;
  aikaleima timestamp without time zone not null default now(), -- Date timestamp = new Date()
  sisalto text, -- String content;
  tyyppi character varying(63) -- ContentType contentType
);
alter table kirjeet.sisalto_uusi add constraint sisalto_tyyppi check(
  tyyppi in ('plain', 'html')
);

-- Liitostaulu: ContentStructureContent: ContentStructure - Content:
create table kirjeet.sisalto_rakenne_sisalto(
  sisalto_rakenne int8 references kirjeet.sisalto_rakenne(id) not null,
  sisalto int8 references  kirjeet.sisalto_uusi(id) not null,
  rooli varchar(64) not null,
  jarjestys int not null, -- Integer orderNumber
  primary key (sisalto_rakenne, sisalto),
  unique (sisalto_rakenne, jarjestys)
);
alter table kirjeet.sisalto_rakenne_sisalto add constraint sisalto_rakenne_sisalto_rooli check(
  rooli in ('body', 'sms', 'attachment')
);


-- ContentReplacement:
create table kirjeet.sisalto_korvauskentta(
  id serial8 primary key,
  rakenne int8 references kirjeet.rakenne(id) not null, -- Structure structure;
  avain varchar(127) not null,  -- String key;
  nimi varchar(255) not null,   -- String name;
  kuvaus text,                  -- String description;
  jarjestys integer,            -- Integer orderNumber;
  tyyppi varchar(63) not null,  -- ContentType contentType;
  riveja integer not null default 1 -- int numberOfRows;
);
alter table kirjeet.sisalto_korvauskentta add constraint sisalto_korvauskentta_tyyppi check(
  tyyppi in ('plain', 'html')
);

-- Template:
alter table kirjeet.kirjepohja add column rakenne int8 references kirjeet.rakenne(id);
alter table kirjeet.kirjepohja add column tila varchar(63); -- TemplateState state; enum TemplateStte { draft, published, closed };
alter table kirjeet.kirjepohja add constraint kirjepohja_tila check(
  tila in ('draft', 'published', 'closed')
);


-- BEGIN MIGRATE:
create or replace function kirjeet.luoRakenneVanhastaPohjasta (_id int8) returns int8 as $$
  declare
    rakenneNimi text;
    vanhaPohja kirjeet.kirjepohja%rowtype;
    PAA_SISALTO_TYYPPI text;
    EMAI_POHJA_SISALTO_TYYPPI text := 'email_body';
    LIITE_SISALTO_TYYPPI text := 'liite';
    rakenneId int8;
    tyyliNimi text;
    sisaltoRakenneId int8;
    sisalto kirjeet.sisalto%rowtype;
    korvaukentta kirjeet.korvauskentat%rowtype;
    sisaltoJarjestysNro int := 0;
    korvauskenttaJarjestys int := 11;
    kirjeTyyppinen bool;
    emailTyyppinen bool;
    sisaltoId int8;
    i int8;
  begin
    select into vanhaPohja kp.* from kirjeet.kirjepohja kp where kp.id = _id;
    raise info 'Migratoidaan vanha pohja %', vanhaPohja.id;
    PAA_SISALTO_TYYPPI := vanhaPohja.nimi;
    
    -- Luodaan rakenne:
    rakenneNimi := vanhaPohja.nimi || '_' || vanhaPohja.id; -- rakenteenkin osalta voisi olla muuttunut pohjien välillä (contentit erityisesti)
    insert into kirjeet.rakenne(nimi, kielikoodi, aikaleima) values (rakenneNimi, vanhaPohja.kielikoodi, vanhaPohja.aikaleima);
    select into rakenneId max(id) from kirjeet.rakenne;
    raise info ' > Rakenne=%', rakenneId;

    -- Luodaan vanhasta templatesta uusi Style-sisältö (yksilöinti vanhan kirjepohjan id:llä):
    tyyliNimi := ('vanha_' || vanhaPohja.nimi || '_' || vanhaPohja.id);

    insert into kirjeet.tyyli (nimi, tyyli) values (tyyliNimi, vanhaPohja.tyylit);
    raise info ' > Tyyli %', tyyliNimi;
    
    -- Tuodaan kaikki vanhat korvauskentät sellaisenaan:
    for korvaukentta in select kk.* from kirjeet.korvauskentat kk where kk.kirjepohja_id = _id order by kk.id loop
      raise info ' > Korvauskenttä % (%)', korvaukentta.nimi, korvaukentta.id;
      if korvaukentta.nimi = 'sisalto' then
        insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
          values (rakenneId, 'sisalto', 'Sisältö', 'Kirjeen ja sähköpostin pääsisältö', 10, 'html', 20);
        raise info ' > Tulkittu sisällöksi';
      elseif korvaukentta.nimi = 'otsikko' then
        insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
          values (rakenneId, 'otsikko', 'Otsikko', 'Kirjeen ylätunnisteessa näkyvä otsikko', 1, 'plain', 1);
        raise info ' > Tulkittu otsikoksi';
      elseif korvaukentta.nimi = 'asiakirjatyyppi' then
        insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
        values (rakenneId, 'asiakirjatyyppi', 'Asiakirjan otsikko', 'Kirjeen sisältöosan varsinainen aloitusteksti / otsikko (asiakirjatyyppi)',
                2, 'plain', 1);
        raise info ' > Tulkittu asiakirjatyypiksi';
      elseif korvaukentta.nimi = 'subject' then
        insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
          values (rakenneId, 'subject', 'Sähköpostin otsikko', 'Sähköpostiviestin otsikko', 3, 'plain', 1);
        raise info ' > Tulkittu sähköpostin otsikoksi';
      else
        raise info ' > Tulkittu muuksi korvaukentäksi, järjestysnumero: %', korvauskenttaJarjestys;
        insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
          values (rakenneId, korvaukentta.nimi, korvaukentta.nimi, korvaukentta.nimi, korvauskenttaJarjestys, 'plain', 10);
        korvauskenttaJarjestys := korvauskenttaJarjestys+1;
      end if;
    end loop;
    
    -- Tyyppipäätelyt:
    if vanhaPohja.tyyppi != 'asiointitili' then
      raise info ' > Muu kuin asiointilityyppinen pohja...';
      -- Asiointitiliä ei vielä varsinaisesti käytössä missään
      emailTyyppinen := (select exists (select s.id from kirjeet.sisalto s
        where s.kirjepohja_id = _id and s.nimi = EMAI_POHJA_SISALTO_TYYPPI));
      kirjeTyyppinen := (not emailTyyppinen or (select exists (select s.id from kirjeet.sisalto s
          where s.kirjepohja_id = _id and s.nimi = PAA_SISALTO_TYYPPI)));
      raise info ' > emailTyyppinen=%, kirjeTyyppinen=%', emailTyyppinen, kirjeTyyppinen;
      
      -- Luodaan sisällöt (ei voida oikeastaan käyttää vanhan rakenteen tyyppi-tietoa koska se on pääsääntöisesti email kaikille):
      if kirjeTyyppinen then
        -- Pohja ei ole asiointitili-tyyppinen JA
        -- Joko kirje ei ole email-tyyyppinen tai kirjeestä löytyy kirjepohjan niminen sisältöosio ja se on email-tyyppinen,
        -- joten oletetaan, että tästä on tarkoitus luoda myös kirjeitä:
        insert into kirjeet.sisalto_rakenne(rakenne, tyyppi, tyyli)
        values (rakenneId, 'letter', tyyliNimi);
        select into sisaltoRakenneId max(id) from kirjeet.sisalto_rakenne;
        raise info ' > Luotiin letter-sisältörakenne %', sisaltoRakenneId;
    
        for sisalto in select s.* from kirjeet.sisalto s where s.kirjepohja_id = _id
                                                                and s.nimi != EMAI_POHJA_SISALTO_TYYPPI
                        order by s.jarjestys, s.id loop
          sisaltoJarjestysNro := sisaltoJarjestysNro+1;
          
          insert into kirjeet.sisalto_uusi(nimi, aikaleima, sisalto, tyyppi)
            values (sisalto.nimi, sisalto.aikaleima, sisalto.sisalto, coalesce(sisalto.tyyppi, 'html'));
          sisaltoJarjestysNro := case when sisalto.jarjestys is not null then greatest(sisalto.jarjestys, sisaltoJarjestysNro)
                                 else sisaltoJarjestysNro end;
          insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, rooli, sisalto, jarjestys)
            values (sisaltoRakenneId, 'body', (select max(id) from kirjeet.sisalto_uusi), sisaltoJarjestysNro);
          raise info ' > Luotiin body-rooliin sisältöelementti % järjestysnumerolle %', (select max(id) from kirjeet.sisalto_uusi), sisaltoJarjestysNro;
        end loop;
      end if;
    
      if emailTyyppinen then 
        -- Kirjeestä löytyy email_body, eli tehdään siitä (myös) email-tyyppinen sisalto_rakenne:
        insert into kirjeet.sisalto_rakenne(rakenne, tyyppi, tyyli)
            values (rakenneId, 'email', tyyliNimi);
        select into sisaltoRakenneId max(id) from kirjeet.sisalto_rakenne;
        raise info ' > Luotiin email-sisältörakenne %', sisaltoRakenneId;
        
        -- Luodaan email_body sisältö ja linkitetään se:
        select into sisalto s.* from kirjeet.sisalto s where s.kirjepohja_id = _id and s.nimi = EMAI_POHJA_SISALTO_TYYPPI;
        insert into kirjeet.sisalto_uusi(nimi, aikaleima, sisalto, tyyppi)
            values (EMAI_POHJA_SISALTO_TYYPPI, sisalto.aikaleima, sisalto.sisalto, coalesce(sisalto.tyyppi, 'html'));
        sisaltoJarjestysNro := case when sisalto.jarjestys is not null then greatest(sisalto.jarjestys, sisaltoJarjestysNro+1)
                               else sisaltoJarjestysNro end;
        insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, rooli, sisalto, jarjestys)
            values (sisaltoRakenneId, 'body', (select max(id) from kirjeet.sisalto_uusi), sisaltoJarjestysNro);
        raise info ' > Luotiin body-rooliin sisältöelementti % järjestysnumerolle %',
            (select max(id) from kirjeet.sisalto_uusi), sisaltoJarjestysNro;
        
        if kirjeTyyppinen then
          raise info ' > Luodaan liitteet kuten kirjeillä muille kuin % ja % -sisällöille', EMAI_POHJA_SISALTO_TYYPPI, PAA_SISALTO_TYYPPI;
          for sisalto in select s.* from kirjeet.sisalto s where s.kirjepohja_id = _id
                                      and s.nimi not in (EMAI_POHJA_SISALTO_TYYPPI,  PAA_SISALTO_TYYPPI)
                          order by s.jarjestys, s.id loop
            sisaltoJarjestysNro := sisaltoJarjestysNro+1;
            raise info ' > Liiteen % sisältöosio', sisalto.nimi;
            
            select into sisaltoId s.id from kirjeet.sisalto_uusi s
                    inner join kirjeet.sisalto_rakenne_sisalto srs on srs.sisalto = s.id
                    inner join kirjeet.sisalto_rakenne sr on sr.id = srs.sisalto_rakenne
                    inner join kirjeet.rakenne r on r.id = _id and sr.rakenne = r.id
                    where s.nimi = sisalto.nimi;
            if sisaltoId is null then
              insert into kirjeet.sisalto_uusi(nimi, aikaleima, sisalto, tyyppi)
                values (sisalto.nimi, sisalto.aikaleima, sisalto.sisalto, coalesce(sisalto.tyyppi, 'html'));
              select into sisaltoId max(id) from kirjeet.sisalto_uusi;
              raise info ' > Luotiin uusi sisältö';
            else
              raise info ' > Sisältö-osio % löytyi jo tästä rakenteesta id:llä %', sisalto.nimi, sisaltoId;
            end if;
            sisaltoJarjestysNro := case when sisalto.jarjestys is not null
                then greatest(sisalto.jarjestys, sisaltoJarjestysNro) else sisaltoJarjestysNro end;
            insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, rooli, sisalto, jarjestys)
              values (sisaltoRakenneId, 'attachment', sisaltoId, sisaltoJarjestysNro);
            raise info ' > Luotiin attachment-rooliin sisältöelementtilinkitys % järjestysnumerolle %',
                (select max(id) from kirjeet.sisalto_uusi), sisaltoJarjestysNro;            
          end loop;
        else
          if (select exists(select s.* from kirjeet.sisalto s where s.kirjepohja_id = _id and s.nimi = LIITE_SISALTO_TYYPPI)) then
            raise info ' > Luodaan liitteet kuten pelkillä sähköposteilla %-tyyppisille sisällöille', LIITE_SISALTO_TYYPPI;
            
            -- Lisätään sähköpostin liitteet:
            for sisalto in select s.* from kirjeet.sisalto s where s.kirjepohja_id = _id and s.nimi = LIITE_SISALTO_TYYPPI
                            order by s.jarjestys, s.id loop
              sisaltoJarjestysNro := sisaltoJarjestysNro+1;
              raise info ' > Liiteen % sisältöosio', sisalto.nimi;
              
              select into sisaltoId min(s.id) from kirjeet.sisalto_uusi s
                  inner join kirjeet.sisalto_rakenne_sisalto srs on srs.sisalto = s.id
                  inner join kirjeet.sisalto_rakenne sr on sr.id = srs.sisalto_rakenne
                  inner join kirjeet.rakenne r on r.id = _id and sr.rakenne = r.id
                where s.nimi = sisalto.nimi;
              if sisaltoId is null then
                insert into kirjeet.sisalto_uusi(nimi, aikaleima, sisalto, tyyppi)
                  values (sisalto.nimi, sisalto.aikaleima, sisalto.sisalto, coalesce(sisalto.tyyppi, 'html'));
                select into sisaltoId max(id) from kirjeet.sisalto_uusi;
                raise info ' > Luotiin uusi sisältö';
              else
                raise info ' > Sisältö-osio % löytyi jo tästä rakenteesta id:llä %', sisalto.nimi, sisaltoId;
              end if;
              sisaltoJarjestysNro := case when sisalto.jarjestys is not null
                  then greatest(sisalto.jarjestys, sisaltoJarjestysNro) else sisaltoJarjestysNro end;
              insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, rooli, sisalto, jarjestys)
                values (sisaltoRakenneId, 'attachment', sisaltoId, sisaltoJarjestysNro);
              raise info ' > Luotiin attachment-rooliin sisältöelementtilinkitys % järjestysnumerolle %',
                (select max(id) from kirjeet.sisalto_uusi), sisaltoJarjestysNro;
            end loop;
          end if;
        end if;
      end if;
    end if;
    return rakenneId;
  end;
$$ language plpgsql;

update kirjeet.kirjepohja set rakenne = kirjeet.luoRakenneVanhastaPohjasta(id);

alter table kirjeet.kirjepohja alter column rakenne set not null;
alter table kirjeet.sisalto rename to vanha_sisalto;
alter table kirjeet.sisalto_uusi rename to sisalto;

-- Oletetaan, että kaikki nykyiset olemassa olevat kirjepohjat ovat julkaistuja ja niitä ei voi siis enää muokata:
update kirjeet.kirjepohja set tila = 'published';
alter table kirjeet.kirjepohja alter column tila set not null;
-- END MIGRATE

