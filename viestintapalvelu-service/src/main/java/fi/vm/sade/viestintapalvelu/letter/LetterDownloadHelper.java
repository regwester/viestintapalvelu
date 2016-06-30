package fi.vm.sade.viestintapalvelu.letter;

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
        return "";
    }

    public static Response downloadPdfResponse(String filename, HttpServletResponse response, byte[] data) {
        response.setHeader("Content-Type", "application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".pdf\"");
        response.setHeader("Content-Length", String.valueOf(data.length));
        response.setHeader("Cache-Control", "private");
        return Response.ok(data).type("application/pdf").build();
    }
}
