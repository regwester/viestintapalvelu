package fi.vm.sade.dto;

public class OrganisaatioHenkiloDto {

    private String organisaatioOid;
    private boolean passivoitu;

    public String getOrganisaatioOid() {
        return organisaatioOid;
    }

    public void setOrganisaatioOid(String organisaatioOid) {
        this.organisaatioOid = organisaatioOid;
    }

    public boolean isPassivoitu() {
        return passivoitu;
    }

    public void setPassivoitu(boolean passivoitu) {
        this.passivoitu = passivoitu;
    }

}
