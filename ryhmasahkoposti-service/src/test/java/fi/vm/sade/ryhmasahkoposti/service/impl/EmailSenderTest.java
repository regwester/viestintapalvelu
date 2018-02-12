package fi.vm.sade.ryhmasahkoposti.service.impl;

import com.google.common.base.Optional;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.util.ReflectionTestUtils;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Arrays;
import java.util.Comparator;

import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4.class)
public class EmailSenderTest {


    private EmailSender emailSender;
    @Before
    public void setup() {
        emailSender = new EmailSender();
        ReflectionTestUtils.setField(emailSender,"smtpHost", "localhost");
        ReflectionTestUtils.setField(emailSender,"smtpPort", "8025");
        ReflectionTestUtils.setField(emailSender,"smtpUseTLS", false);
    }

    @Test
    public void createMail() throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setHtml(false);
        msg.setBody(getLongMsgBody());
        String addr = "test@test.test";
        String hash = "123";
        MimeMessage mail = emailSender.createMail(msg, addr, hash, Optional.absent());
        DataHandler dataHandler = mail.getDataHandler();
        MimeMultipart mimepart =(MimeMultipart)  dataHandler.getContent();
        BodyPart bodyPart = mimepart.getBodyPart(0);
        String ct = (String) bodyPart.getContent();
        String[] split = ct.split(msg.isHtml() ? "\n" : "\r\n");
        java.util.Optional<String> max = Arrays.stream(split).max(Comparator.comparingInt(String::length));
        int length = max.get().length();
        assertTrue(  String.format("Length %d was greater than 300", length), length <= 300);
    }

    @Test
    public void createMailNullContent() throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setHtml(false);
        msg.setBody(null);
        String addr = "test@test.test";
        String hash = "123";
        MimeMessage mail = emailSender.createMail(msg, addr, hash, Optional.absent());
        DataHandler dataHandler = mail.getDataHandler();
        MimeMultipart mimepart =(MimeMultipart)  dataHandler.getContent();
        BodyPart bodyPart = mimepart.getBodyPart(0);
        String ct = (String) bodyPart.getContent();
        String[] split = ct.split(msg.isHtml() ? "\n" : "\r\n");
        java.util.Optional<String> max = Arrays.stream(split).max(Comparator.comparingInt(String::length));
        int length = max.get().length();
        assertTrue(  String.format("Length %d was greater than 300", length), length <= 300);
    }

    @Test
    public void createMailEmptyContent() throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setHtml(false);
        msg.setBody("");
        String addr = "test@test.test";
        String hash = "123";
        MimeMessage mail = emailSender.createMail(msg, addr, hash, Optional.absent());
        DataHandler dataHandler = mail.getDataHandler();
        MimeMultipart mimepart =(MimeMultipart)  dataHandler.getContent();
        BodyPart bodyPart = mimepart.getBodyPart(0);
        String ct = (String) bodyPart.getContent();
        String[] split = ct.split(msg.isHtml() ? "\n" : "\r\n");
        java.util.Optional<String> max = Arrays.stream(split).max(Comparator.comparingInt(String::length));
        int length = max.get().length();
        assertTrue(  String.format("Length %d was greater than 300", length), length <= 300);
    }

    @Test
    public void createMailWithHumongousLink() throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setHtml(true);
        msg.setBody(getLongMsgBodyWithHumongousLink());
        String addr = "test@test.test";
        String hash = "123";
        MimeMessage mail = emailSender.createMail(msg, addr, hash, Optional.absent());
        DataHandler dataHandler = mail.getDataHandler();
        MimeMultipart mimepart =(MimeMultipart)  dataHandler.getContent();
        BodyPart bodyPart = mimepart.getBodyPart(0);
        String ct = (String) bodyPart.getContent();
        String[] split = ct.split(msg.isHtml() ? "\n" : "\r\n");
        java.util.Optional<String> max = Arrays.stream(split).max(Comparator.comparingInt(String::length));
        int length = max.get().length();
        int maxlength = (msg.isHtml() ? 990 : 991);
        assertTrue(  String.format("Length %d was greater than %d", length, maxlength), length <= maxlength);
    }

    @Test
    public void createMailWithHumongousLinkNonHtml() throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setHtml(false);
        msg.setBody(getLongMsgBodyWithHumongousLink());
        String addr = "test@test.test";
        String hash = "123";
        MimeMessage mail = emailSender.createMail(msg, addr, hash, Optional.absent());
        DataHandler dataHandler = mail.getDataHandler();
        MimeMultipart mimepart =(MimeMultipart)  dataHandler.getContent();
        BodyPart bodyPart = mimepart.getBodyPart(0);
        String ct = (String) bodyPart.getContent();
        String[] split = ct.split(msg.isHtml() ? "\n" : "\r\n");
        java.util.Optional<String> max = Arrays.stream(split).max(Comparator.comparingInt(String::length));
        int length = max.get().length();
        int maxlength = (msg.isHtml() ? 990 : 991);
        assertTrue(  String.format("Length %d was greater than %d", length, maxlength), length <= maxlength);
    }


    private String getLongMsgBody() {
        return "<html> <head></head> <body>          <div>    <p> Ans&ouml;kan: H&ouml;gskolornas gemensamma ans&ouml;kan v&aring;ren 2018 <br /> S&ouml;kande: Ellinor Kristofer Pesonen-perftest <br /> Ans&ouml;kningsnummer: 00108731721 <br /> Datum och lagringstidpunkt f&ouml;r ans&ouml;kan: 2018-feb-06 14:32 </p>    <p> Din ans&ouml;kan har mottagits. Om du vill &auml;ndra dina ans&ouml;knings&ouml;nskem&aring;l, kan du under ans&ouml;kningstiden g&ouml;ra &auml;ndringar via f&ouml;ljande l&auml;nk <a href=\"https://untuvastudieinfo.fi/omatsivut/hakutoiveidenMuokkaus.html#/token/45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa\">https://untuvastudieinfo.fi/omatsivut/hakutoiveidenMuokkaus.html#/token/45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa</a> L&auml;nken &auml;r personlig. </p>    <p> Om du har n&auml;tbankskoder, mobilcertifikat eller ett elektroniskt ID-kort, kan du alternativt logga in i Studieinfo.fi och under ans&ouml;kningstiden g&ouml;ra &auml;ndringarna i tj&auml;nsten Min Studieinfo. I tj&auml;nsten kan du f&ouml;rutom att &auml;ndra ans&ouml;knings&ouml;nskem&aring;l ocks&aring; bearbeta svaren p&aring; till&auml;ggsfr&aring;gor i anknytning till ans&ouml;knings&ouml;nskem&aring;len, uppdatera dina kontaktuppgifter, se antagningsresultaten och ta emot studieplatsen. Vid behov kan du &auml;ven printa ut din ans&ouml;kan i Min Studieinfo -tj&auml;nsten. </p>    <h4>Dina ans&ouml;kningsm&aring;l</h4>    <p> 1. Aalto-universitetet, Handelsh&ouml;gskola Avoimen yliopiston opintojen perusteella hakevien valinta, kauppatieteet, kauppatieteiden kandidaatti ja kauppatieteiden maisteri (3 v + 2 v) <br /> 2. Aalto-universitetet, H&ouml;gskolan f&ouml;r konst design och arkitektur Design, konstkandidat och - magister (3 &aring;r + 2 &aring;r) <br /> 3. Helsingfors universitet, Bio- och milj&ouml;vetenskapliga fakulteten Huvudans&ouml;kan, kandidatprogrammet i milj&ouml;vetenskaper, kandidat i naturvetenskaper och filosofie magister (3 &aring;r + 2 &aring;r) <br /> </p>    <h4>Bilagor</h4>    <table style=\"border-spacing: 0; border-collapse: collapse; text-align: left;\">     <thead>      <tr style=\"border-bottom: 1px solid #e6e6e6;\">       <th style=\"text-align:left; padding-bottom: 6px;\">Bilaga</th>       <th style=\"text-align:left; padding-bottom: 6px; width: 250px;\">Leveransadress</th>       <th style=\"text-align:left; padding-bottom: 6px; width: 250px;\">Ska l&auml;mnas in senast</th>      </tr>     </thead>     <tbody style=\"vertical-align: top;\">      <tr style=\"border-bottom: 1px solid #e6e6e6;\">       <td style=\"padding: 6px 10px 6px 0;\"> Betygskopia av ditt studentexamensbetyg<br /> <br /> Aalto-universitetet, H&ouml;gskolan f&ouml;r konst design och arkitektur </td>       <td style=\"padding: 6px 10px 6px 0;\"> Aalto-universitetets ans&ouml;kningsservice<br /> PB 11110<br /> 00076<br /> AALTO<br /> </td>       <td style=\"padding: 6px 0;\"> 7.2.2018 kl 15:00. Om du blir f&auml;rdig under ans&ouml;kningsperioden, kontrollera inl&auml;mningsdatum fr&aring;n h&ouml;gskolan du s&ouml;ker till. </td>      </tr>      <tr style=\"border-bottom: 1px solid #e6e6e6;\">       <td style=\"padding: 6px 10px 6px 0;\"> Betygskopia av ditt studentexamensbetyg<br /> <br /> Helsingfors universitet, Bio- och milj&ouml;vetenskapliga fakulteten </td>       <td style=\"padding: 6px 10px 6px 0;\"> Helsingfors universitets ans&ouml;kningsservice<br /> PB 3<br /> 00014<br /> HELSINGIN YLIOPISTO<br /> </td>       <td style=\"padding: 6px 0;\"> 7.2.2018 kl 15:00. Om du blir f&auml;rdig under ans&ouml;kningsperioden, kontrollera inl&auml;mningsdatum fr&aring;n h&ouml;gskolan du s&ouml;ker till. </td>      </tr>      <tr style=\"border-bottom: 1px solid #e6e6e6;\">       <td style=\"padding: 6px 10px 6px 0;\"> F&ouml;rhandsuppgifter<br /> <br /> <p><span>Antalet f&ouml;rhandsuppgifter varierar beroende p&aring; ans&ouml;kningsalternativ fr&aring;n en till tre. Alla som ans&ouml;ker till ans&ouml;kningsalternativ som leder till konstkandidatexamen ska utf&ouml;ra alla ifr&aring;gavarande f&ouml;rhandsuppgifter som n&auml;mns i beskrivningen av ans&ouml;kningsalternativet i fr&aring;ga. F&ouml;rhandsuppgifter kan g&ouml;ras p&aring; finska eller svenska. <br /></span></p> <p><a href=\"http://www.aalto.fi/sv/studies/admissions/arts/bachelors_degree/#preliminary_assignments\"><span>F&ouml;rhandsuppgifter p&aring; universitetets websidan</span></a><span>.</span></p> <p><span>&nbsp;</span></p> </td>       <td style=\"padding: 6px 10px 6px 0;\"> Aalto-universitetets ans&ouml;kningsservice<br /> PB 11110<br /> 00076<br /> AALTO<br /> </td>       <td style=\"padding: 6px 0;\"> 2018-mar-28 15:00 </td>      </tr>     </tbody>    </table>    <p>Ge feedback om Studieinfo genom att svara p&aring; <a href=\"https://link.webropolsurveys.com/S/D8F69F6A52638FF9\">fr&aring;geformul&auml;ret</a>.</p>   <p> </p>   <p>Svara inte p&aring; detta meddelande, det har skickats automatiskt.</p>   </div>    </body></html>";
    }

    private String getLongMsgBodyWithHumongousLink() {
        return "<html> <head></head> <body>          <div>    <p> Ans&ouml;kan: H&ouml;gskolornas gemensamma ans&ouml;kan v&aring;ren 2018 <br /> S&ouml;kande: Ellinor Kristofer Pesonen-perftest <br /> Ans&ouml;kningsnummer: 00108731721 <br /> Datum och lagringstidpunkt f&ouml;r ans&ouml;kan: 2018-feb-06 14:32 </p>    <p> Din ans&ouml;kan har mottagits. Om du vill &auml;ndra dina ans&ouml;knings&ouml;nskem&aring;l, kan du under ans&ouml;kningstiden g&ouml;ra &auml;ndringar via f&ouml;ljande l&auml;nk <a href=\"https://untuvastudieinfo.fi/omatsivut/hakutoiveidenMuokkaus.html#/token/45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa\">https://untuvastudieinfo.fi/omatsivut/hakutoiveidenMuokkaus.html#/token/45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa45aadc15fa23922b4b69c16f65689113446000deb5608ec447487c2a5d7666aa</a> L&auml;nken &auml;r personlig. </p>    <p> Om du har n&auml;tbankskoder, mobilcertifikat eller ett elektroniskt ID-kort, kan du alternativt logga in i Studieinfo.fi och under ans&ouml;kningstiden g&ouml;ra &auml;ndringarna i tj&auml;nsten Min Studieinfo. I tj&auml;nsten kan du f&ouml;rutom att &auml;ndra ans&ouml;knings&ouml;nskem&aring;l ocks&aring; bearbeta svaren p&aring; till&auml;ggsfr&aring;gor i anknytning till ans&ouml;knings&ouml;nskem&aring;len, uppdatera dina kontaktuppgifter, se antagningsresultaten och ta emot studieplatsen. Vid behov kan du &auml;ven printa ut din ans&ouml;kan i Min Studieinfo -tj&auml;nsten. </p>    <h4>Dina ans&ouml;kningsm&aring;l</h4>    <p> 1. Aalto-universitetet, Handelsh&ouml;gskola Avoimen yliopiston opintojen perusteella hakevien valinta, kauppatieteet, kauppatieteiden kandidaatti ja kauppatieteiden maisteri (3 v + 2 v) <br /> 2. Aalto-universitetet, H&ouml;gskolan f&ouml;r konst design och arkitektur Design, konstkandidat och - magister (3 &aring;r + 2 &aring;r) <br /> 3. Helsingfors universitet, Bio- och milj&ouml;vetenskapliga fakulteten Huvudans&ouml;kan, kandidatprogrammet i milj&ouml;vetenskaper, kandidat i naturvetenskaper och filosofie magister (3 &aring;r + 2 &aring;r) <br /> </p>    <h4>Bilagor</h4>    <table style=\"border-spacing: 0; border-collapse: collapse; text-align: left;\">     <thead>      <tr style=\"border-bottom: 1px solid #e6e6e6;\">       <th style=\"text-align:left; padding-bottom: 6px;\">Bilaga</th>       <th style=\"text-align:left; padding-bottom: 6px; width: 250px;\">Leveransadress</th>       <th style=\"text-align:left; padding-bottom: 6px; width: 250px;\">Ska l&auml;mnas in senast</th>      </tr>     </thead>     <tbody style=\"vertical-align: top;\">      <tr style=\"border-bottom: 1px solid #e6e6e6;\">       <td style=\"padding: 6px 10px 6px 0;\"> Betygskopia av ditt studentexamensbetyg<br /> <br /> Aalto-universitetet, H&ouml;gskolan f&ouml;r konst design och arkitektur </td>       <td style=\"padding: 6px 10px 6px 0;\"> Aalto-universitetets ans&ouml;kningsservice<br /> PB 11110<br /> 00076<br /> AALTO<br /> </td>       <td style=\"padding: 6px 0;\"> 7.2.2018 kl 15:00. Om du blir f&auml;rdig under ans&ouml;kningsperioden, kontrollera inl&auml;mningsdatum fr&aring;n h&ouml;gskolan du s&ouml;ker till. </td>      </tr>      <tr style=\"border-bottom: 1px solid #e6e6e6;\">       <td style=\"padding: 6px 10px 6px 0;\"> Betygskopia av ditt studentexamensbetyg<br /> <br /> Helsingfors universitet, Bio- och milj&ouml;vetenskapliga fakulteten </td>       <td style=\"padding: 6px 10px 6px 0;\"> Helsingfors universitets ans&ouml;kningsservice<br /> PB 3<br /> 00014<br /> HELSINGIN YLIOPISTO<br /> </td>       <td style=\"padding: 6px 0;\"> 7.2.2018 kl 15:00. Om du blir f&auml;rdig under ans&ouml;kningsperioden, kontrollera inl&auml;mningsdatum fr&aring;n h&ouml;gskolan du s&ouml;ker till. </td>      </tr>      <tr style=\"border-bottom: 1px solid #e6e6e6;\">       <td style=\"padding: 6px 10px 6px 0;\"> F&ouml;rhandsuppgifter<br /> <br /> <p><span>Antalet f&ouml;rhandsuppgifter varierar beroende p&aring; ans&ouml;kningsalternativ fr&aring;n en till tre. Alla som ans&ouml;ker till ans&ouml;kningsalternativ som leder till konstkandidatexamen ska utf&ouml;ra alla ifr&aring;gavarande f&ouml;rhandsuppgifter som n&auml;mns i beskrivningen av ans&ouml;kningsalternativet i fr&aring;ga. F&ouml;rhandsuppgifter kan g&ouml;ras p&aring; finska eller svenska. <br /></span></p> <p><a href=\"http://www.aalto.fi/sv/studies/admissions/arts/bachelors_degree/#preliminary_assignments\"><span>F&ouml;rhandsuppgifter p&aring; universitetets websidan</span></a><span>.</span></p> <p><span>&nbsp;</span></p> </td>       <td style=\"padding: 6px 10px 6px 0;\"> Aalto-universitetets ans&ouml;kningsservice<br /> PB 11110<br /> 00076<br /> AALTO<br /> </td>       <td style=\"padding: 6px 0;\"> 2018-mar-28 15:00 </td>      </tr>     </tbody>    </table>    <p>Ge feedback om Studieinfo genom att svara p&aring; <a href=\"https://link.webropolsurveys.com/S/D8F69F6A52638FF9\">fr&aring;geformul&auml;ret</a>.</p>   <p> </p>   <p>Svara inte p&aring; detta meddelande, det har skickats automatiskt.</p>   </div>    </body></html>";
    }
}