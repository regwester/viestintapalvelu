create extension if not exists btree_gin;

create index if not exists raportoitavaviesti_viesti_prosessi_aihe_gin
    on raportoitavaviesti using gin (to_tsvector('simple', viesti || prosessi || aihe));

create index if not exists raportoitavavastaanottaja_hakunimi_gin
    on raportoitavavastaanottaja using gin (to_tsvector('simple', hakunimi));