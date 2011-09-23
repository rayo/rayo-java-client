package com.rayo.client.listener;

import com.rayo.client.xmpp.stanza.IQ;
import com.rayo.client.xmpp.stanza.Presence;

public abstract class RayoMessageListener extends StanzaAdapter {

	private String message;

	public RayoMessageListener(String rayoMessage) {
		
		this.message = rayoMessage;
	}
	
	@Override
	public void onIQ(IQ iq) {

		if (message.equals(iq.getChildName())) {
			messageReceived(iq);
		}
	}
	
	@Override
	public void onPresence(Presence presence) {

		if (message.equals(presence.getChildName())) {
			messageReceived(presence);
		}
	}
	
	public abstract void messageReceived(Object object);
}
