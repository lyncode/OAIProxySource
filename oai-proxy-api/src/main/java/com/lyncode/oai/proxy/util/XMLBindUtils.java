package com.lyncode.oai.proxy.util;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;

public class XMLBindUtils {
	public static void marshal (Object obj, OutputStream out) throws MarshallingException {
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass().getPackage().getName());
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(obj, out);
			
		} catch (JAXBException ex) {
			throw new MarshallingException(ex);
		}
	}
	
	public static Object unmarshal (Class<?> c, InputStream in) throws MarshallingException {
		try {
			JAXBContext context = JAXBContext.newInstance(c.getPackage().getName());
			
			Unmarshaller unmarshal = context.createUnmarshaller();
			return unmarshal.unmarshal(in);
			
		} catch (JAXBException ex) {
			throw new MarshallingException(ex);
		}
	}
}
