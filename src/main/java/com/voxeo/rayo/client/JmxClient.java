package com.voxeo.rayo.client;

import org.jolokia.client.J4pClient;
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

	public static void main(String[] args) throws Exception {
		
		System.out.print(new JmxClient("localhost","8080").jmxValue("com.rayo:Type=MixerStatistics", "ActiveMixersCount"));
	}
}
