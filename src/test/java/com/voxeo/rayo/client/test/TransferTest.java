package com.voxeo.rayo.client.test;

import java.net.URI;

import org.junit.Test;

import com.voxeo.rayo.client.internal.XmppIntegrationTest;

public class TransferTest extends XmppIntegrationTest {
	
	@Test
	public void testTransferUri() throws Exception {
		
		rayo.answer();
		rayo.transfer(new URI("tel:123456"));
		
		Thread.sleep(400);
		assertServerReceived("<iq id=\"*\" type=\"set\" from=\"userc@localhost/voxeo\" to=\"#callId@localhost\"><transfer xmlns=\"urn:xmpp:tropo:transfer:1\" terminator=\"#\" timeout=\"20000\" media=\"bridge\" to=\"tel:123456\" answer-on-media=\"false\"></transfer></iq>");
	}
	
	@Test
	public void testTransferText() throws Exception {
		
		rayo.answer();
		rayo.transfer("tel:123456");
		
		Thread.sleep(400);
		assertServerReceived("<iq id=\"*\" type=\"set\" from=\"userc@localhost/voxeo\" to=\"#callId@localhost\"><transfer xmlns=\"urn:xmpp:tropo:transfer:1\" terminator=\"#\" timeout=\"20000\" media=\"bridge\" to=\"tel:123456\" answer-on-media=\"false\"></transfer></iq>");
	}
}
