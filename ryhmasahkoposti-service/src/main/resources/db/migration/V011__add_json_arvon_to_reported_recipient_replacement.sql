
alter table raportoitavavastaanottajakorvauskentat add column json_arvo text;
alter table raportoitavavastaanottajakorvauskentat add constraint oletus_arvo_tai_json_arvo
  check (
      (oletus_arvo is not null and json_arvo is null)
    or (oletus_arvo is null and json_arvo is not null)
    or (oletus_arvo is null and json_arvo is null) -- toistaiseksi näytti, että oletus_arvon null on sallttu
);
