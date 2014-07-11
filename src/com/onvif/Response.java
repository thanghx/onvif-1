package com.onvif;


import org.w3._2003._05.soap_envelope.Body;
import org.w3._2003._05.soap_envelope.Envelope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

/**
 * Created by calc on 10.07.14.
 *
 */
public class Response<E> {
    //Class<E> type;
    private Class type;

    /**
     *
     * @param type Class Class of object (Object.class, object.getClass())
     */
    public Response(Class type) {
        this.type = type;
    }

    public E getResponse(String xml){
        if(xml.equals("")) return null;

        try {
            JAXBContext jc  = JAXBContext.newInstance(
                    Envelope.class,
                    org.xmlsoap.schemas.soap.envelope.Envelope.class,
                    Body.class,
                    type);
            Unmarshaller u = jc.createUnmarshaller();

            Source s = new StreamSource(new StringReader(xml));
            //JAXBElement<Envelope> root = u.unmarshal(s, Envelope.class);
            JAXBElement root = (JAXBElement)u.unmarshal(s);

            System.out.println(root.getValue().getClass());
            if(root.getValue().getClass().toString().equals("class org.xmlsoap.schemas.soap.envelope.Envelope")){
                //SOAP 1.1
                org.xmlsoap.schemas.soap.envelope.Envelope e = (org.xmlsoap.schemas.soap.envelope.Envelope)root.getValue();
                return (E) e.getBody().getAny().get(0);
            }
            else{
                //SOAP 1.2
                Envelope e = (Envelope)root.getValue();
                return (E) e.getBody().getAny().get(0);
            }

        } catch (JAXBException e) {
            e.printStackTrace();

            return null;
        }
    }
}
