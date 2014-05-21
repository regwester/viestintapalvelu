/**
 * 
 */
package fi.vm.sade.viestintapalvelu.letter;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author migar1
 *
 */
public class LetterContent {

    @ApiModelProperty(value = "Lähetetyn kirjeen sisältö")
    private byte[] content;
    
    @ApiModelProperty(value = "Lähetetyn kirjeen tyyppi (application/pdf, application/zip)")
    private String contentType = "";
    
	@ApiModelProperty(value = "Aikaleima")
    private Date timestamp;    
    
    
	public LetterContent() {}

	public LetterContent(byte[] content, String contentType, Date timestamp) {
		super();
		this.content = content;
		this.contentType = contentType;
		this.timestamp = timestamp;
	}
	
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
    
    
}
