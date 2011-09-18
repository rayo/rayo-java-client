package com.voxeo.rayo.client.test;

import java.net.URI;

import org.junit.Test;

import com.voxeo.rayo.client.internal.XmppIntegrationTest;

public class DialTest extends XmppIntegrationTest {
	
	@Test
	public void testDialUri() throws Exception {
		
		rayo.answer(lastCallId);
		rayo.dial(new URI("tel:123456789"));
		
		Thread.sleep(400);
		assertServerReceived("<iq id=\"*\" type=\"set\" from=\"userc@localhost/voxeo\" to=\"localhost\"><dial xmlns=\"urn:xmpp:rayo:1\" to=\"tel:123456789\" from=\"sip:userc@127.0.0.1:5060\"></dial></iq>");
	}
	
	@Test
	public void testDialText() throws Exception {
		
		rayo.answer(lastCallId);
		rayo.dial("tel:123456789");
		
		Thread.sleep(400);
		assertServerReceived("<iq id=\"*\" type=\"set\" from=\"userc@localhost/voxeo\" to=\"localhost\"><dial xmlns=\"urn:xmpp:rayo:1\" to=\"tel:123456789\" from=\"sip:userc@127.0.0.1:5060\"></dial></iq>");
	}
	
}
