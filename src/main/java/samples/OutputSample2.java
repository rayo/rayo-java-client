package samples;

import com.rayo.core.verb.Output;
import com.rayo.core.verb.Ssml;

public class OutputSample2 extends BaseSample {

	public void run() throws Exception {
		// Invalid SSML. Should send an error
		client.waitFor("offer");
		client.answer();
		Output output = new Output();
		output.setPrompt(new Ssml("<output-as interpret-as=\"ordinal\">100</output-as>"));
		client.output(output);
		Thread.sleep(5000);
		client.hangup();
	}
	
	public static void main(String[] args) throws Exception {
		
		OutputSample2 sample = new OutputSample2();
		sample.connect("localhost", "usera", "1");
		sample.run();
		sample.shutdown();
	}
}
