package fi.vm.sade.viestintapalvelu.model.types;

import java.util.Optional;

public class ContentTypeMapper {
    private static final String FILE_SUFFIX_PDF = ".pdf";
    private static final String FILE_SUFFIX_HTML = ".html";

    public static Optional<String> getFileSuffix(ContentStructureType contentStructureType) {
        switch (contentStructureType) {
            case letter:
                return Optional.of(FILE_SUFFIX_PDF);
            case accessibleHtml:
                return Optional.of(FILE_SUFFIX_HTML);
        }
        return Optional.empty();
    }
}
