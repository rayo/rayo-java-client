package samples;


public class TextSaySample extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		client.say("Hello World. This is a test on Rayo. I hope you heard this message. Bye bye.");
		Thread.sleep(15000);
		client.hangup();
	}
	
	public static void main(String[] args) throws Exception {
		
		TextSaySample sample = new TextSaySample();
		sample.connect("localhost", "usera", "1");
		sample.run();
		sample.shutdown();
	}
}
