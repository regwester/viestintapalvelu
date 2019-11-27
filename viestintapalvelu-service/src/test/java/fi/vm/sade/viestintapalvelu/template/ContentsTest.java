package fi.vm.sade.viestintapalvelu.template;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContentsTest {
    private static final List<TemplateContent> ALL_CONTENTS = Arrays.asList(
            templateContent(Contents.ASIOINTITILI_HEADER),
            templateContent(Contents.ASIOINTITILI_CONTENT),
            templateContent(Contents.ASIOINTITILI_SMS_CONTENT),
            templateContent(Contents.EMAIL_SUBJECT),
            templateContent(Contents.EMAIL_BODY),
            templateContent(Contents.ATTACHMENT),
            templateContent(Contents.ACCESSIBLE_HTML_CONTENT),
            templateContent(Contents.LETTER_CONTENT_CONTENT_NAME)
    );

    private static TemplateContent templateContent(final String contentName) {
        return new TemplateContent() {
            @Override
            public String getName() {
                return contentName;
            }

            @Override
            public String toString() {
                return "[TemplateContent [name=" + contentName + "]]";
            }
        };
    }

    @Test
    public void includesPdfTemplateContents() {
        final Collection<TemplateContent> actual = Contents
                .pdfLetterContents()
                .filter(ALL_CONTENTS);
        assertTrue(listContains(Contents.LETTER_CONTENT_CONTENT_NAME, actual));
        assertEquals(
                "Actual TemplateContent list included zero or too many items. All items: " + actual.toString(),
                1,
                actual.size()
        );
    }

    @Test
    public void includesAccessibleHtmlTemplateContents() {
        final Collection<TemplateContent> actual = Contents
                .accessibleHtmlContents()
                .filter(ALL_CONTENTS);
        assertTrue(listContains(Contents.ACCESSIBLE_HTML_CONTENT, actual));
        assertEquals(
                "Actual TemplateContent list included zero or too many items. All items: " + actual.toString(),
                1,
                actual.size()
        );
    }

    private boolean listContains(
            final String expectedContentName,
            final Collection<TemplateContent> actual) {
        return actual
                .stream()
                .anyMatch(templateContent -> templateContent.getName().equals(expectedContentName));
    }
}
