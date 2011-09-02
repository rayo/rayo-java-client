package samples;


public class DtmfSample1 extends BaseSample {

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		client.dtmf("1");
		Thread.sleep(5000);
		client.hangup();
	}
	
	public static void main(String[] args) throws Exception {
		
		DtmfSample1 sample = new DtmfSample1();
		sample.connect("localhost", "user100", "1");
		sample.run();
		sample.shutdown();
	}
}
