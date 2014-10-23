package fi.vm.sade.ajastuspalvelu.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tehtava")
@Entity(name = "Task")
public class Task extends ScheduleBaseEntity {
    
    @Column(name = "nimi", nullable = false)
    private String name;
    
    @Column(name = "bean_nimi", nullable = false)
    private String beanName;
    
    @Column(name = "kirjepohja_nimi")
    private String templateName;
    
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
