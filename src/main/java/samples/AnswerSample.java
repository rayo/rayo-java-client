package samples;


public class AnswerSample extends BaseSample {

	public void run() throws Exception {

		// Two answers should be NOOP
		
		client.waitFor("offer");
		client.answer();
		client.answer();

		Thread.sleep(500000);
		client.hangup();
	}
	
	public static void main(String[] args) throws Exception {
		
		AnswerSample sample = new AnswerSample();
		sample.connect("localhost", "usera", "1");
		sample.run();
		sample.shutdown();
	}
}
