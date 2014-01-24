package fi.vm.sade.viestintapalvelu.koekutsukirje;

import java.util.List;

public class KoekutsukirjeBatch {
    private List<Koekutsukirje> letters;

    public KoekutsukirjeBatch() {
    }

    public KoekutsukirjeBatch(List<Koekutsukirje> letters) {
        this.letters = letters;
    }

    public List<Koekutsukirje> getLetters() {
        return letters;
    }

    @Override
    public String toString() {
        return "KoekutsukirjeBatch [letters=" + letters + "]";
    }
}
