package net.vuijst.jackson.guice.test.model;

import javax.inject.Named;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PersonWithJavaxNamedAddress {
	
	@JsonIgnore
	@JacksonInject
	@Named("JavaxNamedHomeAddress")
	private IAddress address = null;

	private String name;
	
	public IAddress getAddress() {
		return this.address;
	}

	public String getName() {
		return name;
	}

	public String readAddressType() {
		return this.address.getAddressType();
	}

	public String readStreet() {
		return this.address.getStreet();
	}
	
	public void setAddress(final IAddress address) {
		this.address = address;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public void writeStreet(final String street) {
		this.address.setStreet(street);
	}
}
