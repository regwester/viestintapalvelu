package fi.vm.sade.ryhmasahkoposti.raportointi.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RaportoitavaLiiteService;

@Service
public class RaportoitavaLiiteServiceImpl implements RaportoitavaLiiteService {

	@Override
	public List<RaportoitavaLiite> muodostaRaportoitavatLiitteet(LahetyksenAloitusDTO lahetyksenAloitus) throws IOException {
		List<RaportoitavaLiite> raportoitavatLiitteet = new ArrayList<RaportoitavaLiite>();
			
		if (lahetyksenAloitus.getLahetetynviestinliitteet() == null || 
			lahetyksenAloitus.getLahetetynviestinliitteet().isEmpty()) {
			return raportoitavatLiitteet;
		}
		
		Iterator<LahetettyLiiteDTO> liiteIterator = lahetyksenAloitus.getLahetetynviestinliitteet().iterator();
		
		while (liiteIterator.hasNext()) {
			LahetettyLiiteDTO liite = liiteIterator.next();
			
			RaportoitavaLiite raportoitavaLiite = new RaportoitavaLiite();
			raportoitavaLiite.setLiitetiedostonNimi(liite.getLiitetiedostonNimi());
			
			byte[] zippedLiitetiedosto = zipLiitetiedosto(liite.getLiitetiedostonNimi(), liite.getLiitetiedosto());
			raportoitavaLiite.setLiitetiedosto(zippedLiitetiedosto);
			
			raportoitavatLiitteet.add(raportoitavaLiite);
		}
			
		return raportoitavatLiitteet;
	}
	
    private byte[] zipLiitetiedosto(String liitetiedostonNimi, byte[] liitetiedosto) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipStream = new ZipOutputStream(out);
        
        zipStream.putNextEntry(new ZipEntry(liitetiedostonNimi));
        zipStream.write(liitetiedosto);
        zipStream.closeEntry();

        zipStream.close();
        return out.toByteArray();
    }

}
