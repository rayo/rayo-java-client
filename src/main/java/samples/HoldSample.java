package samples;


public class HoldSample extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		client.say("Putting the call on hold");
		Thread.sleep(5000);
		client.hold();
		Thread.sleep(3000);
		//client.say("Unholding call");
		Thread.sleep(5000);
		client.unhold();
		Thread.sleep(3000);
		client.say("We are back to normal");
		Thread.sleep(5000);
		client.hangup();
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
