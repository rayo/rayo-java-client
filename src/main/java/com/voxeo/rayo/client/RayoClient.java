package com.voxeo.rayo.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.media.mscontrol.join.Joinable;

import org.joda.time.Duration;

import com.rayo.core.verb.Ask;
import com.rayo.core.verb.Choices;
import com.rayo.core.verb.ClientPauseCommand;
import com.rayo.core.verb.ClientResumeCommand;
import com.rayo.core.verb.Conference;
import com.rayo.core.verb.HoldCommand;
import com.rayo.core.verb.InputMode;
import com.rayo.core.verb.MuteCommand;
import com.rayo.core.verb.Output;
import com.rayo.core.verb.Record;
import com.rayo.core.verb.RecordPauseCommand;
import com.rayo.core.verb.RecordResumeCommand;
import com.rayo.core.verb.RefEvent;
import com.rayo.core.verb.Say;
import com.rayo.core.verb.Ssml;
import com.rayo.core.verb.StopCommand;
import com.rayo.core.verb.Transfer;
import com.rayo.core.verb.UnholdCommand;
import com.rayo.core.verb.UnmuteCommand;
import com.rayo.core.verb.VerbRef;
import com.rayo.core.AnswerCommand;
import com.rayo.core.CallCommand;
import com.rayo.core.CallRejectReason;
import com.rayo.core.DialCommand;
import com.rayo.core.DtmfCommand;
import com.rayo.core.HangupCommand;
import com.rayo.core.JoinCommand;
import com.rayo.core.JoinDestinationType;
import com.rayo.core.RejectCommand;
import com.rayo.core.UnjoinCommand;
import com.voxeo.moho.Participant.JoinType;
import com.voxeo.rayo.client.auth.AuthenticationListener;
import com.voxeo.rayo.client.filter.XmppObjectFilter;
import com.voxeo.rayo.client.listener.RayoMessageListener;
import com.voxeo.rayo.client.listener.StanzaListener;
import com.voxeo.servlet.xmpp.rayo.extensions.Extension;
import com.voxeo.servlet.xmpp.rayo.stanza.IQ;
import com.voxeo.servlet.xmpp.rayo.stanza.Stanza;

/**
 * This class servers as a client to the Rayo XMPP platform.
 * 
 * @author martin
 *
 */
public class RayoClient {

	private final XmppConnection connection;
	private static final String DEFAULT_RESOURCE = "voxeo";

	private String lastCallId;

	/**
	 * Creates a new client object. This object will be used to interact with an Rayo server.
	 * 
	 * @param server Server that this client will be connecting to
	 */
	public RayoClient(String server) {

		this(server, null);
	}

	/**
	 * Creates a new client object that will use the specified port number. 
	 * This object will be used to interact with an Rayo server.
	 * 
	 * @param server Server that this client will be connecting to
	 * @param port Port number that the server is listening at
	 */
	public RayoClient(String server, Integer port) {

		connection = new SimpleXmppConnection(server, port);
	}
	
	/**
	 * Connects and authenticates into the Rayo Server. By default it will use the resource 'voxeo'.
	 * 
	 * @param username Rayo username
	 * @param password Rayo password
	 * 
	 * @throws XmppException If the client is not able to connect or authenticate into the Rayo Server
	 */
	public void connect(String username, String password) throws XmppException {
		
		connect(username, password, DEFAULT_RESOURCE);
	}

	/**
	 * Connects and authenticates into the Rayo Server. By default it will use the resource 'voxeo'.
	 * 
	 * @param username Rayo username
	 * @param password Rayo password
	 * @param resource Resource that will be used in this communication
	 * 
	 * @throws XmppException If the client is not able to connect or authenticate into the Rayo Server
	 */
	public void connect(String username, String password, String resource) throws XmppException {
		
		connection.connect();
		connection.login(username, password, resource);
		connection.addStanzaListener(new RayoMessageListener("offer") {
			
			@Override
			@SuppressWarnings("rawtypes")
			public void messageReceived(Object object) {
				
				Stanza stanza = (Stanza)object;
				lastCallId = stanza.getFrom().substring(0, stanza.getFrom().indexOf('@'));
			}
		});		
	}

	/**
	 * Adds a callback class to listen for events on all the incoming stanzas.
	 * 
	 * @param listener Stanza Callback.
	 */
	public void addStanzaListener(StanzaListener listener) {
		
		connection.addStanzaListener(listener);
	}

	/**
	 * Removes a stanza listener
	 * 
	 * @param listener Stanza Callback to be removed
	 */
	public void removeStanzaListener(StanzaListener listener) {
		
		connection.removeStanzaListener(listener);
	}
	
	/**
	 * Adds a callback class to listen for authentication events.
	 * 
	 * @param listener Callback.
	 */
	public void addAuthenticationListener(AuthenticationListener listener) {
		
		connection.addAuthenticationListener(listener);
	}
	
	/**
	 * Adds an XMPP filter
	 * 
	 * @param filter Filter object to be added
	 */
	public void addFilter(XmppObjectFilter filter) {
		
		connection.addFilter(filter);
	}
	
	/**
	 * Removes an XMPP filter
	 * 
	 * @param filter Filter object to be removed
	 */
	public void removeFilter(XmppObjectFilter filter) {
		
		connection.removeFilter(filter);
	}
	
	/**
	 * Disconnects this client connection from the Rayo server
	 * 
	 */
	public void disconnect() throws XmppException {
		
		connection.disconnect();
	}
	
	/**
	 * <p>Waits for an Rayo message. This is a blocking call and therefore should be used carefully. 
	 * When invoked, the invoking thread will block until it receives the specified Rayo 
	 * message.</p>
	 * 
	 * @param rayoMessage Rayo message that the invoking thread will be waiting for
	 *  
	 * @return Object The first Rayo messaging received that matches the specified message name
	 * 
	 * @throws XmppException If there is any problem waiting for the message
	 */
	public Object waitFor(String rayoMessage) throws XmppException {
		
		Extension extension = (Extension)connection.waitForExtension(rayoMessage);
		return extension.getObject();
	}
	
	/**
	 * <p>Waits for an Rayo message. This is a blocking call and therefore should be used carefully. 
	 * When invoked, the invoking thread will block until it receives the specified Rayo 
	 * message.</p>
	 * 
	 * @param rayoMessage Rayo message that the invoking thread will be waiting for
	 * @param clazz Class to cast the returning object to
	 *  
	 * @return T The first Rayo messaging received that matches the specified message name
	 * 
	 * @throws XmppException If there is any problem waiting for the message
	 */
	@SuppressWarnings("unchecked")
	public <T> T waitFor(String rayoMessage, Class<T> clazz) throws XmppException {
		
		Extension extension = (Extension)connection.waitForExtension(rayoMessage);
		return (T)extension.getObject();
	}
	
	/**
	 * <p>Waits for an Rayo message. This is a blocking call but uses a timeout to specify 
	 * the amount of time that the connection will wait until the specified message is received.
	 * If no message is received during the specified timeout then a <code>null</code> object 
	 * will be returned.</p>
	 * 
	 * @param rayoMessage Rayo message that the invoking thread will be waiting for
	 * @param timeout Timeout that will be used when waiting for an incoming Rayo message
	 *  
	 * @return Object The first Rayo messaging received that matches the specified message name 
	 * or <code>null</code> if no message is received during the specified timeout
	 * 
	 * @throws XmppException If there is any problem waiting for the message
	 */
	public Object waitFor(String extensionName, int timeout) throws XmppException {
		
		Extension extension = (Extension)connection.waitForExtension(extensionName, timeout);
		return extension.getObject();
	}
	
	/**
	 * <p>Waits for an Rayo message. This is a blocking call but uses a timeout to specify 
	 * the amount of time that the connection will wait until the specified message is received.
	 * If no message is received during the specified timeout then a <code>null</code> object 
	 * will be returned.</p>
	 * 
	 * @param rayoMessage Rayo message that the invoking thread will be waiting for
	 * @param clazz Class to cast the returning object to
	 * @param timeout Timeout that will be used when waiting for an incoming Rayo message
	 *  
	 * @return Object The first Rayo messaging received that matches the specified message name 
	 * or <code>null</code> if no message is received during the specified timeout
	 * 
	 * @throws XmppException If there is any problem waiting for the message
	 */
	@SuppressWarnings("unchecked")
	public <T> T waitFor(String extensionName, Class<T> clazz, int timeout) throws XmppException {
		
		Extension extension = (Extension)connection.waitForExtension(extensionName, timeout);
		return (T)extension.getObject();
	}

	/**
	 * Answers the latest call that this connection has received from the Rayo server
	 * 
	 * @throws XmppException If there is any issue while answering the call
	 */
	public void answer() throws XmppException {
		
		if (lastCallId != null) {
			answer(lastCallId);
		}
	}
	
	/**
	 * Answers the call with the id specified as a parameter. 
	 * 
	 * @param callId Id of the call that will be answered
	 * 
	 * @throws XmppException If there is any issue while answering the call
	 */
	public void answer(String callId) throws XmppException {
		
		AnswerCommand answer = new AnswerCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(answer));
		connection.send(iq);		
	}

	/**
	 * Accepts the latest call that this connection has received from the Rayo server
	 * 
	 * @throws XmppException If there is any issue while accepting the call
	 */
	public void accept() throws XmppException {
		
		if (lastCallId != null) {
			accept(lastCallId);
		}
	}
	
	/**
	 * Accepts the call with the id specified as a parameter. 
	 * 
	 * @param callId Id of the call that will be accepted
	 * 
	 * @throws XmppException If there is any issue while accepting the call
	 */
	public void accept(String callId) throws XmppException {
		
		AnswerCommand answer = new AnswerCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(answer));
		connection.send(iq);		
	}	
	

	/**
	 * Rejects the latest call that this connection has received from the Rayo server
	 * 
	 * @param reason Code of the reason to reject the call
	 * 
	 * @throws XmppException If there is any issue while rejecting the call
	 */
	public void reject(int reason) throws XmppException {
		
		if (lastCallId != null) {
			reject(CallRejectReason.valueOf(reason), lastCallId);
		}
	}
	
	/**
	 * Rejects the latest call that this connection has received from the Rayo server
	 * 
	 * @param callId Id of the call
	 * 
	 * @throws XmppException If there is any issue while rejecting the call
	 */
	public void reject(String callId) throws XmppException {

		reject(CallRejectReason.DECLINE, callId);
	}
	
	/**
	 * Rejects the latest call that this connection has received from the Rayo server
	 * 
	 * @throws XmppException If there is any issue while rejecting the call
	 */
	public void reject() throws XmppException {
		
		if (lastCallId != null) {
			reject(lastCallId);
		}
	}
	
	/**
	 * Rejects the call with the id specified as a parameter. 
	 * 
	 * @param callId Id of the call that will be accepted
	 * 
	 * @throws XmppException If there is any issue while rejecting the call
	 */
	public void reject(CallRejectReason reason, String callId) throws XmppException {
		
		RejectCommand reject = new RejectCommand(callId, reason);
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(reject));
		connection.send(iq);		
	}	
	
	public VerbRef outputAudio(String audio) throws XmppException, URISyntaxException {
	
		return output(new URI(audio));
	}

	public VerbRef outputSsml(String ssml) throws XmppException {
		
		return saySsml(ssml, lastCallId);
	}

	public VerbRef outputSsml(String ssml, String callId) throws XmppException {
		
		return internalOutput(new Ssml(ssml), lastCallId);
	}

	public VerbRef output(URI uri) throws XmppException {
	
		if (lastCallId != null) {
			return output(uri, lastCallId);
		}
		return null;
	}	

	public VerbRef output(URI uri, String callId) throws XmppException {

		return internalOutput(new Ssml(String.format("<audio src=\"%s\"/>",uri.toString())), callId);
	}

	public VerbRef output(String text) throws XmppException {
	
		if (lastCallId != null) {
			return say(text, lastCallId);
		}
		return null;
	}	

	public VerbRef output(String text, String callId) throws XmppException {

		return internalOutput(new Ssml(text), callId);
	}

	/**
	 * Sends a 'Say' command to Rayo that will play the specified audio file
	 * 
	 * @param audio URI to the audio file
	 * @return SayRef SayRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 * @throws URISyntaxException If the specified audio file is not a valid URI
	 */
	public VerbRef sayAudio(String audio) throws XmppException, URISyntaxException {
	
		return say(new URI(audio));
	}
	
	/**
	 * Sends a 'Say' command including some SSML text
	 * 
	 * @param ssml SSML text
	 * 
	 * @return SayRef SayRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef saySsml(String ssml) throws XmppException {
		
		return saySsml(ssml, lastCallId);
	}
	
	/**
	 * Sends a 'Say' command including some SSML text
	 * 
	 * @param ssml SSML text
	 * @param callId Id of the call to which the say command will be sent 
	 * 
	 * @return SayRef SayRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef saySsml(String ssml, String callId) throws XmppException {
		
		return internalSay(new Ssml(ssml), lastCallId);
	}
	
	/**
	 * Sends a 'Say' command to Rayo that will play the specified audio file
	 * 
	 * @param uri URI to an audio resource that will be played
	 * @return SayRef SayRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef say(URI uri) throws XmppException {
	
		if (lastCallId != null) {
			return say(uri, lastCallId);
		}
		return null;
	}	
	
	/**
	 * Sends a 'Say' command to Rayo that will play the specified audio file
	 * 
	 * @param uri URI to an audio resource that will be played
	 * @param callId Id of the call to which the say command will be sent 
	 * @return SayRef SayRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef say(URI uri, String callId) throws XmppException {

		return internalSay(new Ssml(String.format("<audio src=\"%s\"/>",uri.toString())), callId);
	}

	
	/**
	 * Instructs Rayo to say the specified text
	 * 
	 * @param text Text that we want to say
	 * @return SayRef SayRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef say(String text) throws XmppException {
	
		if (lastCallId != null) {
			return say(text, lastCallId);
		}
		return null;
	}	
	
	/**
	 * Instructs Rayo to say the specified text on the call with the specified id
	 * 
	 * @param text Text that we want to say
	 * @param callId Id of the call to which the say command will be sent 
	 * @return SayRef SayRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef say(String text, String callId) throws XmppException {

		return internalSay(new Ssml(text), callId);
	}
	
	/**
	 * Transfer the last received call to another phone speaking some text before doing the transfer.
	 * 
	 * @param text Text that will be prompted to the user
	 * @param to URI of the call destination
	 * 
	 * @throws XmppException If there is any issue while transfering the call
	 */
	public void transfer(String text, URI to) throws XmppException {

		List<URI> list = new ArrayList<URI>();
		list.add(to);

		if (lastCallId != null) {
			transfer(text, list, lastCallId);
		}
	}

	/**
	 * Transfers the last received call to another destination
	 * 
	 * @param text URI of the call destination
	 * 
	 * @throws XmppException If there is any issue while transfering the call
	 * @throws URISyntaxException If the provided destination is not a valid URI
	 */
	public void transfer(String text) throws XmppException, URISyntaxException {

		transfer(new URI(text));
	}
	
	/**
	 * Transfers the last received call to another destination
	 * 
	 * @param to URI of the call destination
	 * 
	 * @throws XmppException If there is any issue while transfering the call
	 */
	public void transfer(URI to) throws XmppException {

		transfer(null, to);
	}

	/**
	 * Transfers a specific call to another destination
	 * 
	 * @param text Text that will be prompted to the user
	 * @param callId Id of the call we want to transfer
	 * 
	 * @throws XmppException If there is any issue while transfering the call
	 */
	public void transfer(URI to, String callId) throws XmppException {

		List<URI> list = new ArrayList<URI>();
		list.add(to);
		transfer(null, list, callId);
	}

	public void transfer(List<URI> to, String callId) throws XmppException {

		transfer(null, to, callId);
	}
	
	/**
	 * Transfers a call to another phone speaking some text before doing the transfer.
	 * 
	 * @param text Text that will be prompted to the user
	 * @param to URI of the call destination
	 * @param callId Id of the call that we want to transfer
	 * 
	 * @throws XmppException If there is any issue while transfering the call
	 */
	public void transfer(String text, List<URI> to, String callId) throws XmppException {

		Transfer transfer = new Transfer();
		transfer.setTimeout(new Duration(20000));
		transfer.setTerminator('#');

		if (text != null) {
			Ssml ssml = new Ssml(text);
			transfer.setRingbackTone(ssml);
		}
		transfer.setTo(to);
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(transfer));
		connection.send(iq);
	}

	public void hold() throws XmppException {
		
		if (lastCallId != null) {
			hold(lastCallId);
		}
	}
	
	public void hold(String callId) throws XmppException {

		HoldCommand hold = new HoldCommand();
		command(hold,callId);
	}

	public void unhold() throws XmppException {
		
		if (lastCallId != null) {
			unhold(lastCallId);
		}
	}
	
	public void unhold(String callId) throws XmppException {

		UnholdCommand unhold = new UnholdCommand();
		command(unhold,callId);
	}

	public void mute() throws XmppException {
		
		if (lastCallId != null) {
			mute(lastCallId);
		}
	}
	
	public void mute(String callId) throws XmppException {

		MuteCommand mute = new MuteCommand();
		command(mute,callId);
	}

	public void unmute() throws XmppException {
		
		if (lastCallId != null) {
			unmute(lastCallId);
		}
	}
	
	public void unmute(String callId) throws XmppException {

		UnmuteCommand unmute = new UnmuteCommand();
		command(unmute,callId);
	}	
	/**
	 * Calls a specific destination
	 * 
	 * @param text URI to call in text format
	 * 
	 * @throws XmppException If there is any issue while dialing
	 * @throws URISyntaxException If the specified text is not a valid URI
	 */
	public VerbRef dial(String text) throws XmppException, URISyntaxException {

		return dial(new URI(text));
	}
	
	/**
	 * Calls a specific destination
	 * 
	 * @param to URI to dial
	 * 
	 * @throws XmppException If there is any issue while dialing
	 */
	public VerbRef dial(URI to) throws XmppException {

		return dial(null, to);
	}
	
	/**
	 * Calls a specific destination from the specified call id
	 * 
	 * @param from URI that is dialing
	 * @param to URI that we want to dial
	 * 
	 * @throws XmppException If there is any issue while transfering the call
	 */
	public VerbRef dial(URI from, URI to) throws XmppException {

		DialCommand dial = new DialCommand();
		dial.setTo(to);
		dial.setFrom(from);
		try {
			dial.setFrom(new URI("sip:userc@127.0.0.1:5060"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(connection.getServiceName())
			.setChild(Extension.create(dial));
		
		return sendAndGetRef(null, iq);
	}

	private VerbRef sendAndGetRef(String callId, IQ iq) throws XmppException {
		
		VerbRef ref = null;
		IQ result = ((IQ)connection.sendAndWait(iq));
		if (result != null) {
			if (result.hasChild("error")) {
				throw new XmppException(iq.getError());
			}
			RefEvent reference = (RefEvent)result.getExtension().getObject();
			ref = new VerbRef(callId, reference.getJid());
			return ref;
		} else {
			return null;
		}
	}

	/**
	 * Instructs Rayo to ask a question with the specified choices on the latest active call  
	 * 
	 * @param text Text that will be prompted
	 * @param choicesText Choices
	 * 
	 * @throws XmppException If there is any issue while asking the question 
	 */
	public void ask(String text, String choicesText) throws XmppException {

		if (lastCallId != null) {
			ask(text,choicesText,lastCallId);
		}
	}	
	
	/**
	 * Instructs Rayo to ask a question with the specified choices on the call with the given id  
	 * 
	 * @param text Text that will be prompted
	 * @param choicesText Choices
	 * @param callId Id of the call in which the question will be asked
	 * 
	 * @throws XmppException If there is any issue while asking the question 
	 */
	public void ask(String text, String choicesText, String callId) throws XmppException {
		
		Ask ask = new Ask();

		Ssml ssml = new Ssml(text);
		ask.setPrompt(ssml);

		List<Choices> list = new ArrayList<Choices>();
		Choices choices = new Choices();
		choices.setContent(choicesText);
		choices.setContentType("application/grammar+voxeo");
		list.add(choices);
		ask.setChoices(list);
		ask.setTerminator('#');
		ask.setMode(InputMode.DTMF);
		ask.setTimeout(new Duration(650000));
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(ask));
		connection.send(iq);
	}
	
	/**
	 * Creates a conference and joins the last active call 
	 * 
	 * @param roomName Id of the conference
	 * 
	 * @throws XmppException If there is any problem while creating the conference
	 */
	public VerbRef conference(String roomName) throws XmppException {
	
		if (lastCallId != null) {
			return conference(roomName, lastCallId);
		}
		return null;
	}
	
	/**
	 * Creates a conference and joins the last active call 
	 * 
	 * @param roomName Id of the conference
	 * @param callId Id of the call that will be starting the conference
	 * 
	 * @return ConferenceRef A reference to the conference object that has been created
	 * 
	 * @throws XmppException If there is any problem while creating the conference
	 */
	public VerbRef conference(String roomName, String callId) throws XmppException {
		
		Conference conference = new Conference();
		conference.setRoomName(roomName);
		conference.setTerminator('#');
		return conference(conference,callId);
	}
	
	/**
	 * Creates a conference and joins the last active call 
	 * 
	 * @param conference Conference object
	 * 
	 * @return ConferenceRef A reference to the conference object that has been created
	 * 
	 * @throws XmppException If there is any problem while creating the conference
	 */
	public VerbRef conference(Conference conference) throws XmppException {
		
		if (lastCallId != null) {
			return conference(conference, lastCallId);
		}
		return null;
	}
	
	/**
	 * Creates a conference and joins the last active call 
	 * 
	 * @param conference Conference object
	 * @param callId Id of the call that will be starting the conference
	 * 
	 * @return ConferenceRef A reference to the conference object that has been created
	 * 
	 * @throws XmppException If there is any problem while creating the conference
	 */
	public VerbRef conference(Conference conference, String callId) throws XmppException {
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(conference));
		
		return sendAndGetRef(callId, iq);
	}
	
	private VerbRef internalSay(Ssml item, String callId) throws XmppException {
		
		Say say = new Say();
		say.setPrompt(item);
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(say));
		
		return sendAndGetRef(callId, iq);
	}
	
	private VerbRef internalOutput(Ssml item, String callId) throws XmppException {
		
		Output output = new Output();
		output.setPrompt(item);
		
		return output(output, callId);
	}
	
	public VerbRef output(Output output) throws XmppException {
		
		return output(output, lastCallId);
	}
	
	public VerbRef output(Output output, String callId) throws XmppException {
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(output));
		
		return sendAndGetRef(callId, iq);
	}
	
	/**
	 * Pauses a verb component
	 * 
	 * @param ref Verb component that we want to pause
	 */
	public void pause(VerbRef ref) throws XmppException {
		
		ClientPauseCommand pause = new ClientPauseCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(pause));
		connection.send(iq);
	}
	
	/**
	 * Resumes a verb component
	 * 
	 * @param ref Verb component that we want to resume
	 */
	public void resume(VerbRef ref) throws XmppException {
		
		ClientResumeCommand resume = new ClientResumeCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(resume));
		connection.send(iq);
	}
	
	
	/**
	 * Pauses a records component
	 * 
	 * @param ref Verb component that we want to pause
	 */
	public void pauseRecord(VerbRef ref) throws XmppException {
		
		RecordPauseCommand pause = new RecordPauseCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(pause));
		connection.send(iq);
	}
	
	/**
	 * Resumes a record component
	 * 
	 * @param ref Verb component that we want to resume
	 */
	public void resumeRecord(VerbRef ref) throws XmppException {
		
		RecordResumeCommand resume = new RecordResumeCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(resume));
		connection.send(iq);
	}
	
	/**
	 * Stops a verb component
	 * 
	 * @param ref Verb component that we want to stop
	 */
	public void stop(VerbRef ref) throws XmppException {
		
		StopCommand stop = new StopCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(stop));
		connection.send(iq);
	}
	
	public VerbRef record() throws XmppException {
		
		if (lastCallId != null) {
			return record(lastCallId);
		}
		return null;
	}
	
	public VerbRef record(String callId) throws XmppException {
		
		return record(new Record(), callId);
	}
	
	public VerbRef record(Record record) throws XmppException {
		
		if (lastCallId != null) {
			return record(record, lastCallId);
		}
		return null;
	}
	
	public VerbRef record(Record record, String callId) throws XmppException {
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(record));
		
		return sendAndGetRef(callId, iq);
	}
	
	/**
	 * Hangs up the latest active call
	 */
	public void hangup() throws XmppException {
		
		if (lastCallId != null) {
			hangup(lastCallId);
		}
	}
	
	/**
	 * Hangs up the specified call id
	 * 
	 * @param callId Id of the call to be hung up
	 */
	public void hangup(String callId) throws XmppException {
		
		HangupCommand hangup = new HangupCommand(null);
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(hangup));
		connection.send(iq);
	}

	public void unjoin(String from, JoinDestinationType type) throws XmppException {
		
		if (lastCallId != null) {
			unjoin(from, type, lastCallId);
		}	
	}

	public void unjoin(String from, JoinDestinationType type, String callId) throws XmppException {
		
		UnjoinCommand unjoin = new UnjoinCommand();
		unjoin.setFrom(from);
		unjoin.setType(type);
		
		command(unjoin,callId);
	}
	
	public void join(String to, String media, String direction, JoinDestinationType type) throws XmppException {
		
		if (lastCallId != null) {
			join(to,media,direction,type,lastCallId);
		}	
	}

	public void join(String to, String media, String direction, JoinDestinationType type, String callId) throws XmppException {
		
		JoinCommand join = new JoinCommand();
		join.setTo(to);
		join.setDirection(Joinable.Direction.DUPLEX);
		join.setMedia(JoinType.BRIDGE);
		join.setType(type);
		
		command(join,callId);
	}
	
	public void join(JoinCommand join) throws XmppException {
		
		if (lastCallId != null) {
			join(join, lastCallId);
		}
	}
	
	public void join(JoinCommand join, String callId) throws XmppException {
		
		command(join,callId);
	}
	
	public void dtmf(String tones) throws XmppException {
		
		DtmfCommand dtmf = new DtmfCommand(tones);
		command(dtmf);
	}
	
	public void dtmf(String tones, String callId) throws XmppException {
		
		DtmfCommand dtmf = new DtmfCommand(tones);
		command(dtmf, callId);
	}
	
	public void command(CallCommand command) throws XmppException {

		if (lastCallId != null) {
			command(command, lastCallId);
		}
	}
	
	public void command(CallCommand command, String callId) throws XmppException {
        IQ iq = new IQ(IQ.Type.set)
            .setFrom(buildFrom())
            .setTo(buildTo(callId))
            .setChild(Extension.create(command));
        connection.send(iq);
	}
	
	public VerbRef dial(DialCommand command) throws XmppException {
        
		IQ iq = new IQ(IQ.Type.set)
            .setFrom(buildFrom())
            .setTo(connection.getServiceName()) // dials only use service name
            .setChild(Extension.create(command));
        VerbRef ref = sendAndGetRef(null, iq);
        return ref;
	}
	
	public String getLastCallId() {
		
		return lastCallId;
	}
	
	private String buildFrom() {
		
		return connection.getUsername() + "@" + connection.getServiceName() + "/" + connection.getResource();
	}
	
	private String buildTo(String callid) {
		
		return callid + "@" + connection.getServiceName();
	}

	private String buildTo(String callid, String resourceId) {
		
		return callid + "@" + connection.getServiceName() + "/" + resourceId;
	}
	
	public XmppConnection getXmppConnection() {
		
		return connection;
	}
}
