package samples;

import java.net.URI;

import com.rayo.core.verb.VerbRef;

public class PlaybackSample extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();

		VerbRef say = client.say(new URI("http://ccmixter.org/content/DoKashiteru/DoKashiteru_-_you_(na-na-na-na).mp3"));
		Thread.sleep(10000);
		client.pause(say);
		Thread.sleep(10000);
		client.resume(say);
		Thread.sleep(10000);
		client.stop(say);
		Thread.sleep(10000);		

		client.hangup();
	}
		
	public static void main(String[] args) throws Exception {
		
		PlaybackSample sample = new PlaybackSample();
		sample.connect("localhost", "userc", "1");
		sample.run();
		sample.shutdown();
	}	
}
