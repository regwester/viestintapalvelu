package fi.vm.sade.viestintapalvelu.model.types;

import java.util.Optional;

public class ContentTypes {
    public static final String CONTENT_TYPE_PDF = "application/pdf";
    public static final String CONTENT_TYPE_HTML = "text/html";
    private static final String FILE_SUFFIX_PDF = ".pdf";
    private static final String FILE_SUFFIX_HTML = ".html";

    public static Optional<String> getFileSuffix(String contentType) {
        switch (contentType) {
            case CONTENT_TYPE_PDF:
                return Optional.of(FILE_SUFFIX_PDF);
            case CONTENT_TYPE_HTML:
                return Optional.of(FILE_SUFFIX_HTML);
        }
        return Optional.empty();
    }
}
