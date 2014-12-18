
-- Lisätään header-tyyppi:
alter table kirjeet.sisalto_rakenne_sisalto drop constraint sisalto_rakenne_sisalto_rooli;
alter table kirjeet.sisalto_rakenne_sisalto add constraint sisalto_rakenne_sisalto_rooli check(
  rooli in ('header', 'body', 'sms', 'attachment')
);

-- Lisätään headerit emailille ja asiointitilille:
create or replace function kirjeet.luoSisalto (_nimi varchar(255), _sisalto text,
          _tyyppi varchar(63)) returns int8 as $$
begin
  insert into kirjeet.rakenne_sisalto(nimi, sisalto, tyyppi)
    values (_nimi, _sisalto, _tyyppi);
  return (select max(id) from kirjeet.rakenne_sisalto);
end;
$$ language plpgsql;

create or replace function kirjeet.luoSisallonOsa
  (_sisalto_rakenne int8, _sisalto int8, _rooli varchar(63)) returns int as $$
declare
  jarjestys int;
begin
  jarjestys := (select max(csc.jarjestys) from kirjeet.sisalto_rakenne_sisalto csc
    where csc.sisalto_rakenne = _sisalto_rakenne)+1;
  insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, sisalto, rooli, jarjestys)
    values (_sisalto_rakenne, _sisalto, _rooli, jarjestys);
  return jarjestys;
end;
$$ language plpgsql;

select kirjeet.luoSisallonOsa(cs.id, kirjeet.luoSisalto(cs.tyyppi || '_otsikko', '$subject', 'plain'), 'header')
from kirjeet.sisalto_rakenne cs where cs.tyyppi in ('email','asiointitili');

-- Osasta sähköpostipohjia puuttuu subject:
insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
select
  s.id as rakenne,
  'subject' as avain,
  'Sähköpostin otsikko' as nimi,
  'Sähköpostiviestin otsikko' as kuvaus,
  30 as jarjestys,
  'plain' as tyyppi,
  1 as riveja
from kirjeet.rakenne s
where exists(
  select * from kirjeet.sisalto_rakenne cs
  where cs.rakenne = s.id and cs.tyyppi = 'email'
) and not exists(
  select * from kirjeet.sisalto_korvauskentta cr
  where cr.rakenne = s.id and cr.avain = 'subject'
);
insert into kirjeet.korvauskentat(id, kirjepohja_id, nimi, oletus_arvo, aikaleima, version, pakollinen)
select
  (select nextval('hibernate_sequence')) as id,
  t.id as kirjepohja_id,
  'subject' as nimi,
  (select r.oletus_arvo from kirjeet.korvauskentat r where r.kirjepohja_id = t.id and r.nimi = 'otsikko') as oletus_arvo,
  now() as aikaleima,
  0 as version,
  true as pakollinen
from kirjeet.kirjepohja t
    inner join kirjeet.kirjepohja s on t.rakenne = s.id
    inner join kirjeet.sisalto_rakenne cs on cs.rakenne = s.id and cs.tyyppi = 'email'
where exists(select * from kirjeet.korvauskentat r where r.kirjepohja_id = t.id and r.nimi = 'otsikko')
  and not exists (select * from kirjeet.korvauskentat r where r.kirjepohja_id = t.id and r.nimi = 'subject');

-- Lisätään migraatiofunktiomäärittelyyn otsikon lisäys emailille:
create or replace function kirjeet.luoRakenneVanhastaPohjasta (_id int8) returns int8 as $$
declare
  rakenneNimi text;
  vanhaPohja kirjeet.kirjepohja%rowtype;
  pohjaTyyppi text;
  PAA_SISALTO_TYYPPI text;
  EMAI_POHJA_SISALTO_TYYPPI text := 'email_body';
  LIITE_SISALTO_TYYPPI text := 'liite';
  rakenneId int8;
  tyyliNimi text;
  tyyliId int8 := null;
  sisaltoRakenneId int8;
  sisalto kirjeet.sisalto%rowtype;
  korvauskentta kirjeet.korvauskentat%rowtype;
  sisaltoJarjestysNro int := 0;
  korvauskenttaJarjestys int := 100;
  kirjeTyyppinen bool;
  emailTyyppinen bool;
  sisaltoId int8;
  i int8;
  subjectLoytyy bool := false;
  otsikonOletusarvo text := '';
begin
  select into vanhaPohja kp.* from kirjeet.kirjepohja kp where kp.id = _id;
  pohjaTyyppi := coalesce(vanhaPohja.tyyppi, 'tuntematon');
  raise info 'Migratoidaan vanha pohja % (nimi=%, kieli=%, tyyppi=%)', vanhaPohja.id, vanhaPohja.nimi, vanhaPohja.kielikoodi, pohjaTyyppi;
  PAA_SISALTO_TYYPPI := vanhaPohja.nimi;

  -- Luodaan rakenne:
  rakenneNimi := vanhaPohja.nimi || '_' || vanhaPohja.id; -- rakenteenkin osalta voisi olla muuttunut pohjien välillä (contentit erityisesti)
  insert into kirjeet.rakenne(nimi, kielikoodi, aikaleima)
    values (rakenneNimi, vanhaPohja.kielikoodi, vanhaPohja.aikaleima);
  select into rakenneId max(id) from kirjeet.rakenne;
  raise info ' > Rakenne=%', rakenneId;

  -- Luodaan vanhasta templatesta uusi Style-sisältö (yksilöinti vanhan kirjepohjan id:llä):
  if vanhaPohja.tyylit is not null then
    tyyliNimi := ('vanha_' || vanhaPohja.nimi);
    insert into kirjeet.tyyli (nimi, tyyli) values (tyyliNimi, vanhaPohja.tyylit);
    tyyliId := (select max(id) from kirjeet.tyyli);
    raise info ' > Tyyli % (%)', tyyliNimi, tyyliId;
  end if;

  -- Tuodaan kaikki vanhat korvauskentät sellaisenaan:
  for korvauskentta in select kk.* from kirjeet.korvauskentat kk where kk.kirjepohja_id = _id order by kk.id loop
    raise info ' > Korvauskenttä % (%)', korvauskentta.nimi, korvauskentta.id;
    if korvauskentta.nimi = 'sisalto' then
      insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
        values (rakenneId, 'sisalto', 'Sisältö', 'Kirjeen ja sähköpostin pääsisältö', 50, 'html', 20);
      raise info ' > Tulkittu sisällöksi';
    elseif korvauskentta.nimi = 'otsikko' then
      insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
        values (rakenneId, 'otsikko', 'Otsikko', 'Kirjeen ylätunnisteessa näkyvä otsikko', 10, 'plain', 1);
      otsikonOletusarvo := korvauskentta.oletus_arvo;
      raise info ' > Tulkittu otsikoksi';
    elseif korvauskentta.nimi = 'asiakirjatyyppi' then
      insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
        values (rakenneId, 'asiakirjatyyppi', 'Asiakirjan otsikko', 'Kirjeen sisältöosan varsinainen aloitusteksti / otsikko (asiakirjatyyppi)',
              2, 'plain', 1);
      raise info ' > Tulkittu asiakirjatyypiksi';
    elseif korvauskentta.nimi = 'subject' then
      insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
        values (rakenneId, 'subject', 'Sähköpostin otsikko', 'Sähköpostiviestin otsikko', 30, 'plain', 1);
      subjectLoytyy := true;
      raise info ' > Tulkittu sähköpostin otsikoksi';
    else
      raise info ' > Tulkittu muuksi korvaukentäksi, järjestysnumero: %', korvauskenttaJarjestys;
      insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
        values (rakenneId, korvauskentta.nimi, korvauskentta.nimi, korvauskentta.nimi, korvauskenttaJarjestys, 'plain', 10);
      korvauskenttaJarjestys := korvauskenttaJarjestys+1;
    end if;
  end loop;

  -- Tyyppipäätelyt:
  if pohjaTyyppi != 'asiointitili' then
    raise info ' > Muu kuin asiointilityyppinen pohja...';
    -- Asiointitiliä ei vielä varsinaisesti käytössä missään
    emailTyyppinen := (select exists (select s.id from kirjeet.sisalto s
    where s.kirjepohja_id = _id and s.nimi = EMAI_POHJA_SISALTO_TYYPPI));
    kirjeTyyppinen := (not emailTyyppinen or (select exists (select s.id from kirjeet.sisalto s
    where s.kirjepohja_id = _id and s.nimi = PAA_SISALTO_TYYPPI)));
    raise info ' > emailTyyppinen=%, kirjeTyyppinen=%', emailTyyppinen, kirjeTyyppinen;

    -- Tarkistetaan, ettei puutu subjectia:
    if emailTyyppinen and not subjectLoytyy then
      insert into kirjeet.sisalto_korvauskentta(rakenne, avain, nimi, kuvaus, jarjestys, tyyppi, riveja)
        values (rakenneId, 'subject', 'Sähköpostin otsikko', 'Sähköpostiviestin otsikko', 30, 'plain', 1);
      insert into kirjeet.korvauskentat(id, kirjepohja_id, nimi, oletus_arvo, aikaleima, version, pakollinen)
        values ((select nextval('hibernate_sequence')), _id, 'subject', otsikonOletusarvo, now(), 0, true);
      raise info ' > Lisätty puuttunut sähköpostin otsikko (subject)';
    end if;

    -- Luodaan sisällöt (ei voida oikeastaan käyttää vanhan rakenteen tyyppi-tietoa koska se on pääsääntöisesti email kaikille):
    if kirjeTyyppinen then
      -- Pohja ei ole asiointitili-tyyppinen JA
      -- Joko kirje ei ole email-tyyyppinen tai kirjeestä löytyy kirjepohjan niminen sisältöosio ja se on email-tyyppinen,
      -- joten oletetaan, että tästä on tarkoitus luoda myös kirjeitä:
      insert into kirjeet.sisalto_rakenne(rakenne, tyyppi, tyyli)
        values (rakenneId, 'letter', tyyliId);
      select into sisaltoRakenneId max(id) from kirjeet.sisalto_rakenne;
      raise info ' > Luotiin letter-sisältörakenne %', sisaltoRakenneId;

      for sisalto in select s.* from kirjeet.sisalto s where s.kirjepohja_id = _id
                                                             and s.nimi != EMAI_POHJA_SISALTO_TYYPPI
                     order by s.jarjestys, s.id loop
        sisaltoJarjestysNro := sisaltoJarjestysNro+1;

        insert into kirjeet.rakenne_sisalto(nimi, sisalto, tyyppi)
          values (sisalto.nimi, sisalto.sisalto, 'html');
        sisaltoJarjestysNro := case when sisalto.jarjestys is not null then greatest(sisalto.jarjestys, sisaltoJarjestysNro)
                               else sisaltoJarjestysNro end;
        insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, rooli, sisalto, jarjestys)
          values (sisaltoRakenneId, 'body', (select max(id) from kirjeet.rakenne_sisalto), sisaltoJarjestysNro);
        raise info ' > Luotiin body-rooliin sisältöelementti % järjestysnumerolle %', (select max(id) from
          kirjeet.rakenne_sisalto), sisaltoJarjestysNro;
      end loop;
    end if;

    if emailTyyppinen then
      -- Kirjeestä löytyy email_body, eli tehdään siitä (myös) email-tyyppinen sisalto_rakenne:
      insert into kirjeet.sisalto_rakenne(rakenne, tyyppi, tyyli)
        values (rakenneId, 'email', tyyliId);
      select into sisaltoRakenneId max(id) from kirjeet.sisalto_rakenne;
      raise info ' > Luotiin email-sisältörakenne %', sisaltoRakenneId;

      -- Luodaan email_body sisältö ja linkitetään se:
      select into sisalto s.* from kirjeet.sisalto s where s.kirjepohja_id = _id and s.nimi = EMAI_POHJA_SISALTO_TYYPPI;
      insert into kirjeet.rakenne_sisalto(nimi, sisalto, tyyppi)
        values (EMAI_POHJA_SISALTO_TYYPPI, sisalto.sisalto, 'html');
      sisaltoJarjestysNro := case when sisalto.jarjestys is not null then greatest(sisalto.jarjestys, sisaltoJarjestysNro+1)
                             else sisaltoJarjestysNro end;
      insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, rooli, sisalto, jarjestys)
        values (sisaltoRakenneId, 'body', (select max(id) from kirjeet.rakenne_sisalto), sisaltoJarjestysNro);
      raise info ' > Luotiin body-rooliin sisältöelementti % järjestysnumerolle %',
      (select max(id) from kirjeet.rakenne_sisalto), sisaltoJarjestysNro;

      if kirjeTyyppinen then
        raise info ' > Luodaan liitteet kuten kirjeillä muille kuin % ja % -sisällöille', EMAI_POHJA_SISALTO_TYYPPI, PAA_SISALTO_TYYPPI;
        for sisalto in select s.* from kirjeet.sisalto s where s.kirjepohja_id = _id
                                                               and s.nimi not in (EMAI_POHJA_SISALTO_TYYPPI,  PAA_SISALTO_TYYPPI)
                       order by s.jarjestys, s.id loop
          sisaltoJarjestysNro := sisaltoJarjestysNro+1;
          raise info ' > Liiteen % sisältöosio', sisalto.nimi;

          insert into kirjeet.rakenne_sisalto(nimi, sisalto, tyyppi)
            values (sisalto.nimi, sisalto.sisalto, 'html');
          select into sisaltoId max(id) from kirjeet.rakenne_sisalto;
          raise info ' > Luotiin uusi sisältö %', sisaltoId;

          sisaltoJarjestysNro := case when sisalto.jarjestys is not null
          then greatest(sisalto.jarjestys, sisaltoJarjestysNro) else sisaltoJarjestysNro end;
          insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, rooli, sisalto, jarjestys)
            values (sisaltoRakenneId, 'attachment', sisaltoId, sisaltoJarjestysNro);
          raise info ' > Luotiin attachment-rooliin sisältöelementtilinkitys % järjestysnumerolle %',
          (select max(id) from kirjeet.rakenne_sisalto), sisaltoJarjestysNro;
        end loop;
      else
        if (select exists(select s.* from kirjeet.sisalto s where s.kirjepohja_id = _id and s.nimi = LIITE_SISALTO_TYYPPI)) then
          raise info ' > Luodaan liitteet kuten pelkillä sähköposteilla %-tyyppisille sisällöille', LIITE_SISALTO_TYYPPI;

          -- Lisätään sähköpostin liitteet:
          for sisalto in select s.* from kirjeet.sisalto s where s.kirjepohja_id = _id and s.nimi = LIITE_SISALTO_TYYPPI
                         order by s.jarjestys, s.id loop
            sisaltoJarjestysNro := sisaltoJarjestysNro+1;
            raise info ' > Liiteen % sisältöosio', sisalto.nimi;

            insert into kirjeet.rakenne_sisalto(nimi, sisalto, tyyppi)
              values (sisalto.nimi, sisalto.sisalto, 'html');
            select into sisaltoId max(id) from kirjeet.rakenne_sisalto;
            raise info ' > Luotiin uusi sisältö %', sisaltoId;

            sisaltoJarjestysNro := case when sisalto.jarjestys is not null
            then greatest(sisalto.jarjestys, sisaltoJarjestysNro) else sisaltoJarjestysNro end;
            insert into kirjeet.sisalto_rakenne_sisalto(sisalto_rakenne, rooli, sisalto, jarjestys)
              values (sisaltoRakenneId, 'attachment', sisaltoId, sisaltoJarjestysNro);
            raise info ' > Luotiin attachment-rooliin sisältöelementtilinkitys % järjestysnumerolle %',
            (select max(id) from kirjeet.rakenne_sisalto), sisaltoJarjestysNro;
          end loop;
        end if;
      end if;
      perform kirjeet.luoSisallonOsa(sisaltoRakenneId, kirjeet.luoSisalto('email_otsikko', '$subject', 'plain'), 'header');
    end if;
  end if;
  raise info 'Migratoitu vanha pohja % palautetaan rakenne=%', vanhaPohja.id, rakenneId;
  return rakenneId;
end;
$$ language plpgsql;