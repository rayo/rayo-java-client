package samples;

public class DialSample4a extends BaseSample {

	public void run() throws Exception {
				
		client.waitFor("offer");
		client.answer();
		client.say("Thank you for calling. I'm going now to hang up");
		client.hangup();
		Thread.sleep(100);
	}
		
	public static void main(String[] args) throws Exception {
		
		System.out.println("CLIENT A");
		System.out.println("--------------");
		DialSample4a sample = new DialSample4a();
		sample.connect("localhost", "usera", "1");
		sample.run();
		sample.shutdown();
	}	
}
