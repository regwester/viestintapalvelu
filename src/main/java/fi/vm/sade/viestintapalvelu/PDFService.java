package fi.vm.sade.viestintapalvelu;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PDFService {
	
	public void generatePDF(List<String> students) {
		for (String student : students) {
			writeDocument(student, generatePDF(student));
		}
	}
	
	private byte[] generatePDF(String name) {
		return new byte[] {1,0};
	}
	
	private void writeDocument(String name, byte[] document) {
		try {
			FileOutputStream fos = new FileOutputStream("documents/"+name+".pdf", false);
			fos.write(document, 0, document.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new PDFService().generatePDF(Arrays.asList(args));
	}

}
