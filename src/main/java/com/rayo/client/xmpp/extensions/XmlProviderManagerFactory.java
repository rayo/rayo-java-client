package com.rayo.client.xmpp.extensions;

import java.util.ArrayList;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.rayo.client.xml.providers.RayoClientProvider;
import com.rayo.core.validation.Validator;
import com.rayo.core.xml.XmlProviderManager;

public class XmlProviderManagerFactory {

	public static XmlProviderManager buildXmlProvider() {
		
		ClassPathResource res = new ClassPathResource("rayo-providers.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		XmlProviderManager manager = (XmlProviderManager)factory.getBean("xmlProviderManager");
		Validator validator = (Validator)factory.getBean("validator");
		
		RayoClientProvider rayoClientProvider = new RayoClientProvider();
		rayoClientProvider.setNamespaces(new ArrayList<String>());
		rayoClientProvider.getNamespaces().add("urn:xmpp:rayo:1");
		rayoClientProvider.getNamespaces().add("jabber:client");
		rayoClientProvider.setValidator(validator);
		rayoClientProvider.setClasses(new ArrayList<Class<?>>());
		manager.register(rayoClientProvider);
		
		return manager;
	}
}
