package samples;

import java.net.URI;

import com.rayo.core.verb.VerbRef;

public class DialSample extends BaseSample {

	public void run() throws Exception {
		
		//client.dial(new URI("sip:192.168.1.34:5060"));
		VerbRef dialRef = client.dial(new URI("sip:mperez@127.0.0.1:3060"));
		client.waitFor("answered");
		client.say("hello. how are you", dialRef.getVerbId());
		
		Thread.sleep(5000);
		client.hangup(dialRef.getCallId());
	}
		
	public static void main(String[] args) throws Exception {
		
		DialSample sample = new DialSample();
		sample.connect("localhost", "user100", "1");
		sample.run();
		sample.shutdown();
	}	
}
