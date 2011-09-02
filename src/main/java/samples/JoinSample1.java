package samples;

import java.net.URI;

import com.rayo.core.verb.VerbRef;
import com.rayo.core.JoinDestinationType;


public class JoinSample1 extends BaseSample {

	public void run() throws Exception {
		
		connect("localhost", "user100", "1");
		
		client.waitFor("offer");
		client.answer();
		Thread.sleep(3000);
		//dial and get a call ref
		VerbRef result = client.dial(new URI("sip:userc@127.0.0.1:5060"), new URI("sip:mperez@127.0.0.1:3060"));
		client.waitFor("answered");
		Thread.sleep(3000);
		client.join(result.getVerbId(), "direct", "duplex", JoinDestinationType.CALL);
		Thread.sleep(3000);
		client.unjoin(result.getVerbId(), JoinDestinationType.CALL);
		Thread.sleep(6000);
		client.say("now you can talk");
		Thread.sleep(10000);
		client.hangup();
	}
		
	public static void main(String[] args) throws Exception {
		
		new JoinSample1().run();
	}
}
