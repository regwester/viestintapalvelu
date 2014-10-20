
update kirjeet.vastaanottajakirje set sisaltotyyppi = 'application/pdf' where sisaltotyyppi is null
    or trim(sisaltotyyppi) = '';
update kirjeet.vastaanottajakirje set alkuperainensisaltotyyppi = sisaltotyyppi where alkuperainensisaltotyyppi is null
    or trim(alkuperainensisaltotyyppi) = '';
