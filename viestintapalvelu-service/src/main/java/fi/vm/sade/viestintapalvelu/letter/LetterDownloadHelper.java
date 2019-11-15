package fi.vm.sade.viestintapalvelu.letter;

import fi.vm.sade.viestintapalvelu.model.types.ContentTypes;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

public class LetterDownloadHelper {

    public static String determineExtension(String contentType) {
        if (contentType == null) {
            return "";
        }
        if (contentType.endsWith("/zip")) {
            return ".zip";
        }
        if (contentType.endsWith("/pdf")) {
            return ".pdf";
        }
        if (contentType.equals(ContentTypes.CONTENT_TYPE_HTML)) {
            return ".html";
        }
        return "";
    }

    public static Response downloadPdfResponse(String filename, HttpServletResponse response, byte[] data) {
        response.setHeader("Content-Type", ContentTypes.CONTENT_TYPE_PDF);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".pdf\"");
        response.setHeader("Content-Length", String.valueOf(data.length));
        response.setHeader("Cache-Control", "private");
        return Response.ok(data).type(ContentTypes.CONTENT_TYPE_PDF).build();
    }

    public static Response downloadInlineResponse(final HttpServletResponse response, final byte[] letterContents) {
        response.setHeader("Content-Type", ContentTypes.CONTENT_TYPE_HTML + "; encoding=UTF-8");
        response.setHeader("Content-Disposition", "inline");
        response.setHeader("Content-Length", String.valueOf(letterContents.length));
        response.setHeader("Cache-Control", "private");
        return Response.ok(letterContents).type(ContentTypes.CONTENT_TYPE_HTML).build();
    }
}
