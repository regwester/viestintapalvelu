package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import javax.validation.constraints.NotNull;

public class Tulos {
    @NotNull
    private String organisaationNimi;
    @NotNull
    private String oppilaitoksenNimi;
    @NotNull
    private String hakukohteenNimi;
    @NotNull
    private String hyvaksytyt;
    @NotNull
    private String kaikkiHakeneet;
    @NotNull
    private String omatPisteet;
    @NotNull
    private String alinHyvaksyttyPistemaara;
    @NotNull
    private String paasyJaSoveltuvuuskoe;
    @NotNull
    private String valinnanTulos;
    @NotNull
    private String selite;

    public Tulos() {
    }

    public String getOrganisaationNimi() {
        return organisaationNimi;
    }

    public String getOppilaitoksenNimi() {
        return oppilaitoksenNimi;
    }

    public String getHakukohteenNimi() {
        return hakukohteenNimi;
    }

    public String getHyvaksytyt() {
        return hyvaksytyt;
    }

    public String getKaikkiHakeneet() {
        return kaikkiHakeneet;
    }

    public String getOmatPisteet() {
        return omatPisteet;
    }

    public String getAlinHyvaksyttyPistemaara() {
        return alinHyvaksyttyPistemaara;
    }

    public String getPaasyJaSoveltuvuuskoe() {
        return paasyJaSoveltuvuuskoe;
    }

    public String getValinnanTulos() {
        return valinnanTulos;
    }

    public String getSelite() {
        return selite;
    }
}
