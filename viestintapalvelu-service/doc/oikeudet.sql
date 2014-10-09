-- For authentcation database

-- ROLE_APP_ASIAKIRJAPALVELU_SEND_LETTER_EMAIL
begin;
insert INTO text_group VALUES (nextval('hibernate_sequence'), 0);
insert into text VALUES (nextval('hibernate_sequence'), 0, 'FI', 'Sähköpostin lähetys kirjeestä', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'SV', 'Sähköpostin lähetys kirjeestä', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'EN', 'Sähköpostin lähetys kirjeestä', (select max(id) from text_group));

insert INTO kayttooikeus VALUES (nextval('hibernate_sequence'), 0, (select id from palvelu where name = 'ASIAKIRJAPALVELU'),
		'SEND_LETTER_EMAIL', (select max(id) from text_group));
commit;

-- ROLE_APP_ASIAKIRJAPALVELU_SYSTEM_ATTACHMENT_DOWNLOAD:
begin;
insert INTO text_group VALUES (nextval('hibernate_sequence'), 0);
insert into text VALUES (nextval('hibernate_sequence'), 0, 'FI', 'Kirjelähetyksen sähköpostiliitteiden lataus (järjestelmien välinen)', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'SV', 'Kirjelähetyksen sähköpostiliitteiden lataus (järjestelmien välinen)', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'EN', 'Kirjelähetyksen sähköpostiliitteiden lataus (järjestelmien välinen)', (select max(id) from text_group));

insert INTO kayttooikeus VALUES (nextval('hibernate_sequence'), 0, (select id from palvelu where name = 'ASIAKIRJAPALVELU'),
		'SYSTEM_ATTACHMENT_DOWNLOAD', (select max(id) from text_group));
commit;

-- ROLE_APP_IPOSTI_READ
-- ROLE_APP_IPOSTI_SEND
begin;
insert INTO text_group VALUES (nextval('hibernate_sequence'), 0);
insert into text VALUES (nextval('hibernate_sequence'), 0, 'FI', 'IPosti', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'SV', 'IPosti', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'EN', 'IPosti', (select max(id) from text_group));

insert INTO palvelu (id, version, name, palvelutyyppi, textgroup_id) VALUES (nextval('hibernate_sequence'), 0,
		'IPOSTI', 'YKSITTAINEN', (select max(id) from text_group));

insert INTO text_group VALUES (nextval('hibernate_sequence'), 0);
insert into text VALUES (nextval('hibernate_sequence'), 0, 'FI', 'Lukuoikeus', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'SV', 'Read accessright', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'EN', 'Read accessright', (select max(id) from text_group));
insert INTO kayttooikeus VALUES (nextval('hibernate_sequence'), 0, (select id from palvelu where name = 'IPOSTI'),
		'READ', (select max(id) from text_group));

insert INTO text_group VALUES (nextval('hibernate_sequence'), 0);
insert into text VALUES (nextval('hibernate_sequence'), 0, 'FI', 'Lähetysoikeus', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'SV', 'Send permission', (select max(id) from text_group));
insert into text VALUES (nextval('hibernate_sequence'), 0, 'EN', 'Send permission', (select max(id) from text_group));
insert INTO kayttooikeus VALUES (nextval('hibernate_sequence'), 0, (select id from palvelu where name = 'IPOSTI'),
		'SEND', (select max(id) from text_group));
commit;