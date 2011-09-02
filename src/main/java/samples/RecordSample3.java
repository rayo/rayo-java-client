package samples;

import java.net.URI;

import com.rayo.core.verb.Record;
import com.rayo.core.verb.VerbRef;

public class RecordSample3 extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		//client.say(new URI("http://ccmixter.org/content/DoKashiteru/DoKashiteru_-_you_(na-na-na-na).mp3"));
		Record record = new Record();
		record.setFormat("WAV");
		//record.setCodec("INFERRED");
		VerbRef ref = client.record(record);
		Thread.sleep(1000);
		client.say("Hello. How are you");
		Thread.sleep(5000);
		client.stop(ref);
		Thread.sleep(10000);		
		client.hangup();
		Thread.sleep(10000);
	}
	
	public static void main(String[] args) throws Exception {
		
		RecordSample3 sample = new RecordSample3();
		sample.connect("localhost", "userc", "1");
		sample.run();
		sample.shutdown();
	}
}
