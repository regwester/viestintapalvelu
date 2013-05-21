package fi.vm.sade.viestintapalvelu.address;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;

public class AddressLabelBuilder {
	private DocumentBuilder documentBuilder;

	@Inject
	public AddressLabelBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	public byte[] printPDF(AddressLabelBatch input) throws DocumentException, IOException {
		Map<String, Object> context = createDataContext(input.getAddressLabels(), new DecoratorBuilder() {
			protected AddressLabelDecorator newDecorator(AddressLabel addressLabel) {
				return new HtmlAddressLabelDecorator(addressLabel);
			}
		});
		byte[] xhtml = documentBuilder.applyTextTemplate(input.getTemplateName(), context);
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	public byte[] printCSV(AddressLabelBatch input) throws DocumentException, IOException {
		Map<String, Object> context = createDataContext(input.getAddressLabels(), new DecoratorBuilder() {
			protected AddressLabelDecorator newDecorator(AddressLabel addressLabel) {
				return new XmlAddressLabelDecorator(addressLabel);
			}
		});
		return documentBuilder.applyTextTemplate(input.getTemplateName(), context);
	}
	
	private Map<String, Object> createDataContext(List<AddressLabel> addressLabels, final DecoratorBuilder builder) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("labelList", Lists.transform(addressLabels, new Function<AddressLabel, AddressLabelDecorator>() {
			public AddressLabelDecorator apply(AddressLabel label) {
				return builder.newDecorator(label);
			}
		}));
		return data;
	}
	
	private abstract class DecoratorBuilder {
		protected abstract AddressLabelDecorator newDecorator(AddressLabel addressLabel);
	}
}
