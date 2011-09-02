package samples;


public class SsmlSaySample extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		client.saySsml("<audio src=\"http://ccmixter.org/content/fluffy/fluffy_-_You_Believed_It_Yourself_4.mp3\"></audio>Hello world");
		Thread.sleep(500000);
		client.hangup();
	}
	
	public static void main(String[] args) throws Exception {
		
		SsmlSaySample sample = new SsmlSaySample();
		sample.connect("localhost", "userc", "1");
		sample.run();
		sample.shutdown();
	}
}
