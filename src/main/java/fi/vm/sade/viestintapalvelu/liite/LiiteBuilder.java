package fi.vm.sade.viestintapalvelu.liite;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.XmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiiteBuilder {

    private DocumentBuilder documentBuilder;

    @Inject
    public LiiteBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public byte[] printPDF(final String templateName,
                           final AddressLabel addressLabel,
                           final List<Map<String, String>> tulokset) throws IOException, DocumentException {
        Map<String, Object> dataContext = createDataContext(addressLabel, tulokset);
        byte[] xhtml = documentBuilder.applyTextTemplate(templateName,
                dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    private Map<String, Object> createDataContext(final AddressLabel addressLabel,
                                                  final List<Map<String, String>> tulokset) {
        Map<String, Boolean> columns = distinctColumns(tulokset);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("addressLabel", new XmlAddressLabelDecorator(addressLabel));
        data.put("tulokset", normalizeColumns(columns, tulokset));
        data.put("columns", columns);
        return data;
    }

    private List<Map<String, String>> normalizeColumns(
            Map<String, Boolean> columns, List<Map<String, String>> tulokset) {
        for (Map<String, String> row : tulokset) {
            for (String column : columns.keySet()) {
                if (!row.containsKey(column) || row.get(column) == null) {
                    row.put(column, "");
                }
                row.put(column, StringEscapeUtils.escapeHtml(row.get(column)));
            }
        }
        return tulokset;
    }

    private Map<String, Boolean> distinctColumns(
            List<Map<String, String>> tulokset) {
        Map<String, Boolean> printedColumns = new HashMap<String, Boolean>();
        for (Map<String, String> haku : tulokset) {
            for (String column : haku.keySet()) {
                printedColumns.put(column, true);
            }
        }
        return printedColumns;
    }
}
