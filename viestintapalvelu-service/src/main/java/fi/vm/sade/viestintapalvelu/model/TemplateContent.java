package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wordnik.swagger.annotations.ApiModelProperty;

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

@Table(name = "sisalto", schema="kirjeet")
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

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "oid_tallentaja", nullable = true)
    private String storingOid;

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

    public String getStoringOid() {
        return storingOid;
    }

    public void setStoringOid(String storingOid) {
        this.storingOid = storingOid;
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

    @Override
    public String toString() {
        return "TemplateContent [order=" + order + ", name=" + name
                + ", content=" + content + ", contentType=]";
    }

    @Override
    public int compareTo(TemplateContent o) {
        Integer ord = new Integer(order);
        return ord.compareTo(o.order);
    }
}
