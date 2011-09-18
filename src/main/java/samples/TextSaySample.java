package samples;


public class TextSaySample extends BaseSample {

	public void run() throws Exception {
		
		String callId = client.waitForOffer().getCallId();
		client.answer(callId);
		client.say("Hello World. This is a test on Rayo. I hope you heard this message. Bye bye.", callId);
		Thread.sleep(15000);
		client.hangup(callId);
	}
	
	public static void main(String[] args) throws Exception {
		
		TextSaySample sample = new TextSaySample();
		sample.connect("localhost", "usera", "1");
		sample.run();
		sample.shutdown();
	}
}
