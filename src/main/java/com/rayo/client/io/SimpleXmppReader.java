package com.rayo.client.io;

import java.io.IOException;
import java.io.Reader;

import com.rayo.client.XmppConnectionListener;
import com.rayo.client.XmppException;
import com.rayo.client.auth.AuthenticationListener;
import com.rayo.client.filter.XmppObjectFilter;
import com.rayo.client.listener.StanzaListener;
import com.rayo.client.xmpp.stanza.Error;

public class SimpleXmppReader implements XmppReader {

	private Reader reader;
	private Thread thread;
	private XmppReaderWorker readingTask;
	
	public SimpleXmppReader() {
		
		this.readingTask = new XmppReaderWorker();
	}
	
	public void init(Reader reader) throws XmppException {
		
		this.reader = reader;
		thread = new Thread(readingTask);
		thread.setDaemon(true);
	}
	
	@Override
	public void addXmppConnectionListener(XmppConnectionListener listener) {

		readingTask.addXmppConnectionListener(listener);
	}

	@Override
	public void removeXmppConnectionListener(XmppConnectionListener listener) {

		readingTask.removeXmppConnectionListener(listener);
	}
	
	@Override
	public void addStanzaListener(StanzaListener listener) {

		readingTask.addStanzaListener(listener);
	}

	@Override
	public void removeStanzaListener(StanzaListener listener) {

		readingTask.removeStanzaListener(listener);
	}
	
    @Override
    public void addAuthenticationListener(AuthenticationListener authListener) {

    	readingTask.addAuthenticationListener(authListener);
    }
    
    @Override
    public void removeAuthenticationListener(AuthenticationListener authListener) {
    	
    	readingTask.removeAuthenticationListener(authListener);
    }
    
    @Override
    public void addFilter(XmppObjectFilter filter) {

    	filter.setReader(this);
    	readingTask.addFilter(filter);
    }
    
    @Override
    public void removeFilter(XmppObjectFilter filter) {

    	readingTask.removeFilter(filter);
    }  
	
	@Override
	public void start() throws XmppException {

		readingTask.resetParser(reader);		
		thread.start();
	}
		
	public Reader getReader() {
		
		return reader;
	}
	
	public void close() throws XmppException {
		
		try {
			readingTask.setDone(true);

			// Wait for pending tasks
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {}
			
			if (reader != null) {
				reader.close();
			}
			
			readingTask.shutdown();
			
		} catch (IOException ioe) {
			throw new XmppException("IO Error", Error.Condition.remote_server_error, ioe);
		}
	}
}
