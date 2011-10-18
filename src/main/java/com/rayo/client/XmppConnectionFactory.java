package com.rayo.client;

/**
 * <p>Builds an XMPP Connection</p>
 * 
 * @author martin
 *
 */
public interface XmppConnectionFactory {

	XmppConnection createConnection(String hostname, Integer port);
}
