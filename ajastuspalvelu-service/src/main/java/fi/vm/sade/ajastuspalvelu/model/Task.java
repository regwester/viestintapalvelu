package fi.vm.sade.ajastuspalvelu.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Table(name = "tehtava")
@Entity(name = "Task")
public class Task implements Serializable {
    private static final long serialVersionUID = -8322431211657680604L;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task",
        cascade = CascadeType.PERSIST)
    private Set<ScheduledTask> scheduledTasks = new HashSet<ScheduledTask>(0);

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

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

    public Set<ScheduledTask> getScheduledTasks() {
        return scheduledTasks;
    }

    protected void setScheduledTasks(Set<ScheduledTask> scheduledTasks) {
        this.scheduledTasks = scheduledTasks;
    }
}
