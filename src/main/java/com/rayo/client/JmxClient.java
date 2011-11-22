package com.rayo.client;

import org.jolokia.client.J4pClient;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pExecResponse;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;

public class JmxClient {

	J4pClient client;
	
	public JmxClient(String hostname, String port) {
		
		this.client = new J4pClient("http://" + hostname + ":" + port + "/jolokia");
	}

	public Object jmxValue(String url, String attribute) throws Exception {
		
		  J4pReadRequest req = new J4pReadRequest(url,attribute);
		  J4pReadResponse resp = client.execute(req);
		  return resp.getValue();
	}

	public Object jmxExec(String mbean, String operation, Object... args) throws Exception {
		
		  J4pExecRequest req = new J4pExecRequest(mbean,operation, args);
		  J4pExecResponse resp = client.execute(req);
		  return resp.getValue();
	}
}
