package fi.vm.sade.viestintapalvelu.template;

import java.util.Date;

public class TemplateContent implements Comparable<TemplateContent> {

        private int order;
        
        private String name;
        
        private String content;

        private Date timestamp;

        private String storingOid;
        
        private Long id;

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


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "TemplateContent [order=" + order
                    + ", name=" + name + ", content=" + content
                    + ", timestamp=" + timestamp + ", storingOid=" + storingOid
                    + ", id=" + id + "]";
        }

        @Override
        public int compareTo(TemplateContent o) {
            Integer ord = new Integer(order);
            return ord.compareTo(o.order);
        }

}
