/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.lang;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月10日
 * @since 1.0
 */
public abstract class XmlUtils {

    /**
     * 将XML字符串解析为Map.
     * 
     * <pre>
     * 只支持简单的XML格式，如：
     * &lt;xml&gt;
     *   &lt;out_trade_no&gt;<![CDATA[1409811653]]>&lt;/out_trade_no&gt;
     *   &lt;sign&gt;![CDATA[B552ED6B279343CB493C5DD0D78AB241]]>&lt;/sign&gt;
     *   &lt;total_fee&gt;1&lt;/total_fee&gt;
     * &lt;/xml&gt;
     * </pre>
     * 
     * @param xml XML字符串
     * @return Map<String, String>
     */
    public static Map<String, String> parse2Map(String xml) {
        if (StringUtils.isBlank(xml)) {
            return new HashMap<>();
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(IOUtils.toInputStream(xml));
            NodeList allNodes = document.getFirstChild().getChildNodes();

            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < allNodes.getLength(); i++) {
                Node node = allNodes.item(i);
                if (node instanceof Element) {
                    map.put(node.getNodeName(), node.getTextContent());
                }
            }

            return map;
        } catch (Exception e) {
            throw new RuntimeException("将XML字符串解析为Map异常：xml=" + xml, e);
        }
    }

}
