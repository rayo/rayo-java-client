package com.rayo.client;

import com.rayo.client.xmpp.stanza.XmppObject;


public interface XmppConnectionListener {

	public void connectionEstablished(String connectionId);
	public void connectionFinished(String connectionId);
	public void connectionError(String connectionId, Exception e);
	public void connectionReset(String connectionId);
	
	public void messageSent(XmppObject message);
}
