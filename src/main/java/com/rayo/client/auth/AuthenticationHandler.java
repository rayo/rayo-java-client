package com.rayo.client.auth;

import com.rayo.client.XmppException;

public interface AuthenticationHandler extends AuthenticationListener {

	public void login(String username, String password, String resource) throws XmppException;
	
	public boolean isAuthenticated();
}
