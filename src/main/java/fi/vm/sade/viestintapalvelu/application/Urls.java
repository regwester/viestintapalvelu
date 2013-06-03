package fi.vm.sade.viestintapalvelu.application;

import java.util.ArrayList;
import java.util.List;

public class Urls {
	public static final String API_PATH = "api/v1";
	public static final String ADDRESS_LABEL_RESOURCE_PATH = "addresslabel";
	public static final String DOWNLOAD_RESOURCE_PATH = "download";
	public static final String HYVAKSYMISKIRJE_RESOURCE_PATH = "hyvaksymiskirje";
	public static final String JALKIOHJAUSKIRJE_RESOURCE_PATH = "jalkiohjauskirje";

	public static String build(String... parts) {
		UrlBuilder builder = new UrlBuilder();
		for (String part : parts) {
			builder.add(part);
		}
		return builder.toString();
	}

	private static class UrlBuilder {
		private final List<String> parts = new ArrayList<String>();

		public void add(String part) {
			this.parts.add(part);
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < parts.size(); i++) {
				builder.append(parts.get(i));
				if (i != (parts.size() - 1)) {
					builder.append("/");
				}
			}
			return builder.toString();
		}
	}
}
