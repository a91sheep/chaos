/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.store59.pay.service.wangshang;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.security.signature.XMLSignature;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 进行签名验签的测试
 * 
 * @author jin.xie
 * @version $Id: XmlSignTest.java, v 0.1 2016年2月1日 上午10:51:39 jin.xie Exp $
 */
public class XmlSignUtil {
    /**
     * 签名- XML
     * 
     * @throws Exception
     */
    public static String sign(String xmlContent, String rsaPrivateKey) throws Exception {
        Document doc = parseDocumentByString(xmlContent);
        PrivateKey privateKey = SignatureUtils.getPrivateKey(rsaPrivateKey);
        String result = SignatureUtils.signXmlElement(privateKey, doc, "request",
            XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256, 2);
        return result;
    }

    /**
     * 验签 - XML
     * 
     * @throws Exception
     */
    public static boolean verify(String xmlContent, String bankRsaPublicKey) throws Exception {
        Document doc = parseDocumentByString(xmlContent);
        PublicKey publicKey = SignatureUtils.getPublicKey(bankRsaPublicKey);

        return SignatureUtils.verifyXmlElement(publicKey, doc);
    }

    /**
     * 解析XML
     * 
     * @param xmlContent
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private static Document parseDocumentByString(String xmlContent) throws SAXException,
                                                                    IOException, Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);// 否则无法识别namespace
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlContent)));
    }

}