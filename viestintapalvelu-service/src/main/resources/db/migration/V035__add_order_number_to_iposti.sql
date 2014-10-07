
alter table kirjeet.iposti add column jarjestysnumero int;
-- ei laiteta, koska vanhalla datalle ei näitä ole:
--alter table kirjeet.iposti add constraint iposti_jarjestysnumero_unique_per_batch
--    unique ( kirjelahetys_id, jarjestysnumero );
