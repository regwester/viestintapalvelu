package fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.application.Constants;
import fi.vm.sade.viestintapalvelu.infrastructure.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.infrastructure.LiiteBuilder;
import fi.vm.sade.viestintapalvelu.infrastructure.PdfBuilder;
import fi.vm.sade.viestintapalvelu.infrastructure.PdfDocument;
import fi.vm.sade.viestintapalvelu.infrastructure.Utils;

public class HyvaksymiskirjeBuilder extends PdfBuilder {
	@Inject
	public HyvaksymiskirjeBuilder(DocumentBuilder documentBuilder,
			LiiteBuilder liiteBuilder) {
		super(documentBuilder, liiteBuilder);
	}

	public byte[] printPDF(HyvaksymiskirjeBatch batch) throws IOException,
			DocumentException {
		List<PdfDocument> source = new ArrayList<PdfDocument>();
		for (Hyvaksymiskirje kirje : batch.getLetters()) {
			System.out.println("kirje: " + kirje);
			String kirjeTemplateName = Utils
					.resolveTemplateName(Constants.HYVAKSYMISKIRJE_TEMPLATE,
							kirje.getLanguageCode());
			byte[] frontPage = createFirstPagePDF(kirjeTemplateName,
					kirje.getPostalAddress(), kirje.getKoulu(),
					kirje.getKoulutus());
			String liiteTemplateName = Utils.resolveTemplateName(
					Constants.LIITE_TEMPLATE, kirje.getLanguageCode());
			byte[] attachment = liiteBuilder.printPDF(liiteTemplateName,
					kirje.getTulokset());
			source.add(new PdfDocument(kirje.getPostalAddress(), frontPage,
					attachment));
		}
		return documentBuilder.merge(source).toByteArray();
	}

}
