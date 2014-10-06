
alter table kirjeet.vastaanottaja add column iposti int8 references kirjeet.iposti(id);
