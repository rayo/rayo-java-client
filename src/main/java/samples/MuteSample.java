package samples;


public class MuteSample extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		client.say("Muting the call");
		Thread.sleep(5000);
		client.mute();
		Thread.sleep(3000);
		client.say("Whats app!!");
		Thread.sleep(5000);
		client.unmute();
		Thread.sleep(3000);
		client.say("We are back to normal");
		Thread.sleep(5000);
		client.hangup();
		System.out.println("Wait for complete");
		Thread.sleep(2000);
	}
	
	public static void main(String[] args) throws Exception {
		
		MuteSample sample = new MuteSample();
		sample.connect("localhost", "userc", "1");
		sample.run();
		sample.shutdown();
	}
}
