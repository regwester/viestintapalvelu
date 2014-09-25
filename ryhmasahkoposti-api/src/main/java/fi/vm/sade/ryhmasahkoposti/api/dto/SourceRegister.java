package fi.vm.sade.ryhmasahkoposti.api.dto;

public class SourceRegister {
    private String name;

    public SourceRegister() {
    }

    public SourceRegister(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
