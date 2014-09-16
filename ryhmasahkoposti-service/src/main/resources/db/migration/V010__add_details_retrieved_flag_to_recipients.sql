ALTER TABLE public.raportoitavavastaanottaja ADD COLUMN henkilotiedot_haettu BOOLEAN NOT NULL DEFAULT false;
UPDATE public.raportoitavavastaanottaja SET henkilotiedot_haettu = true;