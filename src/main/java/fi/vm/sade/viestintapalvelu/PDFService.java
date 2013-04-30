package fi.vm.sade.viestintapalvelu;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFService {
	
	public void createDocuments(List<String> students) {
		for (String student : students) {
			createDocumentFor(student);
		}
	}
	
	private void createDocumentFor(String student) {
		PDDocument doc = null;
		OutputStream fos = null;
		try {
			doc = new PDDocument();
			PDPage page = new PDPage();
			doc.addPage(page);
			PDFont font = PDType1Font.HELVETICA_BOLD;
			PDPageContentStream stream = new PDPageContentStream(doc, page);
			stream.beginText();
			stream.setFont(font, 24);
			stream.moveTextPositionByAmount(100, 700);
			stream.drawString(student);
			stream.endText();
			stream.close();
			fos = new FileOutputStream("documents/"+student+".pdf", false);
			doc.save(fos);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (COSVisitorException e) {
			e.printStackTrace();
		} finally {
			try {
				doc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new PDFService().createDocuments(Arrays.asList(args));
	}

}
