package samples;

import java.util.HashMap;
import java.util.Map;

import javax.media.mscontrol.join.Joinable;

import com.rayo.core.verb.Conference;
import com.rayo.core.verb.Ssml;
import com.rayo.core.JoinCommand;
import com.rayo.core.JoinDestinationType;
import com.voxeo.moho.Participant.JoinType;


public class MixerSample extends BaseSample {

	public void run() throws Exception {
		
		connect("localhost", "userc", "1");
		
		client.waitFor("offer");
		String firstCall = client.getLastCallId();
		client.answer(firstCall);
		client.say("We are goint to create a conference and then say something to it");
		Conference conference = buildConference();
		client.conference(conference, firstCall);
		
		client.waitFor("offer");
		String secondCall = client.getLastCallId();
		client.answer(secondCall);

		JoinCommand join = buildJoinCommand(conference.getRoomName());
		client.command(join, secondCall);
		
		client.say("Oh my god, we are speaking to the conference.Oh my god, we are speaking to the conference.Oh my god, we are speaking to the conference.Oh my god, we are speaking to the conference.", conference.getRoomName());
		Thread.sleep(10000);
		client.hangup();
	}
	
	private JoinCommand buildJoinCommand(String conferenceName) {
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("playTones", "true");
		JoinCommand join = new JoinCommand();
		join.setTo(conferenceName);
		join.setType(JoinDestinationType.MIXER);
		join.setDirection(Joinable.Direction.DUPLEX);
		join.setMedia(JoinType.BRIDGE);
		
		return join;
	}
	
	private Conference buildConference() {
		
        Conference conference = new Conference();
        conference.setRoomName("1234");
        conference.setModerator(true);
        conference.setBeep(false);
        Ssml announcementSsml = new Ssml("has joined the conference");  
        announcementSsml.setVoice("allison");
        conference.setAnnouncement(announcementSsml);
        Ssml musicSsml = new Ssml("The moderator how not yet joined.. Listen to this awesome music while you wait.<audio src='http://www.yanni.com/music/awesome.mp3' />");
        musicSsml.setVoice("herbert");
        conference.setHoldMusic(musicSsml);
        
        return conference;
	}
		
	public static void main(String[] args) throws Exception {
		
		new MixerSample().run();
	}
}
