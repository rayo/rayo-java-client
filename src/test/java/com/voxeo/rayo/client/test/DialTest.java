package com.voxeo.rayo.client.test;

import com.rayo.client.DefaultXmppConnectionFactory;
import com.rayo.client.XmppConnection;
import com.voxeo.rayo.client.internal.XmppIntegrationTest;

public class DialTest extends XmppIntegrationTest {
	
	@Override
	protected XmppConnection createConnection(String hostname, Integer port) {

		return new DefaultXmppConnectionFactory().createConnection(hostname, port);
	}
}
