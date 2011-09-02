package samples;

import java.net.URI;

import com.rayo.core.verb.Record;
import com.rayo.core.verb.VerbRef;

public class RecordSample2 extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		client.say(new URI("http://ccmixter.org/content/DoKashiteru/DoKashiteru_-_you_(na-na-na-na).mp3"));
		Record record = new Record();
		VerbRef ref = client.record(record);
		client.stop(ref);
		Thread.sleep(10000);		
		client.hangup();
		Thread.sleep(10000);
	}
	
	public static void main(String[] args) throws Exception {
		
		RecordSample2 sample = new RecordSample2();
		sample.connect("localhost", "userc", "1");
		sample.run();
		sample.shutdown();
	}
}
