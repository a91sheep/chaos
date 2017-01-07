package com.store59.pay.service.wangshang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsMain {
    /**
     * 是否开启验签
     */
    public static final boolean isSign = true;
    
    /**
     * 渠道
     */
    public static final String channel = "MYBANK";
    
    /**
     * 联调环境url
     */
//    public static final String  reqUrl = "https://fcsupergw.dev.dl.alipaydev.com/open/api/common/request.htm";
//    public static final String BATCH_CONFIRM_URL = "https://fcopen.mybank.cn/open/api/request.htm";
    //sit环境url
//    public static final String BATCH_CONFIRM_URL = "https://fcsupergw.dl.alipaydev.com/open/api/common/request.htm";
    //    https://fcsupergw.dl.alipaydev.com/open/api/common/request.htm
    /**
     * 
     */
    public static String httpsReq(String reqUrl, String param) throws NoSuchAlgorithmException,
                                                               NoSuchProviderException,
                                                               IOException, KeyManagementException {
        URL url = new URL(reqUrl);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        httpsConn.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String paramString, SSLSession paramSSLSession) {
                return true;
            }
        });

        //创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());

        //从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        //创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
        httpsConn.setSSLSocketFactory(ssf);

        httpsConn.setDoOutput(true);
        httpsConn.setRequestMethod("POST");
        httpsConn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
        httpsConn.setRequestProperty("Content-Length", String.valueOf(param.length()));

        OutputStreamWriter out = new OutputStreamWriter(httpsConn.getOutputStream(), "UTF-8");
        out.write(param);
        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
            httpsConn.getInputStream(), "UTF-8"));
        String tempLine = "";
        StringBuffer resultBuffer = new StringBuffer();
        while ((tempLine = reader.readLine()) != null) {
            resultBuffer.append(tempLine).append(System.getProperty("line.separator"));
        }
        return resultBuffer.toString();
    }

}
