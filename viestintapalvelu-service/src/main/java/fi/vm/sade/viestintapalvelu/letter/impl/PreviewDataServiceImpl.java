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
 */

package fi.vm.sade.viestintapalvelu.letter.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.letter.Letter;
import fi.vm.sade.viestintapalvelu.letter.LetterBuilder;
import fi.vm.sade.viestintapalvelu.letter.LetterEmailService;
import fi.vm.sade.viestintapalvelu.letter.PreviewDataService;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.template.Replacement;
import fi.vm.sade.viestintapalvelu.template.Template;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class PreviewDataServiceImpl implements PreviewDataService {

    public static final Logger log = LoggerFactory.getLogger(PreviewDataServiceImpl.class);

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private LetterBuilder letterBuilder;
    @Autowired
    private LetterEmailService letterEmailService;

    @Override
    public Letter getLetter(Template template) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            final List<String> lastnames = mapper.readValue(resourceLoader.getResource("/generator/lastnames.json").getFile(), List.class);
            final List<List<String>> countries = mapper.readValue(resourceLoader.getResource("/generator/countries.json").getFile(), List.class);
            final List<String> firstnames = mapper.readValue(resourceLoader.getResource("/generator/firstnames.json").getFile(), List.class);
            final List<String> hakutoive = mapper.readValue(resourceLoader.getResource("/generator/hakutoive.json").getFile(), List.class);
            final List<String> koulut = mapper.readValue(resourceLoader.getResource("/generator/koulut.json").getFile(), List.class);
            final List<String> language = mapper.readValue(resourceLoader.getResource("/generator/language.json").getFile(), List.class);
            final List<String> postoffices = mapper.readValue(resourceLoader.getResource("/generator/postoffices.json").getFile(), List.class);
            final List<String> streets = mapper.readValue(resourceLoader.getResource("/generator/streets.json").getFile(), List.class);

            Random rand = new Random();
            String firstname = firstnames.get(rand.nextInt(firstnames.size()));
            String lastname = lastnames.get(rand.nextInt(lastnames.size()));
            String addressline = streets.get(rand.nextInt(streets.size())) + " " + rand.nextInt(100);
            String addressline2 = postoffices.get(rand.nextInt(postoffices.size()));
            List<String> cou = countries.get(rand.nextInt(countries.size()));
            String addressline3 = cou.get(0);
            String postalcode = addressline2.split(" ")[0];
            String city = addressline2.split(" ")[1];
            String region = "Dummy region";
            String country = addressline3;
            String countrycode = cou.get(1);
            AddressLabel addresslabel = new AddressLabel(firstname, lastname, addressline, null, null, postalcode, city, region, country,
                    countrycode);

            Letter letter = new Letter(addresslabel, null, null, getTemplateReplacements(template), "email@foo.bar");
            return letter;
            // letter.setAddressLabel(addresslabel);
        } catch (Exception e) {
            log.error("Error reading preview data json", e);
        }

        return new Letter();

    }

    @Override
    public LetterBatch getLetterBatch(int numLetters, Template template, LetterReceivers letterReceivers, String applicationPeriod) {

        List<Letter> letters = new ArrayList<>();
        for (int i = 0; i < numLetters; i++) {
            letters.add(getLetter(template));
        }
        LetterBatch batch = new LetterBatch();
        Set<LetterReceivers> receiverses = new HashSet<>();
        receiverses.add(letterReceivers);
        batch.setLetterReceivers(receiverses);
        batch.setTemplateId(template.getId());
        batch.setLanguage(template.getLanguage());
        batch.setApplicationPeriod(applicationPeriod);
        batch.setTemplateName(template.getName());
        batch.setTemplateId(template.getId());
        batch.setId(-1l);
        LetterReplacement letterReplacement = getLetterReplacement(template);
        Set<LetterReplacement> letterReplacements = new HashSet<>();
        letterReplacements.add(letterReplacement);
        batch.setLetterReplacements(letterReplacements);

        batch.setTag("tag");
        return batch;
    }

    private LetterReplacement getLetterReplacement(Template template) {
        LetterReplacement replacement = new LetterReplacement();
        replacement.setName("koulu");
        replacement.setMandatory(true);
        replacement.setJsonValue("LetterReplacement koulu json");
        return replacement;
    }

    @Override
    public LetterReceivers getLetterReceivers(Template template, String applicationPeriod) {
        LetterReceivers receivers = new LetterReceivers();
        LetterBatch batch = getLetterBatch(1, template, receivers, applicationPeriod);
        receivers.setLetterBatch(batch);
        receivers.setTimestamp(DateTime.now().toDate());
        LetterReceiverLetter letter = getLetterReceiverLetter();
        receivers.setLetterReceiverLetter(letter);
        letter.setLetterReceivers(receivers);
        final Set<LetterReceiverReplacement> letterReceiverReplacements = getLetterReceiverReplacements(receivers);
        receivers.setLetterReceiverReplacement(letterReceiverReplacements);
        receivers.setLetterReceiverAddress(getLetterReceiverAddress());
        receivers.setEmailAddress("foo@bar.com");
        return receivers;
    }

    private LetterReceiverAddress getLetterReceiverAddress() {
        LetterReceiverAddress address = new LetterReceiverAddress();
        ObjectMapper mapper = new ObjectMapper();

        try {
            final List<String> lastnames = mapper.readValue(resourceLoader.getResource("/generator/lastnames.json").getFile(), List.class);
            final List<List<String>> countries = mapper.readValue(resourceLoader.getResource("/generator/countries.json").getFile(), List.class);
            final List<String> firstnames = mapper.readValue(resourceLoader.getResource("/generator/firstnames.json").getFile(), List.class);
            final List<String> language = mapper.readValue(resourceLoader.getResource("/generator/language.json").getFile(), List.class);
            final List<String> postoffices = mapper.readValue(resourceLoader.getResource("/generator/postoffices.json").getFile(), List.class);
            final List<String> streets = mapper.readValue(resourceLoader.getResource("/generator/streets.json").getFile(), List.class);

            Random rand = new Random();
            String firstname = firstnames.get(rand.nextInt(firstnames.size()));
            String lastname = lastnames.get(rand.nextInt(lastnames.size()));
            String addressline = streets.get(rand.nextInt(streets.size())) + " " + rand.nextInt(100);
            String addressline2 = postoffices.get(rand.nextInt(postoffices.size()));
            List<String> cou = countries.get(rand.nextInt(countries.size()));
            String addressline3 = cou.get(0);
            String postalcode = addressline2.split(" ")[0];
            String city = addressline2.split(" ")[1];
            String region = "Dummy region";
            String country = addressline3;
            String countrycode = cou.get(1);

            address.setFirstName(firstname);
            address.setLastName(lastname);
            address.setAddressline(addressline);
            //address.setAddressline2(addressline2);
            //address.setAddressline3(addressline3);
            address.setPostalCode(postalcode);
            address.setCity(city);
            address.setRegion(region);
            address.setCountry(country);
            address.setCountryCode(countrycode);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    private Set<LetterReceiverReplacement> getLetterReceiverReplacements(LetterReceivers receivers) {
        Set<LetterReceiverReplacement> receiverreplacements = new HashSet<>();
        LetterReceiverReplacement receiverReplacement = new LetterReceiverReplacement();
        receiverReplacement.setTimestamp(new Date());
        receiverReplacement.setLetterReceivers(receivers);
        receiverreplacements.add(receiverReplacement);
        return receiverreplacements;
    }

    private LetterReceiverLetter getLetterReceiverLetter() {
        LetterReceiverLetter letter = new LetterReceiverLetter();
        letter.setId(0l);
        letter.setLetter(getLetterContent());
        letter.setTimestamp(DateTime.now().toDate());
        return letter;
    }

    private byte[] getLetterContent() {
        String test = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head>    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>    <meta http-equiv=\"Content-Style-Type\" content=\"text/css\"/>    <style type=\"text/css\">$tyylit</style>    <title>Hyväksymiskirje</title></head><body><div class='header'>    <div class=\"organisaatio\"></div>    <div class='osoitetarra'>        <div>$osoite</div>    </div></div><div class=\"asiakirjanTyyppi\">    $letterDate</div><div class=\"asiaotsikko\">ILMOITUS PÄÄTÖKSESTÄ OPISKELIJAVALINNAN TULOKSESTA KORKEAKOULUJEN YHTEISHAUSSA SYKSYLLÄ 2014<br/><br/><b>$koulu<br/>$koulutus</b></div><div class=\"sisalto\">    $sisalto    <p><b>Muutoksenhakuoikeus</b></p>    <p>        Mikäli olet tyytymätön opiskelijavalinnan tulokseen ja valintaperusteiden soveltamisessa on mielestäsi        tapahtunut virhe, tutustu ensiksi kyseisen hakukohteen valintaperusteisiin sekä oikaisumenettelyä koskevaan        ohjeeseen. Ota tämän jälkeen tarvittaessa yhteyttä siihen ammattikorkeakouluun tai yliopistoon, jonka        hakukohdetta tyytymättömyytesi koskee. Mikäli asia ei tällöin selviä, voit tehdä kirjallisen oikaisupyynnön        opiskelijavalintaa koskevasta päätöksestä. Oikaisupyynnöstä tulee käydä ilmi yksilöidysti mihin haet oikaisua,        miten päätöstä tulisi mielestäsi oikaista ja millä perustein haet tätä muutosta. Lisäksi oikaisupyynnöstä on        käytävä ilmi oikaisun hakijan henkilö- ja yhteystiedot. Oikaisupyynnön oheen liitetään tarvittavat pyyntöä        tukevat asiakirjat. Ammattikorkeakoulut ja yliopistot eivät käsittele yhteishaussa hyväksytyksi tulleen hakijan        oikaisupyyntöä, jos se koskee hakutoivejärjestyksessä alemmaksi asetettua hakukohdetta.    </p>    <p>        Ammattikorkeakoulun hakukohdetta koskeva oikaisupyyntö tulee osoittaa ko. ammattikorkeakoulun hallitukselle ja        jättää ammattikorkeakouluun 14 vuorokauden kuluessa tämän ilmoituksen tiedoksisaannista.    </p>    <p>        Yliopiston hakukohdetta koskeva oikaisupyyntö tulee osoittaa yliopistolle tai sen valintaperusteissaan        määrittelemälle yliopiston toimielimelle ja jättää yliopistoon 14 vuorokauden kuluessa opiskelijavalinnan        tuloksen julkistamisesta.    </p>    <p><b>Tervetuloa opiskelemaan!</b></p><p> #if($hakijapalveluidenOsoite) $hakijapalveluidenOsoite.organisaationimi $hakijapalveluidenOsoite.addressline $hakijapalveluidenOsoite.postalCode $hakijapalveluidenOsoite.city <br/> #if($hakijapalveluidenOsoite.email) $hakijapalveluidenOsoite.email #end #if($hakijapalveluidenOsoite.numero) $hakijapalveluidenOsoite.numero #end #end</p></div></body></html>";
        return test.getBytes();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    // ensure we don't save anything to database
    public byte[] getPreviewPdf(Template template, String applicationPeriod, String letterContents) throws IOException, DocumentException {

        final LetterReceivers letterReceivers = getLetterReceivers(template, applicationPeriod);
        Map<String, Object> batchreplacements = new HashMap<>();

        Map<String, Object> letterreplacements = getLetterReplacements();
        final List<Object> tulokset = (List<Object>) letterreplacements.get("tulokset");
        Map<String, Object> eka = (Map<String, Object>) tulokset.get(0);
        batchreplacements.putAll(eka);
        batchreplacements.put("koulu", letterreplacements.get("koulu"));
        batchreplacements.put("koulutus", letterreplacements.get("koulutus"));
        batchreplacements.put("henkilotunnus", "123456-7890");
        batchreplacements.put("sisalto", letterContents);
        return letterBuilder.constructPDFForLetterReceiverLetter(letterReceivers, template, batchreplacements, letterreplacements).getLetter();
    }

    private Map<String, Object> getLetterReplacements() throws IOException {
        Random rand = new Random();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> tulokset = new ArrayList<>();
        final List<String> hakutoive = mapper.readValue(resourceLoader.getResource("/generator/hakutoive.json").getFile(), List.class);
        final List<String> koulut = mapper.readValue(resourceLoader.getResource("/generator/koulut.json").getFile(), List.class);
        String koulu = koulut.get(rand.nextInt(koulut.size()));
        String koulutus = hakutoive.get(rand.nextInt(hakutoive.size()));
        for (int i = 0; i < 3; i++) {
            final String oppilaitos;
            final String hakukohde;
            if (i == 0) {
                oppilaitos = koulu;
                hakukohde = koulutus;
            } else {
                oppilaitos = koulut.get(rand.nextInt(koulut.size()));
                hakukohde = hakutoive.get(rand.nextInt(hakutoive.size()));
            }

            // List<Map<String, Object>> haku = new ArrayList<>();
            Map<String, Object> haku = new HashMap<>();
            haku.put("organisaationNimi", "org nimi");
            haku.put("oppilaitoksenNimi", oppilaitos);
            haku.put("hakukohteenNimi", hakukohde);
            int omatpisteet = rand.nextInt(100);
            haku.put("omatPisteet", omatpisteet + "/" + (omatpisteet / 2));
            haku.put("paasyJaSoveltuvuuskoe", rand.nextInt(60) + "");
            haku.put("hyvaksytyt", rand.nextInt(50) + "/" + 100);
            haku.put("valinnanTulos", "Sinut on hylätty");
            haku.put("hylkaysperuste", "Lorem ipsum");
            haku.put("oma", 123);
            haku.put("minimi", 456);

            tulokset.add(haku);
        }

        Map<String, Object> tulos = new HashMap<>();
        List<Map<String, Object>> pisteet = new ArrayList<>();
        tulos.put("pisteet", pisteet);
        tulos.put("sijoitukset", pisteet);
        for (int i = 0; i < 3; i++) {
            Map<String, Object> pisteRow = new HashMap<>();
            pisteRow.put("nimi", "pisteet nimi");
            pisteRow.put("oma", 123);
            pisteRow.put("minimi", 456);
            pisteet.add(pisteRow);
        }

        Map<String, Object> letterreplacements = new HashMap<>();
        letterreplacements.put("tulokset", tulokset);
        letterreplacements.put("tulos", tulos);
        letterreplacements.put("koulu", koulu);
        letterreplacements.put("koulutus", koulutus);
        return letterreplacements;
    }

    @Override
    @Transactional(readOnly = true)
    public String getEmailPreview(Template template, String applicationPeriod, String letterContents) {
        final LetterReceivers letterReceivers = getLetterReceivers(template, applicationPeriod);
        fi.vm.sade.viestintapalvelu.model.LetterBatch batch = getLetterBatch(1, template, letterReceivers, applicationPeriod);

        LetterReplacement sisaltoReplacement = new LetterReplacement();
        sisaltoReplacement.setDefaultValue(letterContents);
        sisaltoReplacement.setName("sisalto");
        batch.getLetterReplacements().add(sisaltoReplacement);
        batch.setBatchStatus(LetterBatch.Status.waiting_for_ipost_processing);
        final String preview = letterEmailService.getPreview(batch, template, Optional.<String> absent());
        return preview;
    }

    private Map<String, Object> getTemplateReplacements(Template template) {
        Map<String, Object> templateReplacements = new HashMap<>();
        final List<Replacement> replacements = template.getReplacements();
        for (Replacement replacement : replacements) {
            templateReplacements.put(replacement.getName(), replacement.getDefaultValue());
        }
        return templateReplacements;
    }
}
