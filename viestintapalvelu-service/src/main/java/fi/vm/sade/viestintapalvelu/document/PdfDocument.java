package fi.vm.sade.viestintapalvelu.document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;

public class PdfDocument {
    private AddressLabel addressLabel;
    private byte[] frontPage;
    private byte[] attachment;
    private ArrayList<byte[]> contents = new ArrayList<byte[]>();
    
    public PdfDocument(AddressLabel addressLabel, byte[] ... contents) {
        this(addressLabel);
        for (byte[] c : contents) {
            this.contents.add(c);
        }
    }
    
    public PdfDocument(AddressLabel addressLabel) {
      this(addressLabel, null, null);
    }
    
    public PdfDocument(AddressLabel addressLabel, byte[] frontPage,
                       byte[] attachment) {
        this.addressLabel = addressLabel;
        this.frontPage = frontPage;
        this.attachment = attachment;
    }
    
    public AddressLabel getAddressLabel() {
        return addressLabel;
    }

    public int getContentSize() {
        return (contents != null) ? contents.size() : -1 ;
    }
    
    public InputStream getContentStream(int index) {
        return new ByteArrayInputStream(contents.get(index));
    }
    
    public void addContent(byte[] content) {
        contents.add(content);
    }
    
    public InputStream getFrontPage() {
        if (frontPage != null) {
            return new ByteArrayInputStream(frontPage);
        }
        return null;
    }

    public InputStream getAttachment() {
    	// There are no attachments in e.g. koekutsukirje
    	if (attachment != null) {
    		return new ByteArrayInputStream(attachment);
    	}
    	return null;
    	
    }
}
