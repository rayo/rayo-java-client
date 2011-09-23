package com.rayo.client.io;

import com.rayo.client.XmppException;
import com.rayo.client.xmpp.stanza.XmppObject;

public interface XmppWriter {

	public void openStream(String serviceName) throws XmppException;
	
	public void write(XmppObject object) throws XmppException;
	
	public void write(String string) throws XmppException;

	public void close() throws XmppException;
}
