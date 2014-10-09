package fi.vm.sade.ryhmasahkoposti.util;

public enum CallingProcess {

    OSOITETIETOJARJESTELMA("Osoitetietojarjestelma"),
    HENKILOPALVELU("Henkilopalvelu"),
    VALINTA("Valinta"),
    VIESTINTAPALVELU("Viestintapalvelu");
    
    private String name;
    
    private CallingProcess(String name) {
       this.name = name;
    }
    public String toString() {
        return this.name;
    }
    
    public boolean equals(String input) {
        return this.name.equalsIgnoreCase(input);
    }
    
    public static CallingProcess getByName(String provided) {
        if (provided != null) {
            for (CallingProcess cp : CallingProcess.values()) {
                if (cp.name.equalsIgnoreCase(provided)) {
                    return cp;
                }
            }
        }
        return null;
    }
}
