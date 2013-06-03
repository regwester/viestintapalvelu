package fi.vm.sade.viestintapalvelu.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;

public abstract class JalkiohjauskirjeMixin {
	@JsonProperty("addressLabel")
	public abstract PostalAddress getPostalAddress();

	@JsonProperty("addressLabel")
	public abstract void setPostalAddress();
}