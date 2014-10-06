
update kirjeet.kirjelahetys set iposti = EXISTS (select kl.id
  from kirjeet.kirjelahetys kl
    inner join kirjeet.iposti kip on kip.kirjelahetys_id = kl.id
  where kl.id = kirjeet.kirjelahetys.id) where iposti is null;
alter table kirjeet.kirjelahetys alter column iposti set not null;
alter table kirjeet.kirjelahetys alter column iposti set default false;
