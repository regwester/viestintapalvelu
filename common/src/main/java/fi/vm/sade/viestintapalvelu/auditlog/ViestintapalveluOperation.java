package fi.vm.sade.viestintapalvelu.auditlog;

import fi.vm.sade.auditlog.Operation;


public enum ViestintapalveluOperation implements Operation {
    KIRJE_LUKU,
    KIRJELAHETYS_LUKU,
    KIRJE_KOOSTE_LUKU,
    SAHKOPOSTI_LUKU,
    KIRJEPOHJA_LUONTI,
    KIRJEPOHJA_LUKU,
    KIRJEPOHJA_MUOKKAUS,
    KIRJEPOHJA_LIITTAMINEN_HAKUUN,
    KIRJEPOHJA_LUONNOS_TALLENNUS,
    KIRJEPOHJA_LUONNOS_MUOKKAUS;
}
