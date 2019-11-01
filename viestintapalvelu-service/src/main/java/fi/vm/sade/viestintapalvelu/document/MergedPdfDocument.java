/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.document;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.pdfbox.util.PDFMergerUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class MergedPdfDocument {
    private List<DocumentMetadata> documentMetadata;
    private ByteArrayOutputStream output;

    public MergedPdfDocument() {
        this.documentMetadata = new ArrayList<>();
        this.output = new ByteArrayOutputStream();
    }

    public void write(PdfDocument pdfDocument) throws IOException, COSVisitorException {
        final ByteArrayOutputStream intermediaryOutput = new ByteArrayOutputStream();

        try {
            final PDFMergerUtility pdfMerger = new PDFMergerUtility();
            pdfMerger.setDestinationStream(intermediaryOutput);
            IntStream.range(0, pdfDocument.getContentSize())
                    .mapToObj(pdfDocument::getContentStream)
                    .forEachOrdered(pdfMerger::addSource);
            pdfMerger.mergeDocuments();
        } finally {
            intermediaryOutput.flush();
        }

        InputStream is = null;
        PDDocument document = null;
        try {
            is = new ByteArrayInputStream(intermediaryOutput.toByteArray());
            document = PDDocument.load(is);

            final PDDocumentCatalog documentCatalog = document.getDocumentCatalog();
            documentCatalog.setViewerPreferences(new PDViewerPreferences(new COSDictionary()));
            documentCatalog.getViewerPreferences().setDisplayDocTitle(true);

            documentMetadata.add(new DocumentMetadata(
                    pdfDocument.getAddressLabel(),
                    1,
                    document.getNumberOfPages()
            ));

            document.save(output);
        } finally {
            close(is);
            close(document);
        }
    }

    private void close(PDDocument pdDocument) throws IOException {
        if (pdDocument != null) {
            pdDocument.close();
        }
    }

    private void close(InputStream is) throws IOException {
        if (is != null) {
            is.close();
        }
    }

    public List<DocumentMetadata> getDocumentMetadata() {
        return documentMetadata;
    }

    public byte[] toByteArray() {
        return output.toByteArray();
    }
}
