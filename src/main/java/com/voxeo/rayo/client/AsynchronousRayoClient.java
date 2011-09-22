package com.voxeo.rayo.client;

import com.voxeo.servlet.xmpp.rayo.stanza.IQ;

/**
 * This class is an asynchronousr Rayo Client. Basically extends the base class
 * RayoClient but all the operations invoked will be asynchronous.
 * 
 * @author martin
 *
 */ 
public class AsynchronousRayoClient extends RayoClient {

	
	public AsynchronousRayoClient(String server, Integer port) {
		super(server, port);
	}

	public AsynchronousRayoClient(String server) {
		super(server);
	}

	@Override
	protected IQ sendIQ(IQ iq) throws XmppException {

		connection.send(iq);
		return null;
	}
}
