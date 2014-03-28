package fi.vm.sade.viestintapalvelu.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.TemplateContent;
import fi.vm.sade.viestintapalvelu.model.Replacement;

@Component
public class TemplateService {

    public Template getTemplateFromFiles(String languageCode, String... names)
            throws IOException {
        Template result = new Template();
        Set<TemplateContent> contents = new HashSet<TemplateContent>();

        int i = 1;
        for (String name : names) {
            String templateName = Utils.resolveTemplateName(name, languageCode);
            if (templateName != null) {
                BufferedReader buff = new BufferedReader(new InputStreamReader(
                        getClass().getResourceAsStream(templateName)));
                StringBuilder sb = new StringBuilder();

                String line = buff.readLine();
                while (line != null) {
                    sb.append(line);
                    line = buff.readLine();
                }
                TemplateContent content = new TemplateContent();
                content.setName(name);
                content.setContent(sb.toString());
                content.setOrder(i++);
                contents.add(content);
            }
        }
        result.setContents(contents);

        Replacement replacement = new Replacement();
        replacement.setName("$letterBodyText");
        Set<Replacement> replacements = new HashSet<Replacement>();
        replacements.add(replacement);
        result.setReplacements(replacements);
        return result;
    }

    public Template template(String name, String languageCode)
            throws IOException, DocumentException {

        Template result = new Template();
        String templateName = Utils.resolveTemplateName(name, languageCode);
        BufferedReader buff = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(templateName)));
        StringBuilder sb = new StringBuilder();

        String line = buff.readLine();
        while (line != null) {
            sb.append(line);
            line = buff.readLine();
        }

        TemplateContent content = new TemplateContent();
        content.setName(templateName);
        content.setContent(sb.toString());
        List<TemplateContent> contents = new ArrayList<TemplateContent>();
        contents.add(content);

        Replacement replacement = new Replacement();
        replacement.setName("$letterBodyText");
        ArrayList<Replacement> rList = new ArrayList<Replacement>();
        rList.add(replacement);
        return result;
    }

}
