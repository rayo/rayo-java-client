package samples;

import com.rayo.core.verb.Output;
import com.rayo.core.verb.Ssml;

public class OutputSample3 extends BaseSample {

	public void run() throws Exception {

		client.waitFor("offer");
		client.answer();
		Output output = new Output();
		output.setPrompt(new Ssml("<speak><say-as interpret-as=\"ordinal\">100</say-as></speak>"));
		client.output(output);
		Thread.sleep(5000);
		client.hangup();
	}
	
	public static void main(String[] args) throws Exception {
		
		OutputSample3 sample = new OutputSample3();
		sample.connect("localhost", "usera", "1");
		sample.run();
		sample.shutdown();
	}
}
