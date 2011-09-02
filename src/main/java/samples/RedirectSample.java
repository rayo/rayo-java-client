package samples;

import java.net.URI;

import com.rayo.core.RedirectCommand;

public class RedirectSample extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		//client.answer();
		//client.dial(new URI("sip:192.168.1.34:5060"));
		
		RedirectCommand redirect = new RedirectCommand();
		redirect.setTo(new URI("sip:mperez@127.0.0.1:3060"));
		client.command(redirect);
		Thread.sleep(60000);
		client.hangup();
	}
		
	public static void main(String[] args) throws Exception {
		
		RedirectSample sample = new RedirectSample();
		sample.connect("localhost", "userc", "1");
		sample.run();
		sample.shutdown();
	}	
}
