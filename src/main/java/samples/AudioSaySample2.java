package samples;

import java.net.URI;

public class AudioSaySample2 extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();

		// Does not exist. It should throw an error
		client.say(new URI("http://ccmixter.org/DoKashiteru/DoKashiteru_-_you_(na-na-na-na).mp3"));

		Thread.sleep(500000);
		client.hangup();
	}
	
	public static void main(String[] args) throws Exception {
		
		AudioSaySample2 sample = new AudioSaySample2();
		sample.connect("localhost", "usera", "1");
		sample.run();
		sample.shutdown();
	}
}
