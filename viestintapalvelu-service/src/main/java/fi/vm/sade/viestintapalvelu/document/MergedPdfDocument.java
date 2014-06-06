package fi.vm.sade.viestintapalvelu.document;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MergedPdfDocument {
    private List<DocumentMetadata> documentMetadata;
    private Document document = new Document();
    private PdfWriter writer;
    private ByteArrayOutputStream output;
    private PdfContentByte pdfContentByte;
    private int currentPageNumber;

    public MergedPdfDocument() throws DocumentException {
        this.documentMetadata = new ArrayList<DocumentMetadata>();
        this.document = new Document();
        this.output = new ByteArrayOutputStream();
        this.writer = PdfWriter.getInstance(document, output);
        this.document.open();
        this.pdfContentByte = writer.getDirectContent();
        this.currentPageNumber = 0;
    }

    public void write(PdfDocument pdfDocument) throws IOException {
        int startPage = currentPageNumber + 1;
        int pages = 0;
        if (pdfDocument.getFrontPage() != null) {
            pages += write(pdfDocument.getFrontPage());
        }
        if (pdfDocument.getAttachment() != null) {
            pages += write(pdfDocument.getAttachment());
        }
        if (pdfDocument.getContentSize() > 0) {
            for (int i = 0 ; i < pdfDocument.getContentSize() ; i++) {
                pages += write(pdfDocument.getContentStream(i));
            }
        }
        documentMetadata.add(new DocumentMetadata(
                pdfDocument.getAddressLabel(), startPage, pages));
    }

    private int write(InputStream in) throws IOException {
        PdfReader reader = new PdfReader(in);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            document.newPage();
            currentPageNumber++;
            PdfImportedPage page = writer.getImportedPage(reader, i);
            pdfContentByte.addTemplate(page, 0, 0);
        }
        return reader.getNumberOfPages();
    }

    public List<DocumentMetadata> getDocumentMetadata() {
        return documentMetadata;
    }

    public byte[] toByteArray() {
        return output.toByteArray();
    }

    public void flush() throws IOException {
        output.flush();
        document.close();
        output.close();
    }
}
