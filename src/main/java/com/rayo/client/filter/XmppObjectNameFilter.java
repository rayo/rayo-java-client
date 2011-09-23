package com.rayo.client.filter;

import com.rayo.client.xmpp.stanza.AbstractXmppObject;

public class XmppObjectNameFilter extends AbstractXmppObjectFilter {

	private String name;
	
	public XmppObjectNameFilter(String name) {
		
		this.name = name;
	}
	
	@Override
	public AbstractXmppObject doFilter(AbstractXmppObject object) {

		if (name.equalsIgnoreCase(object.getStanzaName())) {
			return object;
		}
		return null;
	}
}
