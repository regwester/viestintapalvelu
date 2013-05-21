package fi.vm.sade.viestintapalvelu;

public class Urls {
	public static final String API_PATH = "/api/v1";
	public static final String ADDRESS_LABEL_RESOURCE_PATH = "addresslabel";
	public static final String DOWNLOAD_RESOURCE_PATH = "download";
	public static final String HYVAKSYMISKIRJE_RESOURCE_PATH = "hyvaksymiskirje";
	public static final String JALKIOHJAUSKIRJE_RESOURCE_PATH = "jalkiohjauskirje";

	public static RestServer localhost() {
		return new Localhost();
	}

	public static interface RestServer {
		String addresslabelDownload();
	}

	public static class Localhost implements RestServer {
		private static final String SCHEME = "http";
		private static final String DOMAIN = "localhost";
		private static final int PORT = Launcher.DEFAULT_PORT;

		@Override
		public String addresslabelDownload() {
			return apiRootUrl() + "/" + ADDRESS_LABEL_RESOURCE_PATH + "/"
					+ DOWNLOAD_RESOURCE_PATH;
		}

		public String apiRootUrl() {
			return SCHEME + "://" + DOMAIN + ":" + PORT + Urls.API_PATH;
		}
	}
}
