alter table kirjeet.kirjepohja add column tila character varying(64) default 'luonnos' CHECK (tila in ('suljettu', 'julkaistu', 'luonnos'));

update kirjeet.kirjepohja set tila = 'julkaistu';