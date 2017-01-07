package com.store59.pay.service.wangshang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.dom.DOMElement;

public class XmlUtil {

	public static void main(String[] args) throws IOException, DocumentException {
		XmlUtil instance=new XmlUtil();
		String function="ant.ebank.acount.balance.query";
		
		/*Map<String,String> form=new HashMap<String,String>();
		form.put("appid", "2016011800000001");
		form.put("function", function);//余额查询
		form.put("reqTime", new Timestamp(System.currentTimeMillis()).toString());
		form.put("reqMsgId", "211112222233438");
		
		form.put("partner", "202210000000000001025");
		form.put("cardNo", "8888886531660936");
		instance.format(form,function);*/
		
//		String response="<document><response id=\"response\"><head><version>1.0.0</version><appid></appid><function>ant.ebank.acount.balance.query</function><respTime></respTime><respTimeZone>UTC+8</respTimeZone><reqMsgId></reqMsgId><reserve></reserve><signType>RSA</signType><inputCharset>UTF-8</inputCharset></head><body><resultInfo><resultStatus>U</resultStatus><resultCode>9000</resultCode><resultMsg>暂时系统异常</resultMsg></resultInfo><customerType></customerType><openOrg></openOrg><arrangementId></arrangementId><accountId>1234</accountId><cashExCode></cashExCode><openDate></openDate><closeDate></closeDate><termUnit></termUnit><term></term><arrangementChineseName></arrangementChineseName><arrangementEnglishName></arrangementEnglishName><businessType></businessType><accountPurpose></accountPurpose><accountAttribute></accountAttribute><accountStatus></accountStatus><availableBalance></availableBalance><actualBalance></actualBalance><freezeAmount></freezeAmount><currencyCode></currencyCode><creditFreeze></creditFreeze><debtFreeze></debtFreeze></body></response><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></ds:CanonicalizationMethod><ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"></ds:SignatureMethod><ds:Reference URI=\"\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></ds:DigestMethod><ds:DigestValue>0osSCGUBEKqrumihNvsuNA3/e80=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>AhT+SP0SMPHjQaD1v/R4a+U9oMXI9WPT1NIZR/gyhKw7L5YZhieZHKpP/0yXuzeGxeH8LpYUCOUMQUFW2120cdtGnCzhw52ijqtpum5jY53bwYyaCT5Yc3a3nAmzItQqSXje7VK6m01h2Qa4Ewr8XD//1/6QTbgFODWjry7I5Ds=</ds:SignatureValue></ds:Signature></document>";
		String response="<document><response id=\"response\"><head><version>1.0.0</version><appid></appid><function>ant.ebank.acount.balance.query</function><respTime></respTime><respTimeZone>UTC+8</respTimeZone><reqMsgId></reqMsgId><reserve></reserve><signType>RSA</signType><inputCharset>UTF-8</inputCharset></head>"
				+ "<body>"
				+ "<resultInfo><resultStatus>U</resultStatus><resultCode>9000</resultCode><resultMsg>暂时系统异常</resultMsg></resultInfo>"
				+ "<resultInfo><resultStatus>A</resultStatus><resultCode>9000</resultCode><resultMsg>暂时系统异常</resultMsg></resultInfo>"
				+ "<resultInfo><resultStatus>S</resultStatus><resultCode>9000</resultCode><resultMsg>暂时系统异常</resultMsg></resultInfo>"
				+ "<customerType>123</customerType>"
				+ "<customerType>223</customerType>"
				+ "<customerType>323</customerType>"
				+ "<openOrg></openOrg><arrangementId></arrangementId><accountId>1234</accountId><cashExCode></cashExCode><openDate></openDate><closeDate></closeDate><termUnit></termUnit><term></term><arrangementChineseName></arrangementChineseName><arrangementEnglishName></arrangementEnglishName><businessType></businessType><accountPurpose></accountPurpose><accountAttribute></accountAttribute><accountStatus></accountStatus><availableBalance></availableBalance><actualBalance></actualBalance><freezeAmount></freezeAmount><currencyCode></currencyCode><creditFreeze></creditFreeze><debtFreeze></debtFreeze></body></response><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></ds:CanonicalizationMethod><ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"></ds:SignatureMethod><ds:Reference URI=\"\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></ds:DigestMethod><ds:DigestValue>0osSCGUBEKqrumihNvsuNA3/e80=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>AhT+SP0SMPHjQaD1v/R4a+U9oMXI9WPT1NIZR/gyhKw7L5YZhieZHKpP/0yXuzeGxeH8LpYUCOUMQUFW2120cdtGnCzhw52ijqtpum5jY53bwYyaCT5Yc3a3nAmzItQqSXje7VK6m01h2Qa4Ewr8XD//1/6QTbgFODWjry7I5Ds=</ds:SignatureValue></ds:Signature></document>";
		Map<String,Object> map=instance.parse(response,function);
	}
	
	//读取报文格式
	public String sendStyle(String function) throws FileNotFoundException{
      String path = this.getClass().getClassLoader().getResource("").getPath();
      String configFilePath = path + "xml/send/"+function+".xml";
      InputStream is = new FileInputStream(new File(configFilePath));
//		InputStream is=getClass().getResourceAsStream("xml/send/"+function+".xml");
		return getStyle(is);
	}
	//读取报文格式
	public String receiveStyle(String function){
		InputStream is=getClass().getResourceAsStream("xml/receive/"+function+".xml");
		return getStyle(is);
	}
	public String getStyle(InputStream is){
		BufferedReader br=null;
	    StringBuffer sb=new StringBuffer();
		try{
			br=new BufferedReader(new InputStreamReader(is));
	        String s="";
	        while((s=br.readLine())!=null){
	        	sb.append(s);
	        }
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if(br!=null)br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	//封装报文
	public String format(Map<String,String> form,String function) throws DocumentException, FileNotFoundException{
        //报文头
		Element headElement=formatHead(form);
		//报文体
        Element bodyElement=formatBody(form,function);
        
        Document docRes=DocumentHelper.createDocument();
        Element rootElement=docRes.addElement("document");
        Element requestElement=rootElement.addElement("request").addAttribute("id", "request");
        requestElement.add(headElement);
        requestElement.add(bodyElement);
        
        String out=docRes.asXML();
        return out;
	}
	
	private void sign(Document doc){
	    String xml = doc.getRootElement().element("response").asXML();
	    //return RSA.sign(xml, privateKey);
	}
	//封装报文头
	public Element formatHead(Map<String,String> form) throws DocumentException, FileNotFoundException{
		Document docStyle = DocumentHelper.parseText(sendStyle("head"));
        Element headStyle = docStyle.getRootElement();
		List<Element> list = headStyle.elements();
		Element headElement=new DOMElement("head");
		for(int i=0;i<list.size();i++){
			Element e=list.get(i);
			String tagName=e.attributeValue("tagName");
			String value=form.get(tagName);
			if(value==null){
				String defaultValue=e.attributeValue("defaultValue");
				if(defaultValue!=null){
					value=defaultValue;
				}
			}
			if(value!=null){
				headElement.addElement(tagName).setText(value);
			}
		}
		return headElement;
	}
	//封装报文体
	public Element formatBody(Map<String,String> form,String function) throws DocumentException, FileNotFoundException{
        Document docStyle = DocumentHelper.parseText(sendStyle(function));
        Element bodyStyle = docStyle.getRootElement();
		List<Element> list = bodyStyle.elements();
		Element bodyElement=new DOMElement("body");
		for(int i=0;i<list.size();i++){
			Element e=list.get(i);
			String tagName=e.attributeValue("tagName");
			String value=form.get(tagName);
			if(value==null){
				String defaultValue=e.attributeValue("defaultValue");
				if(defaultValue!=null){
					value=defaultValue;
				}
			}
			if(value!=null){
				bodyElement.addElement(tagName).setText(value);
			}
		}
		return bodyElement;
	}
	//封装签名数据
	public Element formatSign(Map<String,String> form){
        Namespace dsNamespace = new Namespace("ds","http://www.w3.org/2000/09/xmldsig#");
        Element signature=new DOMElement(new QName("Signature",dsNamespace));
        Element signatureValue=signature.addElement(new QName("SignatureValue",dsNamespace));
        signatureValue.setText("签名数据");
		return signature;
	}
	
	//解析报文
	public Map<String,Object> parse(String response,String function) throws DocumentException{
		Map<String, Object> returnMap=new HashMap<String,Object>();
		Document docStyle = DocumentHelper.parseText(response);
		List<Element> headList= docStyle.getRootElement().element("response").element("head").elements();
		Map<String,Object> headMap=parseHead(headList);
		
		List<Element> bodyList= docStyle.getRootElement().element("response").element("body").elements();
		Map<String,Object> bodyMap=parseBody(bodyList);
		
		
		returnMap.putAll(headMap);
		returnMap.putAll(bodyMap);
		return returnMap;
	}
	//解析报文头
	private Map<String, Object> parseHead(List<Element> list) throws DocumentException {
		Map<String,Object> headMap=new HashMap<String,Object>();
		for(int i=0;i<list.size();i++){
			Element e=list.get(i);
			if(e.getText()!=null && !"".equals(e.getText()))
				headMap.put(e.getName(), e.getText());
		}
		return headMap;
	}
	//解析报文体
	private Map<String, Object> parseBody(List<Element> list) throws DocumentException {
		Map<String,Object> bodyMap=new HashMap<String,Object>();
		for(int i=0;i<list.size();i++){
			Element e=list.get(i);
			//包含子元素
			if(e.elements().size()!=0){
				//重复标签合并成list
				if(bodyMap.containsKey(e.getName())){
					Object o=bodyMap.get(e.getName());
					if(o instanceof ArrayList){
						ArrayList<Object> oList=(ArrayList<Object>)o;
						oList.add(parseBody(e.elements()));
					}else{
						ArrayList<Object> oList=new ArrayList<Object>();
						oList.add(o);
						oList.add(parseBody(e.elements()));
						bodyMap.put(e.getName(), oList);
					}
				}else{
					bodyMap.put(e.getName(), parseBody(e.elements()));
				}
			//无子元素且非空
			}else if(e.getText()!=null && !"".equals(e.getText())){
				//重复标签合并成list
				if(bodyMap.containsKey(e.getName())){
					Object o=bodyMap.get(e.getName());
					if(o instanceof ArrayList){
						ArrayList<Object> oList=(ArrayList<Object>)o;
						oList.add(e.getText());
					}else{
						ArrayList<Object> oList=new ArrayList<Object>();
						oList.add(o);
						oList.add(e.getText());
						bodyMap.put(e.getName(), oList);
					}
				}else{
					bodyMap.put(e.getName(), e.getText());
				}
			}
		}
		return bodyMap;
	}

	public void addDs() throws IOException, DocumentException{
		InputStream is=XmlUtil.class.getResourceAsStream("originMessage.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        StringBuffer sb=new StringBuffer();
        String s="";
        while((s=br.readLine())!=null){
        	sb.append(s);
        }
        s=sb.toString();
        Document document = DocumentHelper.parseText(s);
        String out=document.asXML();
        
        Element root=document.getRootElement();
        
        Namespace dsNamespace = new Namespace("ds","http://www.w3.org/2000/09/xmldsig#");
        Element signature=root.addElement(new QName("Signature",dsNamespace));
        Element signedInfo=signature.addElement(new QName("SignedInfo",dsNamespace));
        signedInfo.addElement(new QName("CanonicalizationMethod",dsNamespace))
        	.addAttribute("Algorithm", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
        signedInfo.addElement(new QName("SignatureMethod",dsNamespace))
    		.addAttribute("Algorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
        Element reference=signedInfo.addElement(new QName("Reference",dsNamespace));
        reference.addAttribute("URI", "");
        reference.addElement(new QName("Transforms",dsNamespace))
        	.addElement(new QName("Transform",dsNamespace))
        	.addAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#enveloped-signature");
        reference.addElement(new QName("DigestMethod",dsNamespace))
        	.addAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1");
        reference.addElement(new QName("DigestValue",dsNamespace))
        	.setText("MD5值");
        
        Element signatureValue=signature.addElement(new QName("SignatureValue",dsNamespace));
        signatureValue.setText("签名数据");
        
        out=document.asXML();
	}

}
