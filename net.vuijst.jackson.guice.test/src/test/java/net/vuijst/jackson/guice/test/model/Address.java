package net.vuijst.jackson.guice.test.model;

public class Address implements IAddress {

	public Address() {
	}

	public Address(final String addressType) {
		this.addressType = addressType;
	}

	private String addressType = "Unknown address type";
	private String street;

	@Override
	public String getStreet() {
		return street;
	}

	@Override
	public void setStreet(final String street) {
		this.street = street;
	}

	@Override
	public String getAddressType() {
		return this.addressType;
	}

}
