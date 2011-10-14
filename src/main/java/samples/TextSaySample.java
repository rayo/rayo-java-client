package samples;


public class TextSaySample extends BaseSample {

	public void run() throws Exception {
		
		String callId = client.waitForOffer(900000).getCallId();
		client.answer(callId);
		client.output("Hello World. This is a test on Rayo. I hope you heard this message. Bye bye.", callId);
		client.output("And this is me.", callId);
		Thread.sleep(15000);
		client.hangup(callId);
	}
	
	public static void main(String[] args) throws Exception {
		
		TextSaySample sample = new TextSaySample();
		sample.connect("jabber.org", "mpermar", "xxxx", "telefonica115.orl.voxeo.net");
		sample.run();
		sample.shutdown();
	}
}
