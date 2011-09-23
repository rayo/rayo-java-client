package com.rayo.client.auth;

import java.util.Collection;

import com.rayo.client.xmpp.stanza.sasl.Challenge;
import com.rayo.client.xmpp.stanza.sasl.Failure;
import com.rayo.client.xmpp.stanza.sasl.Success;

public interface AuthenticationListener {

	public void authSettingsReceived(Collection<String> mechanisms);
	
	public void authSuccessful(Success success);
	
	public void authFailure(Failure failure);
	
	public void authChallenge(Challenge challenge);
	
	public void authBindingRequired();
	
	public void authSessionsSupported();
}
