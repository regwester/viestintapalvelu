package fi.vm.sade.viestintapalvelu.template;

import java.util.Date;

public class Replacement {

        private String name = null;

        private String defaultValue = null;

        private boolean mandatory = false;

        private Date timestamp;
        
        private String storingOid;
        
        private long id;
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
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

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Replacement [name=" + name
                    + ", defaultValue=" + defaultValue + ", mandatory="
                    + mandatory + ", timestamp=" + timestamp + ", storingOid="
                    + storingOid + ", id=" + id + "]";
        }

    }

