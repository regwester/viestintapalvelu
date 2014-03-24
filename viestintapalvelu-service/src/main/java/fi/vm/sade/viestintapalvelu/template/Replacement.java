package fi.vm.sade.viestintapalvelu.template;

public class Replacement {
	private String name = null;
	private String defaultValue = null;
	private boolean mandatory = false;
	
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
	@Override
	public String toString() {
		return "Replacement [name=" + name + ", defaultValue=" + defaultValue
				+ ", mandatory=" + mandatory + "]";
	}
	
}
