package com.voxeo.rayo.client.listener;

import com.voxeo.servlet.xmpp.rayo.stanza.Error;
import com.voxeo.servlet.xmpp.rayo.stanza.IQ;
import com.voxeo.servlet.xmpp.rayo.stanza.Message;
import com.voxeo.servlet.xmpp.rayo.stanza.Presence;

public abstract class StanzaAdapter implements StanzaListener {

	@Override
	public void onIQ(IQ iq) {
		
	}
	
	@Override
	public void onMessage(Message message) {
		
	}
	
	@Override
	public void onPresence(Presence presence) {
		
	}
	
	@Override
	public void onError(Error error) {
		
	}
}
