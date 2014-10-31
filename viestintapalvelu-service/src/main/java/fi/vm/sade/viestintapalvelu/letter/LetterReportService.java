package fi.vm.sade.viestintapalvelu.letter;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import fi.vm.sade.viestintapalvelu.dto.OrganizationDTO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchReportDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchesReportDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterReceiverLetterDTO;
import fi.vm.sade.viestintapalvelu.dto.query.LetterReportQueryDTO;

/**
 * Palvelukerroksen rajapinta kirjelähetysten raportoinnin liiketoimintalogiikkaa varten
 * 
 * @author vehei1
 *
 */
public interface LetterReportService {
    /**
     * Hakee halutun kirjelähetyksen raporttitiedot
     *  
     * @param id Kirjelähetyksen avain
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @return Kirjelähetyksen tiedot
     */
    LetterBatchReportDTO getLetterBatchReport(Long id, PagingAndSortingDTO pagingAndSorting);
    
    /**
     * Hakee käyttäjän organisaation raportoitavat kirjelähetykset lajiteltuna ja sivutettuna. Jos organizationOID on yhtä
     * kuin rekisterinpitäjän OID, haetaan kaikki kirjelähetykset organisaatiosta riippumatta.
     *  
     * @param organizationOID Organisaation OID
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return Näytettävät kirjelähetyksen raporttitiedot
     */
    LetterBatchesReportDTO getLetterBatchesReport(String organizationOID, PagingAndSortingDTO pagingAndSorting);
    
    /**
     * Hakee hakuparametrien mukaiset raportoitavat kirjelähetykset lajiteltuna ja sivutettuna
     * 
     * @param query Hakuparametrit
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return Näytettävät kirjelähetyksen raporttitiedot
     */
    LetterBatchesReportDTO getLetterBatchesReport(LetterReportQueryDTO query, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee vastaanottajan kirjeen sisällön 
     * 
     * @param id Vastaanottajan kirjeen sisällön ID
     * @return Vastaanottajan kirjeen tiedot
     * @throws DataFormatException 
     * @throws IOException 
     */
    LetterReceiverLetterDTO getLetterReceiverLetter(Long id) throws IOException, DataFormatException;

    /**
     * Hakee käyttäjän organisaatiotiedot
     * 
     * @return Lista organisaation tietoja
     */
    List<OrganizationDTO> getUserOrganizations();
}
