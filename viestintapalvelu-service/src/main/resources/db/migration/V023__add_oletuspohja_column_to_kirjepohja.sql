
alter table kirjeet.kirjepohja add column oletuspohja boolean;
update kirjeet.kirjepohja set oletuspohja = false;
alter table kirjeet.kirjepohja alter column oletuspohja set not null;
