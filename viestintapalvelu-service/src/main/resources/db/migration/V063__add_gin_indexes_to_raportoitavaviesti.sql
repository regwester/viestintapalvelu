create extension btree_gin;

create index raportoitavaviesti_hakunimi_gin on raportoitavaviesti using gin (viesti);


create index raportoitavavastaanottaja_hakunimi_gin on raportoitavavastaanottaja using gin (to_tsvector('simple', hakunimi));