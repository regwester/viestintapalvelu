ALTER TABLE public.raportoitavaviesti ADD COLUMN kirjelahetys_id int8;
ALTER TABLE public.raportoitavaviesti ADD CONSTRAINT unique_raportoitavaviesti_kirjelahetys_id UNIQUE(kirjelahetys_id);
