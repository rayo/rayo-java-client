package com.rayo.client.io;

import com.rayo.client.filter.XmppObjectFilterSupport;
import com.rayo.client.listener.StanzaListenerSupport;
import com.rayo.client.xmpp.stanza.XmppObject;

/**
 * <p>A message dispatcher is used by the main XMPP reader loop to dispatch 
 * messages to listeners and filters.</p>
 * 
 * <p>A message dispatcher implementation should be non blocking and thread safe, 
 * freeing the XMPP reader main loop from any potential locks.</p> 
 * 
 * @author martin
 *
 */
public interface MessageDispatcher extends StanzaListenerSupport, XmppObjectFilterSupport {

	/**
	 * Dispatchs a message to the different listeners and filters
	 * 
	 * @param object
	 */
	void dispatch(XmppObject object);

	/**
	 * Resets the message dispatcher. Message dispatcher implementations will have to clean 
	 * all the queues, listeners, etc. that might have implemented.
	 */
	void reset();
}
