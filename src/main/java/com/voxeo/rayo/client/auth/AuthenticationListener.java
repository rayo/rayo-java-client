package com.voxeo.rayo.client.auth;

import java.util.Collection;

import com.voxeo.servlet.xmpp.rayo.stanza.sasl.Challenge;
import com.voxeo.servlet.xmpp.rayo.stanza.sasl.Failure;
import com.voxeo.servlet.xmpp.rayo.stanza.sasl.Success;

public interface AuthenticationListener {

	public void authSettingsReceived(Collection<String> mechanisms);
	
	public void authSuccessful(Success success);
	
	public void authFailure(Failure failure);
	
	public void authChallenge(Challenge challenge);
	
	public void authBindingRequired();
	
	public void authSessionsSupported();
}
