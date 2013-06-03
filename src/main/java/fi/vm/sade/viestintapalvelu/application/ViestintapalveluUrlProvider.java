package fi.vm.sade.viestintapalvelu.application;

public interface ViestintapalveluUrlProvider {
	String root();

	String index();

	String apiRoot();

	String addresslabelDownload();

	String addresslabel();
}