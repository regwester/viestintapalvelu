package fi.vm.sade.ajastuspalvelu.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Table(name = "tehtava")
@Entity(name = "Task")
public class Task {
    
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @SequenceGenerator(name = "tehtava_id_seq", sequenceName = "tehtava_id_seq")
    @GeneratedValue(generator = "tehtava_id_seq")
    private Long id;
    
    @Version
    @Column(name = "versio", nullable = false)
    private Long versio;
    
    @Column(name = "nimi", nullable = false)
    private String name;
    
    @Column(name = "bean_nimi", nullable = false)
    private String beanName;
    
    @Column(name = "kirjepohja_nimi")
    private String templateName;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getVersio() {
        return versio;
    }
    
    public void setVersio(Long versio) {
        this.versio = versio;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getBeanName() {
        return beanName;
    }
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
