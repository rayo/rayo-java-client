package com.rayo.client.listener;

import com.rayo.client.xmpp.stanza.Error;
import com.rayo.client.xmpp.stanza.IQ;
import com.rayo.client.xmpp.stanza.Message;
import com.rayo.client.xmpp.stanza.Presence;

public interface StanzaListener {

	public void onIQ(IQ iq);
	
	public void onMessage(Message message);
	
	public void onPresence(Presence presence);
	
	public void onError(Error error);
}
