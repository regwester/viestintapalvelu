package fi.vm.sade.ryhmasahkoposti.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.dom4j.DocumentException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.vm.sade.ryhmasahkoposti.api.dto.ReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateContentDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;

public class TemplateBuilder {

    private static Logger LOGGER = LoggerFactory.getLogger(TemplateBuilder.class);

    private VelocityEngine templateEngine = new VelocityEngine();

    /**
     * Build template content without any replacements
     * 
     * @param template
     * @return
     */
    public String buildTemplate(TemplateDTO template) {

	if (template == null) {
	    LOGGER.error("Template is null");
	    return null;
	}

	// Create a list to sort
	List<TemplateContentDTO> contentList = new ArrayList<TemplateContentDTO>();
	for (TemplateContentDTO templateContent : template.getContents()) {
	    contentList.add(templateContent);
	}

	// Sort
	Collections.sort(contentList);

	// Create page
	StringBuffer result = new StringBuffer();
	for (TemplateContentDTO tc : contentList) {

	    String page = createPage(template, tc.getContent().getBytes()); 
	    result.append(page);
	}

	return result.toString();
    }

    /**
     * Build template content without any replacements
     * 
     * @param template
     * @return
     */
    public String buildTempleMessage(String message, 
	    List<ReplacementDTO> messageReplacements, 
	    List<ReportedRecipientReplacementDTO> recipientReplacements) {

	Map<String, Object> replacements = new HashMap<String, Object>();        	

	// Place message replacements
	if (messageReplacements != null) {
	    for (ReplacementDTO repl : messageReplacements) {
		replacements.put(repl.getName(), repl.getDefaultValue());
	    }
	}

	// Place user replacements
	if (recipientReplacements != null) {
	    for (ReportedRecipientReplacementDTO repl : recipientReplacements) {
		replacements.put(repl.getName(), repl.getDefaultValue());
	    }
	}

	return createContent(message, replacements); 

    }


    /**
     * Create message content
     * 
     * @param message
     * @param replacements
     * @return
     */
    private String createContent(String message, Map<String, Object> replacements) {

	@SuppressWarnings("unchecked")
	Map<String, Object> dataContext = createDataContext(replacements);

	StringWriter writer = new StringWriter();
	templateEngine.evaluate(new VelocityContext(dataContext), writer, "LOG", 
		new InputStreamReader(new ByteArrayInputStream(message.getBytes())));
	return writer.toString();
    }

    /**
     * Create data context
     * @param replacementsList
     * @return
     */
    private Map<String, Object> createDataContext(Map<String, Object>... replacementsList) {

	Map<String, Object> data = new HashMap<String, Object>();
	for (Map<String, Object> replacements : replacementsList) {
	    if (replacements != null) {
		for (String key : replacements.keySet()) {
		    if (replacements.get(key) instanceof String) {
			data.put(key, cleanHtmlFromApi((String) replacements.get(key)));
		    } else {
			data.put(key, replacements.get(key));
		    }
		}
	    }
	}

	data.put("letterDate",
		new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
	return data;
    }

    private String cleanHtmlFromApi(String string) {
	return Jsoup.clean(string, Whitelist.relaxed());
    }

    /**
     * Create page
     * 
     * @param template
     * @param pageContent
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws DocumentException
     */
    private String createPage(TemplateDTO template, byte[] pageContent) {

	Map<String, Object> dataContext = new HashMap<String, Object>();
	String styles = template.getStyles();
	if (styles == null) {
	    styles = "";
	}
	dataContext.put("tyylit", styles);
        dataContext.put("letterDate", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));

	StringWriter writer = new StringWriter();
	templateEngine.evaluate(new VelocityContext(dataContext), writer, "LOG", new InputStreamReader(new ByteArrayInputStream(pageContent)));
	return writer.toString();

    }
}
