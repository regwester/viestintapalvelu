package fi.vm.sade.viestintapalvelu;

import java.io.InputStream;

import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;

public class OPHUserAgent extends ITextUserAgent {
	
	private static final String classpathScheme = "classpath";
	private static final String schemeSeparator = ":";
	
	
    public OPHUserAgent(ITextOutputDevice outputDevice) {
        super(outputDevice);
    }

    @Override
    protected InputStream resolveAndOpenStream(String uri) {
        if (isClasspathResource(uri)) {
        	return getClass().getResourceAsStream(toPlainClasspathResource(uri));
        } else {
            return super.resolveAndOpenStream(uri);
        }
    }

	@Override
    public String resolveURI(String uri) {
    	if (isClasspathResource(uri)) {
    		return uri;
    	}
    	return super.resolveURI(uri);
    }
    
    private boolean isClasspathResource(String uri) {
    	return uri != null && uri.startsWith(classpathScheme + schemeSeparator);
    }
    
    private String toPlainClasspathResource(String uri) {
		return uri.replace(classpathScheme + schemeSeparator, "");
	}
}