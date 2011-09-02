package com.voxeo.rayo.client.response;

import com.voxeo.servlet.xmpp.rayo.stanza.XmppObject;

public interface ResponseHandler {

	public void handle(XmppObject response);
}
