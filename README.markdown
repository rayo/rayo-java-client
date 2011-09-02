# Rayo Java Client Library

This client library lets you build Rayo applications in Java. It is a very lightweight library that lets you create Rayo applications with very few lines of code. 

## Samples
https://github.com/rayo/rayo-java-client/tree/master/src/main/java/samples
Feel free to check out the samples available [here](https://github.com/rayo/rayo-java-client/tree/master/src/main/java/samples). You can run any of these samples from the command line with Maven. Here is an example:

        mvn clean compile
        mvn exec:java -Dexec.mainClass="samples.TextSaySample" -Dexec.classpathScope=compile -Ddetail=true

All the samples try to connect with and username "userc" and a password "1" to a Prism instance in localhost:5222. You can change the source code if you need it. All the samples will wait for an incoming offer, so after running the command line from above you should open your favorite sip phone and call your Prism instance. After doing this the rest of the sample will process the incoming offer and execute the code.   
 
**Troubleshooting**

The Rayo Java Client library depends on [Rayo](http://www.github.com/rayo/rayo-server). If you try to run the samples or you try to build the Jar file then you will need to have the latest Rayo jars in your repository. So, you may need to grab a copy of Rayo and run mvn clean install first.

## Using the Rayo client library 

The easiest way to use the Rayo's Java Client library is to create an instance of the [RayoClient](https://github.com/rayo/rayo-server/blob/master/rayo-java-client/src/main/java/com/voxeo/rayo/client/RayoClient.java) class. This class lets you interact with the Rayo Server through a set of very useful and simple methods. Here is an example:

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

All the samples available in the [samples source folder](https://github.com/rayo/rayo-java-client/tree/master/src/main/java/samples) use the [RayoClient](https://github.com/rayo/rayo-server/blob/master/rayo-java-client/src/main/java/com/voxeo/rayo/client/RayoClient.java) class. This is for example part of the source code of the [Conference Sample](https://github.com/rayo/rayo-java-client/tree/master/src/main/java/samples):

	public void run() throws Exception {
		
		client.waitFor("offer");
		client.answer();
		client.conference("123456");
		Thread.sleep(60000);
		client.hangup();
	}


Feel free to look at the samples source code and the RayoClient class as there is tons of available methods. Right now the following verbs are implemented:

	* Say
	* Ask
	* Conference
	* Transfer
	* Dial

## Using the low level access libraries

RayoClient is an abstraction class that lets you to quickly create Rayo applications. However, when you need more control over your interaction with an Rayo server it's much better to use some implementation of [XmppConnection](https://github.com/rayo/rayo-server/blob/master/rayo-java-client/src/main/java/com/voxeo/rayo/client/SimpleXmppConnection.java). 

XmppConnection instances let you can manage the connection with the Rayo server. You can listen and react to all type of events and stanzas. You can manage the authentication process. You can create your own IQ stanzas and send it to the server. You can send stanzas synchronously or asynchronously or register callbacks for your messages. 

There is lots of stuff. This documentation will be improved in the future but right now we recommend you to start looking at the [tons of different examples available as unit tests](https://github.com/rayo/rayo-java-client/tree/master/src/test/java/com/voxeo/rayo/client/test) that show how the API can be used. 

