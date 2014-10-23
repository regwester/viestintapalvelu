package fi.vm.sade.ajastuspalvelu.service.converter;

public interface Converter<M, D> {

    public M convertToModel(D dto);
    
    public D convertToDto(M model);
}
