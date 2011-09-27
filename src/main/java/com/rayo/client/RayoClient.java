package com.rayo.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.media.mscontrol.join.Joinable;

import org.joda.time.Duration;

import com.rayo.client.auth.AuthenticationListener;
import com.rayo.client.filter.XmppObjectFilter;
import com.rayo.client.listener.RayoMessageListener;
import com.rayo.client.listener.StanzaListener;
import com.rayo.client.verb.ClientPauseCommand;
import com.rayo.client.verb.ClientResumeCommand;
import com.rayo.client.verb.RefEvent;
import com.rayo.client.xmpp.extensions.Extension;
import com.rayo.client.xmpp.stanza.IQ;
import com.rayo.client.xmpp.stanza.Stanza;
import com.rayo.core.AcceptCommand;
import com.rayo.core.AnswerCommand;
import com.rayo.core.CallCommand;
import com.rayo.core.CallRejectReason;
import com.rayo.core.DialCommand;
import com.rayo.core.DtmfCommand;
import com.rayo.core.HangupCommand;
import com.rayo.core.JoinCommand;
import com.rayo.core.JoinDestinationType;
import com.rayo.core.OfferEvent;
import com.rayo.core.RedirectCommand;
import com.rayo.core.RejectCommand;
import com.rayo.core.UnjoinCommand;
import com.rayo.core.verb.Ask;
import com.rayo.core.verb.Choices;
import com.rayo.core.verb.Conference;
import com.rayo.core.verb.HoldCommand;
import com.rayo.core.verb.Input;
import com.rayo.core.verb.InputMode;
import com.rayo.core.verb.MuteCommand;
import com.rayo.core.verb.Output;
import com.rayo.core.verb.Record;
import com.rayo.core.verb.RecordPauseCommand;
import com.rayo.core.verb.RecordResumeCommand;
import com.rayo.core.verb.Say;
import com.rayo.core.verb.SeekCommand;
import com.rayo.core.verb.SpeedDownCommand;
import com.rayo.core.verb.SpeedUpCommand;
import com.rayo.core.verb.Ssml;
import com.rayo.core.verb.StopCommand;
import com.rayo.core.verb.Transfer;
import com.rayo.core.verb.UnholdCommand;
import com.rayo.core.verb.UnmuteCommand;
import com.rayo.core.verb.VerbRef;
import com.rayo.core.verb.VolumeDownCommand;
import com.rayo.core.verb.VolumeUpCommand;
import com.voxeo.moho.Participant.JoinType;

/**
 * This class servers as a client to the Rayo XMPP platform.
 * 
 * @author martin
 *
 */
public class RayoClient {

	protected final XmppConnection connection;
	private static final String DEFAULT_RESOURCE = "voxeo";

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
		/*
		 * Left as example
		 * 
		connection.addStanzaListener(new RayoMessageListener("offer") {
			
			@Override
			@SuppressWarnings("rawtypes")
			public void messageReceived(Object object) {
				
				Stanza stanza = (Stanza)object;
				lastCallId = stanza.getFrom().substring(0, stanza.getFrom().indexOf('@'));
			}
		});
		*/		
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
	 * <p>Waits for an Offer Event. Shortcut method to wait for an incoming call.</p>
	 * 
	 * @return OfferEvent Offer event that has been received
	 * 
	 * @throws XmppException If there is any problem waiting for offer event
	 */
	public OfferEvent waitForOffer() throws XmppException {
	
		return waitForOffer(null);
	}
	
	/**
	 * <p>Waits for an Offer Event. Shortcut method to wait for an incoming call.</p>
	 * 
	 * @timeout Timeout 
	 * @return OfferEvent Offer event that has been received
	 * 
	 * @throws XmppException If there is any problem waiting for offer event
	 */
	public OfferEvent waitForOffer(Integer timeout) throws XmppException {
		
		final StringBuilder callId = new StringBuilder();
		RayoMessageListener tempListener = new RayoMessageListener("offer") {
			
			@Override
			@SuppressWarnings("rawtypes")
			public void messageReceived(Object object) {
				
				Stanza stanza = (Stanza)object;
				callId.append(stanza.getFrom().substring(0, stanza.getFrom().indexOf('@')));
			}
		};
		addStanzaListener(tempListener);
		try {
			OfferEvent stanza = waitFor("offer", OfferEvent.class, timeout);
			
			OfferEvent offer = new OfferEvent(callId.toString());
			offer.setTo(stanza.getTo());
			offer.setFrom(stanza.getFrom());
			offer.setHeaders(stanza.getHeaders());
			
			return offer;
		} finally {
			removeStanzaListener(tempListener);
		}
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
	public <T> T waitFor(String extensionName, Class<T> clazz, Integer timeout) throws XmppException {
		
		Extension extension = (Extension)connection.waitForExtension(extensionName, timeout);
		return (T)extension.getObject();
	}
	
	/**
	 * Answers the call with the id specified as a parameter. 
	 * 
	 * @param callId Id of the call that will be answered
	 * 
	 * @return IQ Resulting IQ
	 * 
	 * @throws XmppException If there is any issue while answering the call
	 */
	public IQ answer(String callId) throws XmppException {
		
		return answer(callId, new AnswerCommand());	
	}
	
	
	/**
	 * Answers the call with the id specified as a parameter. 
	 * 
	 * @param callId Id of the call that will be answered
	 * @param command Answer command
	 * 
	 * @return IQ Resulting IQ
	 * 
	 * @throws XmppException If there is any issue while answering the call
	 */
	public IQ answer(String callId, AnswerCommand command) throws XmppException {
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(command));
		return sendIQ(iq);		
	}
	
	/**
	 * Accepts the call with the id specified as a parameter. 
	 * 
	 * @param callId Id of the call that will be accepted
	 * @return IQ Resulting IQ
	 * @throws XmppException If there is any issue while accepting the call
	 */
	public IQ accept(String callId) throws XmppException {
		
		return accept(callId, new AcceptCommand());	
	}	
	
	
	/**
	 * Accepts the call with the id specified as a parameter. 
	 * 
	 * @param callId Id of the call that will be accepted
	 * @param command Accept command
	 * @return IQ Resulting IQ
	 * @throws XmppException If there is any issue while accepting the call
	 */
	public IQ accept(String callId, AcceptCommand command) throws XmppException {
		
		return command(command, callId);	
	}	
	
	/**
	 * Rejects the latest call that this connection has received from the Rayo server
	 * 
	 * @param callId Id of the call
	 * @return IQ Resulting IQ
	 * 
	 * @throws XmppException If there is any issue while rejecting the call
	 */
	public IQ reject(String callId) throws XmppException {

		return reject(CallRejectReason.DECLINE, callId);
	}
	
	
	/**
	 * Rejects a call id
	 * 
	 * @param reject Reject command
	 * @param callId Id of the call
	 * @return IQ Resulting IQ
	 * 
	 * @throws XmppException If there is any issue while rejecting the call
	 */
	public IQ reject(String callId, RejectCommand reject) throws XmppException {

		return command(reject, callId);
	}
	
	/**
	 * Rejects the call with the id specified as a parameter. 
	 * 
	 * @param callId Id of the call that will be accepted
	 * @return IQ Resulting IQ
	 * 
	 * @throws XmppException If there is any issue while rejecting the call
	 */
	public IQ reject(CallRejectReason reason, String callId) throws XmppException {
		
		RejectCommand reject = new RejectCommand(callId, reason);
		return command(reject, callId);	
	}	

	public VerbRef outputSsml(String ssml, String callId) throws XmppException {
		
		return internalOutput(new Ssml(ssml), callId);
	}

	public VerbRef output(URI uri, String callId) throws XmppException {

		return internalOutput(new Ssml(String.format("<audio src=\"%s\"/>",uri.toString())), callId);
	}	

	public VerbRef output(String text, String callId) throws XmppException {

		return internalOutput(new Ssml(text), callId);
	}
	
	/**
	 * Sends a 'Say' command including some SSML text
	 * 
	 * @param ssml SSML text
	 * @param callId Id of the call to which the say command will be sent 
	 * 
	 * @return VerbRef VerbRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef saySsml(String ssml, String callId) throws XmppException {
		
		return internalSay(new Ssml(ssml), callId);
	}
	
	/**
	 * Sends a 'Say' command to Rayo that will play the specified audio file
	 * 
	 * @param audio URI to the audio file
	 * @param callId Id of the call
	 * 
	 * @return VerbRef VerbRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 * @throws URISyntaxException If the specified audio file is not a valid URI
	 */
	public VerbRef sayAudio(String audio, String callId) throws XmppException, URISyntaxException {
	
		return say(new URI(audio), callId);
	}
	
	/**
	 * Sends a 'Say' command to Rayo that will play the specified audio file
	 * 
	 * @param uri URI to an audio resource that will be played
	 * @param callId Id of the call to which the say command will be sent 
	 * @return VerbRef VerbRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef say(URI uri, String callId) throws XmppException {

		return internalSay(new Ssml(String.format("<audio src=\"%s\"/>",uri.toString())), callId);
	}
	
	/**
	 * Instructs Rayo to say the specified text on the call with the specified id
	 * 
	 * @param text Text that we want to say
	 * @param callId Id of the call to which the say command will be sent 
	 * @return VerbRef VerbRef instance that allows to handle the say stream
	 * 
	 * @throws XmppException If there is any issues while sending the say command
	 */
	public VerbRef say(String text, String callId) throws XmppException {

		return internalSay(new Ssml(text), callId);
	}

	/**
	 * Transfers a specific call to another destination
	 * 
	 * @param to URI where the call will be transfered
	 * @param callId Id of the call we want to transfer
	 * @return IQ Resulting IQ
	 * @throws XmppException If there is any issue while transfering the call
	 */
	public IQ transfer(URI to, String callId) throws XmppException {

		List<URI> list = new ArrayList<URI>();
		list.add(to);
		return transfer(null, list, callId);
	}
	

	/**
	 * Transfers a specific call to another destination
	 * 
	 * @param to URI where the call will be transfered
	 * @param callId Id of the call we want to transfer
	 * @return IQ Resulting IQ
	 * @throws XmppException If there is any issue while transfering the call
	 * @throws URISyntaxException If an invalid URI is passed as a parameter
	 */
	public IQ transfer(String to, String callId) throws XmppException, URISyntaxException {

		return transfer(new URI(to), callId);
	}

	public IQ transfer(List<URI> to, String callId) throws XmppException {

		return transfer(null, to, callId);
	}
	
	/**
	 * Transfers a call to another phone speaking some text before doing the transfer.
	 * 
	 * @param text Text that will be prompted to the user
	 * @param to URI of the call destination
	 * @param callId Id of the call that we want to transfer
	 * @return IQ Resulting IQ
	 * @throws XmppException If there is any issue while transfering the call
	 */
	public IQ transfer(String text, List<URI> to, String callId) throws XmppException {

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
		return sendIQ(iq);
	}
	
	public IQ hold(String callId) throws XmppException {

		HoldCommand hold = new HoldCommand();
		return command(hold,callId);
	}
	
	public IQ unhold(String callId) throws XmppException {

		UnholdCommand unhold = new UnholdCommand();
		return command(unhold,callId);
	}
	
	public IQ mute(String callId) throws XmppException {

		MuteCommand mute = new MuteCommand();
		return command(mute,callId);
	}
	
	public IQ unmute(String callId) throws XmppException {

		UnmuteCommand unmute = new UnmuteCommand();
		return command(unmute,callId);
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
	 * Instructs Rayo to ask a question with the specified choices on the call with the given id  
	 * 
	 * @param text Text that will be prompted
	 * @param choicesText Choices
	 * @param callId Id of the call in which the question will be asked
	 * @return IQ Resulting IQ
	 * @throws XmppException If there is any issue while asking the question 
	 */
	public IQ ask(String text, String choicesText, String callId) throws XmppException {
		
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
		return sendIQ(iq);
	}
	
	public VerbRef input(Input input, String callId) throws XmppException {
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(input));
		
		return sendAndGetRef(callId, iq);
	}
	
	/**
	 * Creates a conference and joins the last active call 
	 * 
	 * @param roomName Id of the conference
	 * @param callId Id of the call that will be starting the conference
	 * 
	 * @return VerbRef A reference to the conference object that has been created
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
	 * @param callId Id of the call that will be starting the conference
	 * 
	 * @return VerbRef A reference to the conference object that has been created
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
	 * @return IQ Resulting IQ
	 */
	public IQ pause(VerbRef ref) throws XmppException {
		
		ClientPauseCommand pause = new ClientPauseCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(pause));
		return sendIQ(iq);
	}
	
	/**
	 * Resumes a verb component
	 * 
	 * @param ref Verb component that we want to resume
	 * @return IQ Resulting IQ
	 */
	public IQ resume(VerbRef ref) throws XmppException {
		
		ClientResumeCommand resume = new ClientResumeCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(resume));
		return sendIQ(iq);
	}
	
	/**
	 * Speeds up
	 * 
	 * @param ref Verb component that we want to speed up
	 * @return IQ Resulting IQ
	 */
	public IQ speedUp(VerbRef ref) throws XmppException {
		
		SpeedUpCommand speedup = new SpeedUpCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(speedup));
		return sendIQ(iq);
	}
	
	/**
	 * Speeds down
	 * 
	 * @param ref Verb component that we want to speed up
	 * @return IQ Resulting IQ
	 */
	public IQ speedDown(VerbRef ref) throws XmppException {
		
		SpeedDownCommand speedDown = new SpeedDownCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(speedDown));
		return sendIQ(iq);
	}
	
	
	/**
	 * Turn volume up
	 * 
	 * @param ref Verb component that we want to turn volume up
	 * @return IQ Resulting IQ
	 */
	public IQ volumeUp(VerbRef ref) throws XmppException {
		
		VolumeUpCommand volumeUp = new VolumeUpCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(volumeUp));
		return sendIQ(iq);
	}
	
	/**
	 * Turn volume down
	 * 
	 * @param ref Verb component that we want to turn volume down
	 * @return IQ Resulting IQ
	 */
	public IQ volumeDown(VerbRef ref) throws XmppException {
		
		VolumeDownCommand volumeDown = new VolumeDownCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(volumeDown));
		return sendIQ(iq);
	}
	
	/**
	 * Pauses a records component
	 * 
	 * @param ref Verb component that we want to pause
	 */
	public IQ pauseRecord(VerbRef ref) throws XmppException {
		
		RecordPauseCommand pause = new RecordPauseCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(pause));
		return sendIQ(iq);
	}
	
	/**
	 * Resumes a record component
	 * 
	 * @param ref Verb component that we want to resume
	 */
	public IQ resumeRecord(VerbRef ref) throws XmppException {
		
		RecordResumeCommand resume = new RecordResumeCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(resume));
		return sendIQ(iq);
	}
	
	/**
	 * Performs a seek operation on the given verb
	 * 
	 * @param ref Verb component that we want to resume
	 * @param command Seek command to execute
	 */
	public IQ seek(VerbRef ref, SeekCommand command) throws XmppException {
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(command));
		return sendIQ(iq);
	}
	
	/**
	 * Stops a verb component
	 * 
	 * @param ref Verb component that we want to stop
	 */
	public IQ stop(VerbRef ref) throws XmppException {
		
		StopCommand stop = new StopCommand();
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(ref.getCallId(),ref.getVerbId()))
			.setChild(Extension.create(stop));
		return sendIQ(iq);
	}
	
	public VerbRef record(String callId) throws XmppException {
		
		return record(new Record(), callId);
	}
	
	public VerbRef record(Record record, String callId) throws XmppException {
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(record));
		
		return sendAndGetRef(callId, iq);
	}
	
	/**
	 * Hangs up the specified call id
	 * 
	 * @param callId Id of the call to be hung up
	 * @return IQ Resulting IQ
	 */
	public IQ hangup(String callId) throws XmppException {
		
		return hangup(callId, new HangupCommand(null));
	}
	
	
	/**
	 * Hangs up the specified call id
	 * 
	 * @param command Hangup command
	 * @return IQ Resulting IQ
	 */
	public IQ hangup(String callId, HangupCommand command) throws XmppException {
		
		IQ iq = new IQ(IQ.Type.set)
			.setFrom(buildFrom())
			.setTo(buildTo(callId))
			.setChild(Extension.create(command));
		return sendIQ(iq);
	}

	public IQ unjoin(String from, JoinDestinationType type, String callId) throws XmppException {
		
		UnjoinCommand unjoin = new UnjoinCommand();
		unjoin.setFrom(from);
		unjoin.setType(type);
		
		return command(unjoin,callId);
	}

	public IQ join(String to, String media, String direction, JoinDestinationType type, String callId) throws XmppException {
		
		JoinCommand join = new JoinCommand();
		join.setTo(to);
		join.setDirection(Joinable.Direction.DUPLEX);
		join.setMedia(JoinType.BRIDGE);
		join.setType(type);
		
		return command(join,callId);
	}
	
	public IQ join(JoinCommand join, String callId) throws XmppException {
		
		return command(join,callId);
	}
	
	public IQ dtmf(String tones, String callId) throws XmppException {
		
		DtmfCommand dtmf = new DtmfCommand(tones);
		return command(dtmf, callId);
	}
	
	public IQ command(CallCommand command, String callId) throws XmppException {
        IQ iq = new IQ(IQ.Type.set)
            .setFrom(buildFrom())
            .setTo(buildTo(callId))
            .setChild(Extension.create(command));
        return sendIQ(iq);
	}
	
	public VerbRef dial(DialCommand command) throws XmppException {
        
		IQ iq = new IQ(IQ.Type.set)
            .setFrom(buildFrom())
            .setTo(connection.getServiceName()) // dials only use service name
            .setChild(Extension.create(command));
        VerbRef ref = sendAndGetRef(null, iq);
        return ref;
	}
	
	/**
	 * Redirects an existing call to the given URI
	 * 
	 * @param uri URI for redirecting the call to
	 * @param callId Id of the call to redirect
	 */
	public IQ redirect(URI uri, String callId) throws XmppException {
		
		RedirectCommand redirect = new RedirectCommand();
		redirect.setTo(uri);
		return redirect(redirect, callId);
	}
	
	
	/**
	 * Redirects an existing call
	 * 
	 * @param command Redirect command
	 * @param callId Id of the call to redirect
	 * @return IQ Resulting IQ
	 */
	public IQ redirect(RedirectCommand command, String callId) throws XmppException {
		
		return command(command, callId);
	}
	
	protected IQ sendIQ(IQ iq) throws XmppException {
		
		return (IQ)connection.sendAndWait(iq);
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
