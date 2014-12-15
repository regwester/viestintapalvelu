package fi.vm.sade.viestintapalvelu.externalinterface.api.dto;

/**
 * Created by jonimake on 10.12.2014.
 */
public class HakuRDTO<T> {
    private T result;
    private String status;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
