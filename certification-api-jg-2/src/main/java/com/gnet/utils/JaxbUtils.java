package com.gnet.utils;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.google.common.base.Throwables;
import com.jfinal.log.Logger;

/**
 * 
 * Xml object互转. <br/>
 * 
 */
public class JaxbUtils {

    protected final static Logger LOG = Logger.getLogger(JaxbUtils.class);

    /**
     * 
     * string -> object
     * 
     * @param src
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(String src, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(clazz).createUnmarshaller();
            result = (T) avm.unmarshal(new StringReader(src));
        } catch (JAXBException e) {
            Throwables.propagate(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(File xmlFile, Class<T> clazz) {
        T result = null;
        try {
            Unmarshaller avm = JAXBContext.newInstance(clazz).createUnmarshaller();
            result = (T) avm.unmarshal(xmlFile);
        } catch (JAXBException e) {
            Throwables.propagate(e);
        }
        return result;
    }

    /**
     * 
     * object -> string
     * 
     * @author kid create 2013-4-1
     * @param src
     * @param clazz
     * @return
     */
    public static String marshal(Object jaxbElement) {
        StringWriter sw = null;
        try {
            Marshaller fm = JAXBContext.newInstance(jaxbElement.getClass()).createMarshaller();
            fm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            sw = new StringWriter();
            fm.marshal(jaxbElement, sw);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return sw.toString();
    }
}
