package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.*;

import fi.vm.sade.generic.model.BaseEntity;

/*
 * CREATE TABLE kirjeet.sisalto
 (
 id bigint NOT NULL,
 kirjepohja_id bigint,
 nimi character varying(255),
 sisalto character varying(5000),
 aikaleima timestamp without time zone,
 oid_tallentaja character varying(255),
 CONSTRAINT sisalto_kirjepohja_id_fkey FOREIGN KEY (kirjepohja_id)
 REFERENCES kirjeet.kirjepohja (id) MATCH SIMPLE
 ON UPDATE NO ACTION ON DELETE NO ACTION
 )
 */

@Table(name = "sisalto", schema= "kirjeet")
@Entity()
public class TemplateContent extends BaseEntity implements
        Comparable<TemplateContent> {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kirjepohja_id")
    private Template template;

    @Column(name = "jarjestys")
    private int order;
    
    @Column(name = "nimi")
    private String name;
    
    @Column(name = "sisalto")
    private String content;

    @Column(name = "tyyppi")
    private String contentType;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}    
	
	@Override
	public String toString() {
		return "TemplateContent [template=" + template + ", order=" + order
				+ ", name=" + name + ", content=" + content + ", contentType="
				+ contentType + ", timestamp=" + timestamp + "]";
	}

	@Override
    public int compareTo(TemplateContent o) {
        Integer ord = new Integer(order);
        return ord.compareTo(o.order);
    }
}
