package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.RyhmasahkopostiSelailuResource;
import fi.vm.sade.ryhmasahkoposti.service.RyhmasahkopostinRaportointiService;

@Component
public class RyhmasahkopostiSelailuResourceImpl implements RyhmasahkopostiSelailuResource {
	private RyhmasahkopostinRaportointiService ryhmasahkopostinRaportointiService;

	@Autowired
	public RyhmasahkopostiSelailuResourceImpl(RyhmasahkopostinRaportointiService ryhmasahkopostinRaportointiService) {
		this.ryhmasahkopostinRaportointiService = ryhmasahkopostinRaportointiService;
	}

	@Override
	public List<RaportoitavaViestiDTO> getRaportoitavatViestit() {
		System.out.println("getRaportoitavatViestit()");
		return null;
	}

	@Override
	public List<RaportoitavaViestiDTO> getRaportoitavatViestit(String hakuKentta) {
		return ryhmasahkopostinRaportointiService.haeRaportoitavatViestit(hakuKentta);
	}
	

}