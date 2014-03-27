package fi.vm.sade.viestintapalvelu.template;

public class TemplateContent {

    private long id;
    private int order;
    private String name;
    private String content;
    private String contentType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return "TemplateContent [id=" + id + ", order=" + order + ", name="
                + name + ", content=" + content + ", contentType="
                + contentType + "]";
    }

}
