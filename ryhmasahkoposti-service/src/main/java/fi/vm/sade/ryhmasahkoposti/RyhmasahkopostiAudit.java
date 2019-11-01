package fi.vm.sade.ryhmasahkoposti;

import fi.vm.sade.auditlog.ApplicationType;
import fi.vm.sade.auditlog.Audit;
import fi.vm.sade.viestintapalvelu.auditlog.AuditLogger;

public class RyhmasahkopostiAudit {
    public static final Audit AUDIT = new Audit(new AuditLogger(), "ryhmasahkoposti-service", ApplicationType.VIRKAILIJA);
}
