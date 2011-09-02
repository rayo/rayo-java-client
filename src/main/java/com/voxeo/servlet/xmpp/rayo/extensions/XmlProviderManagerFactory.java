package com.voxeo.servlet.xmpp.rayo.extensions;

import java.util.ArrayList;

import com.rayo.core.validation.Validator;
import com.rayo.core.xml.DefaultXmlProviderManager;
import com.rayo.core.xml.XmlProviderManager;
import com.rayo.core.xml.providers.AskProvider;
import com.rayo.core.xml.providers.ConferenceProvider;
import com.rayo.core.xml.providers.InputProvider;
import com.rayo.core.xml.providers.OutputProvider;
import com.rayo.core.xml.providers.RayoProvider;
import com.rayo.core.xml.providers.RecordProvider;
import com.rayo.core.xml.providers.SayProvider;
import com.rayo.core.xml.providers.TransferProvider;
import com.voxeo.rayo.client.xml.providers.RayoClientProvider;

public class XmlProviderManagerFactory {

	public static XmlProviderManager buildXmlProvider() {
		
		XmlProviderManager manager = new DefaultXmlProviderManager();
		Validator validator = new Validator();
		RayoProvider rayoProvider = new RayoProvider();
		rayoProvider.setNamespaces(new ArrayList<String>());
		rayoProvider.getNamespaces().add("urn:xmpp:rayo:1");
		rayoProvider.getNamespaces().add("urn:xmpp:rayo:ext:1");
		rayoProvider.getNamespaces().add("urn:xmpp:rayo:ext:complete:1");
		rayoProvider.setValidator(validator);
		manager.register(rayoProvider);
		SayProvider sayProvider = new SayProvider();
		sayProvider.setNamespaces(new ArrayList<String>());
		sayProvider.getNamespaces().add("urn:xmpp:tropo:say:1");
		sayProvider.getNamespaces().add("urn:xmpp:tropo:say:complete:1");
		sayProvider.setValidator(validator);
		manager.register(sayProvider);
		AskProvider askProvider = new AskProvider();
		askProvider.setNamespaces(new ArrayList<String>());
		askProvider.getNamespaces().add("urn:xmpp:tropo:ask:1");
		askProvider.getNamespaces().add("urn:xmpp:tropo:ask:complete:1");
		askProvider.setValidator(validator);
		manager.register(askProvider);
		TransferProvider transferProvider = new TransferProvider();
		transferProvider.setNamespaces(new ArrayList<String>());
		transferProvider.getNamespaces().add("urn:xmpp:tropo:transfer:1");
		transferProvider.getNamespaces().add("urn:xmpp:tropo:transfer:complete:1");
		transferProvider.setValidator(validator);
		manager.register(transferProvider);
		ConferenceProvider conferenceProvider = new ConferenceProvider();
		conferenceProvider.setNamespaces(new ArrayList<String>());
		conferenceProvider.getNamespaces().add("urn:xmpp:tropo:conference:1");
		conferenceProvider.getNamespaces().add("urn:xmpp:tropo:conference:complete:1");
		conferenceProvider.setValidator(validator);
		manager.register(conferenceProvider);
		RayoClientProvider rayoClientProvider = new RayoClientProvider();
		rayoClientProvider.setNamespaces(new ArrayList<String>());
		rayoClientProvider.getNamespaces().add("urn:xmpp:rayo:1");
		rayoClientProvider.getNamespaces().add("jabber:client");
		rayoClientProvider.setValidator(validator);
		manager.register(rayoClientProvider);		
		OutputProvider outputProvider = new OutputProvider();
		outputProvider.setNamespaces(new ArrayList<String>());
		outputProvider.getNamespaces().add("urn:xmpp:rayo:output:1");
		outputProvider.getNamespaces().add("urn:xmpp:rayo:output:complete:1");
		outputProvider.setValidator(validator);		
		manager.register(outputProvider);
		InputProvider inputProvider = new InputProvider();
		inputProvider.setNamespaces(new ArrayList<String>());
		inputProvider.getNamespaces().add("urn:xmpp:rayo:input:1");
		inputProvider.getNamespaces().add("urn:xmpp:rayo:input:complete:1");
		inputProvider.setValidator(validator);		
		manager.register(inputProvider);	
		RecordProvider recordProvider = new RecordProvider();
		recordProvider.setNamespaces(new ArrayList<String>());
		recordProvider.getNamespaces().add("urn:xmpp:rayo:record:1");
		recordProvider.getNamespaces().add("urn:xmpp:rayo:record:complete:1");
		recordProvider.setValidator(validator);		
		manager.register(recordProvider);			
		return manager;		
	}
}
