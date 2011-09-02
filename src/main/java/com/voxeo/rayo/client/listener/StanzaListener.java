package com.voxeo.rayo.client.listener;

import com.voxeo.servlet.xmpp.rayo.stanza.Error;
import com.voxeo.servlet.xmpp.rayo.stanza.IQ;
import com.voxeo.servlet.xmpp.rayo.stanza.Message;
import com.voxeo.servlet.xmpp.rayo.stanza.Presence;

public interface StanzaListener {

	public void onIQ(IQ iq);
	
	public void onMessage(Message message);
	
	public void onPresence(Presence presence);
	
	public void onError(Error error);
}
