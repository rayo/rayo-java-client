package samples;

import com.rayo.core.verb.Record;

public class RecordSample extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		Record record = new Record();
		//record.setMaxDuration(new Duration(2000));
		record.setFormat("mp3");
		client.record(record);
		Thread.sleep(10000);
		client.hangup();
		System.out.println("Wait for complete");
		Thread.sleep(2000);
	}
	
	public static void main(String[] args) throws Exception {
		
		RecordSample sample = new RecordSample();
		sample.connect("localhost", "userc", "1");
		sample.run();
		sample.shutdown();
	}
}
