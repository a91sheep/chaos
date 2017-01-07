/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.oxm;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

/**
 * JAXB2映射器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月12日
 * @since 1.0
 */
public class Jaxb2Mapper {

    private final Map<Class<?>, JAXBContext> jaxbContexts = new ConcurrentReferenceHashMap<Class<?>, JAXBContext>(64);

    /**
     * 将XML格式的字符串转换为对象.
     * 
     * @param xml XML格式的字符串
     * @param clazz 转换后的类型
     * @return 转换后对象
     * @throws XmlMappingException in case of JAXB errors
     */
    public <T> T unmarshal(String xml, Class<T> clazz) {
        try {
            Unmarshaller unmarshaller = createUnmarshaller(clazz);
            JAXBElement<T> jaxbElement = unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), clazz);
            return jaxbElement.getValue();
        } catch (UnmarshalException ex) {
            throw new XmlMappingException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);
        } catch (JAXBException ex) {
            throw new XmlMappingException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
        }
    }

    /**
     * 将对象转换为XML格式的字符串(使用默认编码).
     * 
     * @param instance 需要转换的对象
     * @return XML格式字符串
     * @throws XmlMappingException in case of JAXB errors
     */
    public String marshal(Object instance) {
        return marshal(instance, null);
    }

    /**
     * 将对象转换为XML格式的字符串.
     * 
     * @param instance 需要转换的对象
     * @param charset 字符编码
     * @return XML格式字符串
     * @throws XmlMappingException in case of JAXB errors
     */
    public String marshal(Object instance, Charset charset) {
        try {
            Class<?> clazz = ClassUtils.getUserClass(instance);
            Marshaller marshaller = createMarshaller(clazz);
            setCharset(marshaller, charset);
            Writer writer = new StringWriter();
            marshaller.marshal(instance, new StreamResult(writer));
            return writer.toString();
        } catch (MarshalException ex) {
            throw new XmlMappingException("Could not marshal [" + instance + "]: " + ex.getMessage(), ex);
        } catch (JAXBException ex) {
            throw new XmlMappingException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
        }
    }

    /**
     * Creates a new {@link Marshaller} for the given class.
     *
     * @param clazz the class to create the marshaller for
     * @return the {@code Marshaller}
     * @throws XmlMappingException in case of JAXB errors
     */
    protected Marshaller createMarshaller(Class<?> clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createMarshaller();
        } catch (JAXBException ex) {
            throw new XmlMappingException("Could not create Marshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
        }
    }

    /**
     * Creates a new {@link Unmarshaller} for the given class.
     *
     * @param clazz the class to create the unmarshaller for
     * @return the {@code Unmarshaller}
     * @throws XmlMappingException in case of JAXB errors
     */
    protected Unmarshaller createUnmarshaller(Class<?> clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException ex) {
            throw new XmlMappingException("Could not create Unmarshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
        }
    }

    /**
     * Returns a {@link JAXBContext} for the given class.
     *
     * @param clazz the class to return the context for
     * @return the {@code JAXBContext}
     * @throws XmlMappingException in case of JAXB errors
     */
    protected JAXBContext getJaxbContext(Class<?> clazz) {
        Assert.notNull(clazz, "'clazz' must not be null");
        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz);
                jaxbContexts.put(clazz, jaxbContext);
            } catch (JAXBException ex) {
                throw new XmlMappingException("Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        }
        return jaxbContext;
    }

    private void setCharset(Marshaller marshaller, Charset charset) throws PropertyException {
        if (charset != null) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, charset.name());
        }
    }

}
