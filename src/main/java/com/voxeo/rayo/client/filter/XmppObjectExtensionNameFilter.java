package com.voxeo.rayo.client.filter;

import com.voxeo.servlet.xmpp.rayo.stanza.AbstractXmppObject;
import com.voxeo.servlet.xmpp.rayo.stanza.Stanza;

public class XmppObjectExtensionNameFilter extends AbstractXmppObjectFilter {

	private String name;
	
	public XmppObjectExtensionNameFilter(String name) {
		
		this.name = name;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public AbstractXmppObject doFilter(AbstractXmppObject object) {

		if (object instanceof Stanza) {
			Stanza stanza = (Stanza)object;
			if (stanza.hasExtension()) {
				if (name.equalsIgnoreCase(stanza.getExtension().getStanzaName())) {
					return stanza.getExtension();
				}
			} else {
				if (stanza.getChildName() != null) {
					// will still handle default stanzas as extensions for the sake of this filter
					if (name.equalsIgnoreCase(stanza.getChildName())) {
						return stanza.getExtension();
					}
				}
			}
		}
		return null;
	}
}
