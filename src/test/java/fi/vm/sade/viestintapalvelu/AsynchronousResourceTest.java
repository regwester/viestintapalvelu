package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.jretrofit.Retrofit;
import org.junit.Test;

import fi.vm.sade.viestintapalvelu.download.DownloadResource;

public class AsynchronousResourceTest {
	private AsynchronousResource resource = new AsynchronousResource();

	@Test
	public void urlTo() {
		@SuppressWarnings("unused")
		Object target = new Object() {
			public String getContextPath() {
				return "/viestintapalvelu/";
			}

			public StringBuffer getRequestURL() {
				StringBuffer buffer = new StringBuffer();
				buffer.append("https://itest-virkailija.oph.ware.fi/viestintapalvelu/api/v1/download/1a6c5852-52c3-46d3-a649-861c8e887300");
				return buffer;
			}

			public String getServletPath() {
				return "/api/v1/";
			}
		};
		HttpServletRequest request = Retrofit.partial(target,
				HttpServletRequest.class);
		assertEquals(
				"https://itest-virkailija.oph.ware.fi/viestintapalvelu/api/v1/download",
				resource.urlTo(request, DownloadResource.class));
	}
}
