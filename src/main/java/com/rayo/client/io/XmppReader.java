package com.rayo.client.io;

import java.io.Reader;

import com.rayo.client.XmppConnectionListener;
import com.rayo.client.XmppException;
import com.rayo.client.auth.AuthenticationSupport;
import com.rayo.client.filter.XmppObjectFilterSupport;
import com.rayo.client.listener.StanzaListener;


public interface XmppReader extends XmppObjectFilterSupport, AuthenticationSupport {

	public void init(Reader reader) throws XmppException;
	public void start() throws XmppException;
	
	public void close() throws XmppException;
	
	public void addXmppConnectionListener(XmppConnectionListener listener);
	public void removeXmppConnectionListener(XmppConnectionListener listener);
	
    public void addStanzaListener(StanzaListener stanzaListener);
    public void removeStanzaListener(StanzaListener stanzaListener);

}
