package com.voxeo.rayo.client;

import com.voxeo.rayo.client.auth.AuthenticationSupport;
import com.voxeo.rayo.client.filter.XmppObjectFilterSupport;
import com.voxeo.rayo.client.listener.StanzaListener;
import com.voxeo.rayo.client.response.ResponseHandler;
import com.voxeo.servlet.xmpp.rayo.extensions.Extension;
import com.voxeo.servlet.xmpp.rayo.stanza.XmppObject;


public interface XmppConnection extends XmppObjectFilterSupport, AuthenticationSupport {

	public ConnectionConfiguration getConfiguration();
	public void connect() throws XmppException;
	public void disconnect() throws XmppException;
	public void send(XmppObject object) throws XmppException;
	public void send(XmppObject object, ResponseHandler handler) throws XmppException;
	public XmppObject sendAndWait(XmppObject object) throws XmppException;
	public XmppObject sendAndWait(XmppObject object, int timeout) throws XmppException;
	public void login(String username, String password, String resourceName) throws XmppException;

	public String getConnectionId();
	public String getServiceName();
	public boolean isConnected();
	public boolean isAuthenticated();
	
	public void addStanzaListener(StanzaListener stanzaListener);
	public void removeStanzaListener(StanzaListener stanzaListener);

	public void addXmppConnectionListener(XmppConnectionListener connectionListener);
	public void removeXmppConnectionListener(XmppConnectionListener connectionListener);
	
	XmppObject waitFor(String node) throws XmppException;
	XmppObject waitFor(String node, int timeout) throws XmppException;
	Extension waitForExtension(String extensionName) throws XmppException;
	Extension waitForExtension(String extensionName, int timeout) throws XmppException;

	public String getUsername();
	public String getResource();
}
