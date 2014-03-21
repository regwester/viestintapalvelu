package fi.vm.sade.viestintapalvelu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

@Table(name="sisalto")
@Entity()
public class TemplateContent extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(name="jarjestys")
	private int order;
	@Column(name="nimi")
	private String name;
	@Column(name="sisalto")
	private String content;
	
	
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
		return "TemplateContent [order=" + order + ", name="
				+ name + ", content=" + content + ", contentType=]";
	}

}
