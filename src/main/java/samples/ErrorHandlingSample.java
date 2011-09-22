package samples;

import com.rayo.core.verb.VerbRef;
import com.voxeo.servlet.xmpp.rayo.stanza.IQ;
import com.voxeo.servlet.xmpp.rayo.stanza.Error;


public class ErrorHandlingSample extends BaseSample {

	public void run() throws Exception {
		
		String callId = client.waitForOffer().getCallId();
		client.answer(callId);
		client.say("Hello. We are going to cause an error", callId);
		
		// call id and verb id do not exist
		VerbRef ref = new VerbRef("1234", "1234"); 
		IQ iq = client.pause(ref);
		
		if (iq.isError()) {
			Error error = iq.getError();
			System.out.println("Error: " + error);
		}
		
		Thread.sleep(1000);
		client.hangup(callId);
	}
	
	public static void main(String[] args) throws Exception {
		
		ErrorHandlingSample sample = new ErrorHandlingSample();
		sample.connect("localhost", "usera", "1");
		sample.run();
		sample.shutdown();
	}
}
