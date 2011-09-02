package com.voxeo.servlet.xmpp.rayo.extensions;

public interface Provider {

	public Object deserialize(String xml) throws ProviderException;
	
	public String serialize(Object object) throws ProviderException;
}
