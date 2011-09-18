package samples;


public class HoldSample extends BaseSample {

	public void run() throws Exception {
		
		String callId = client.waitForOffer().getCallId();
		client.answer(callId);
		client.say("Putting the call on hold", callId);
		Thread.sleep(5000);
		client.hold(callId);
		Thread.sleep(3000);
		//client.say("Unholding call");
		Thread.sleep(5000);
		client.unhold(callId);
		Thread.sleep(3000);
		client.say("We are back to normal", callId);
		Thread.sleep(5000);
		client.hangup(callId);
		System.out.println("Wait for complete");
		Thread.sleep(2000);
	}
	
	public static void main(String[] args) throws Exception {
		
		HoldSample sample = new HoldSample();
		sample.connect("localhost", "userc", "1");
		sample.run();
		sample.shutdown();
	}
}
