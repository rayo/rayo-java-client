package com.rayo.client.response;

import com.rayo.client.xmpp.stanza.XmppObject;

public interface ResponseHandler {

	public void handle(XmppObject response);
}
