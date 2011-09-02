# Rayo Java Client Library

This client library lets you build Rayo applications in Java. It is a very lightweight library that lets you create Rayo applications with very few lines of code. 

## Samples

Feel free to check out the samples available [here](https://github.com/rayo/rayo-java-client/tree/master/src/main/java/samples). You can run any of these samples from the command line with Maven. Here is an example:

        mvn clean compile
        mvn exec:java -Dexec.mainClass="samples.TextSaySample" -Dexec.classpathScope=compile -Ddetail=true

Every sample works in a similar way. First the sample will connect to the Rayo server using some username and password (tipically "usera" and "1"). The Rayo Server should be listening in localhost:5222 although that can be changed easily. Every sample will then wait for an incoming call (Offer Event). So after launching the sample you should open your favorite soft phone and call your Rayo Server. After placing the call the sample will receive the Offer and will proceed to execute the code. 

Some samples may behave a bit different depending on what they are trying to achieve. So for example the Dial samples will place outgoing calls to your soft phones instead of waiting for incoming calls, the conference samples may dial your soft phones and join them together, etc. But the overall behavior is pretty similar and very simple to learn.
 
**Requirements**

The Rayo Java Client library depends on [Rayo](http://www.github.com/rayo/rayo-server). If you try to run the samples or you try to build the Jar file then you will need to have the latest Rayo jars in your repository. So, you may need to grab a copy of Rayo and [build it](https://github.com/rayo/rayo-server/wiki/Building-Rayo-From-Source) first.

## Using the Rayo client library 

The Rayo Java Client implements a low-level XMPP framework that gives you fine grained access to the XMPP connection and all the messages and events. However, the easiest way to get started is to use an instance of [RayoClient](https://github.com/rayo/rayo-java-client/blob/master/src/main/java/com/voxeo/rayo/client/RayoClient.java) which is a high-level abstraction that lets you interact with the Rayo Server through a set of very useful and meaningful methods. Here is an example:

	RayoClient rayo = new RayoClient("localhost"); // Prepares the client instance
	rayo.connect("userc","1","voxeo"); // connects and logs in
	rayo.waitFor("offer"); // Waits for an incoming call
	rayo.answer(); // Answers the call
	rayo.say("Hello"); // Says hello

	// You can also play audio
	VerbRef say = rayo.say("http://somemp3.mp3");
	say.pause(); // and pause it
	say.resume(); // and resume it

	// And finally disconnect
	rayo.disconnect();

Most of the samples available in the [samples source folder](https://github.com/rayo/rayo-java-client/tree/master/src/main/java/samples) use the [RayoClient](https://github.com/rayo/rayo-server/blob/master/rayo-java-client/src/main/java/com/voxeo/rayo/client/RayoClient.java) class. This is for example part of the source code of the [Conference Sample](https://github.com/rayo/rayo-java-client/tree/master/src/main/java/samples):

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		client.conference("123456");
		Thread.sleep(60000);
		client.hangup();
	}


Feel free to look at the samples source code and the RayoClient class as there is tons of available methods. Right now the Rayo client implements the full [Rayo Specification](https://github.com/rayo/rayo-server/wiki/Rayo-specification), all the [Tropo Rayo Extensions](https://github.com/rayo/rayo-server/wiki/Tropo-rayo-extensions) and all the [experimental extensions on the wiki](https://github.com/rayo/rayo-server/wiki).


## Using the low level access libraries

RayoClient is an abstraction class that lets you to quickly create Rayo applications. However, when you need more control over your interaction with an Rayo server you can use any implementation of the [XmppConnection](https://github.com/rayo/rayo-java-client/blob/master/src/main/java/com/voxeo/rayo/client/XmppConnection.java) interface. 

XmppConnection instances let you can manage the connection with the Rayo server. You can listen and react to all type of events and stanzas. You can manage the authentication process. You can create your own IQ stanzas and send it to the server. You can send stanzas synchronously or asynchronously or register callbacks for your messages. 

There is lots of stuff. This documentation will be improved in the future but right now we recommend you to start looking at the [tons of different examples available as unit tests](https://github.com/rayo/rayo-java-client/tree/master/src/test/java/com/voxeo/rayo/client/test) that show how the API can be used. 

