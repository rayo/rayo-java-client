package com.voxeo.rayo.client.test.util;

import com.rayo.client.response.ResponseHandler;
import com.rayo.client.xmpp.stanza.XmppObject;

public class MockResponseHandler implements ResponseHandler {

	private int handled;
	
	@Override
	public void handle(XmppObject response) {

		handled ++;
	}
	
	public int getHandled() {
		
		return handled;
	}
}
