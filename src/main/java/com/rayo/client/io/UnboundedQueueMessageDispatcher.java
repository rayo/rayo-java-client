package com.rayo.client.io;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateFormatUtils;

import com.rayo.client.filter.XmppObjectFilter;
import com.rayo.client.listener.StanzaListener;
import com.rayo.client.xmpp.stanza.AbstractXmppObject;
import com.rayo.client.xmpp.stanza.Error;
import com.rayo.client.xmpp.stanza.IQ;
import com.rayo.client.xmpp.stanza.Message;
import com.rayo.client.xmpp.stanza.Presence;
import com.rayo.client.xmpp.stanza.XmppObject;
import com.rayo.client.xmpp.stanza.Error.Condition;
import com.rayo.client.xmpp.stanza.Error.Type;

/**
 * <p>Implements the {@link MessageDispatcher} interface providing an 
 * implementation based in an unbouded queue and a thread that reads from 
 * the queue and dispatches messages to the different listeners and filters.</p> 
 * 
 * @author martin
 *
 */
public class UnboundedQueueMessageDispatcher implements MessageDispatcher {

	private Collection<StanzaListener> stanzaListeners = new ConcurrentLinkedQueue<StanzaListener>();
	private Collection<XmppObjectFilter> filters = new ConcurrentLinkedQueue<XmppObjectFilter>();

	private LinkedBlockingQueue<XmppObject> messages = new LinkedBlockingQueue<XmppObject>();
	
	/**
	 * Initiates the message dispatcher. When created, the instance will start a 
	 * new thread that will be ready to process incoming messages.
	 */
	public UnboundedQueueMessageDispatcher() {
		
		Runnable dispatcherTask = new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					XmppObject object = null;
					try {
						object = messages.poll(1000, TimeUnit.SECONDS);
					} catch (InterruptedException e) {}
					
					if (object != null) {
						log(String.format("Fetched XMPP Object [%s] from the dispatching queue", object));
						for(StanzaListener listener: stanzaListeners) {
							if (object instanceof IQ) {
								log(String.format("Invoking listener [%s] onIQ method with IQ id [%s]", listener, object.getId()));
								listener.onIQ((IQ)object);
							} else if (object instanceof Presence) {
								log(String.format("Invoking listener [%s] onPresence method  with presence id [%s]", listener, object.getId()));
								listener.onPresence((Presence)object);
							} else if (object instanceof Message) {
								log(String.format("Invoking listener [%s] onMessage method with message id [%s]", listener, object.getId()));
								listener.onMessage((Message)object);
							} else if (object instanceof Error) {
								log(String.format("Invoking listener [%s] onError method with error id [%s]", listener, object.getId()));
								listener.onError((Error)object);
							}
							log(String.format("Listener [%s] has finished its work", listener));
						}
						filter((AbstractXmppObject)object);
					}
				}
			}
		};
		new Thread(dispatcherTask).start();
	}

	@Override
	public void addStanzaListener(StanzaListener listener) {
		
		stanzaListeners.add(listener);
	}
	
	@Override
	public void removeStanzaListener(StanzaListener listener) {
		
		stanzaListeners.remove(listener);
	}
	
	@Override
    public void addFilter(XmppObjectFilter filter) {

    	filters.add(filter);
    }
    
	@Override
    public void removeFilter(XmppObjectFilter filter) {

    	filters.remove(filter);
    } 
    
    @Override
    public void dispatch(XmppObject object) {

		log(String.format("Dispatching XMPP Object with id [%s] to the dispatching queue", object.getId()));
    	messages.add(object);
    }
    
    private void filter(final AbstractXmppObject object) {

    	log("Invoking filters");
    	for (XmppObjectFilter filter: filters) {		    		
    		try {
    	    	log("Invoking filter " + filter);
    			filter.filter(object);
				log(String.format("Filter [%s] has finished its work", filter));
			} catch (Exception e) {
				e.printStackTrace();
    			dispatch(new Error(Condition.undefined_condition, Type.cancel, String.format("Error on client filter: %s - %s",e.getClass(),e.getMessage())));  
			}    		
    	}                  		
	}
    
    @Override
    public void reset() {

    	messages.clear();
    	filters.clear();
    	stanzaListeners.clear();
    }
    
    private void log(String value) {
    	
    	System.out.println(String.format("[IN ] [%s] [%s] [%s]", DateFormatUtils.format(new Date(),"hh:mm:ss.SSS"), Thread.currentThread(), value));
    }
}
