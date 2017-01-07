package com.store59.printapi.common.http;

import com.store59.kylin.common.exception.BaseException;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.model.StreamBody;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
@Component
public class HttpClientRequest {
	/**
	 * 连接池管理器
	 */
	private HttpClientConnectionManager connectionManager;
	/**
	 * 闲置连接监测线程
	 */
	private IdleConnectionMonitorThread idleConnectionMonitorThread;

	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 100;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 50;
	/**
	 * 非阻塞输入/输出操作的套接字超时时间（10秒）
	 */
	public final static int SO_TIMEOUT = 10000;
	/**
	 * 请求连接管理器的连接时的超时时间（10秒）
	 */
	private final static int CONNECTION_REQUEST_TIMEOUT = 10000;
	/**
	 * 连接请求发出直到建立连接的超时时间（10秒）
	 */
	private final static int CONNECT_TIMEOUT = 10000;
	/**
	 * 连接建立后等待数据的超时时间，或者说，在连续的数据包之间的最大的闲置时间（10秒）
	 */
	private final static int SOCKET_TIMEOUT = 10000;
	/**
	 * 连接空闲超时时间（一旦空闲时间达到设置值，该连接将被关闭）（30秒）
	 */
	private final static int IDLE_TIMEOUT = 30000;

	private int connectionRequestTimeout;
	private int connectTimeout;
	private int socketTimeout;
	private int idleTimeout;

	private static final Logger log = LoggerFactory.getLogger(HttpClientRequest.class);

	/**
	 * 默认构造方法
	 */
	public HttpClientRequest() {
		this(MAX_TOTAL_CONNECTIONS, MAX_ROUTE_CONNECTIONS, SO_TIMEOUT, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
				SOCKET_TIMEOUT, IDLE_TIMEOUT);
	}

	/**
	 * 带参数的构造方法
	 */
	public HttpClientRequest(int maxTotalConnections, int maxRouteConnections, int soTimeout,
			int connectionRequestTimeout, int connectTimeout, int socketTimeout, int idleTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
		this.connectTimeout = connectTimeout;
		this.socketTimeout = socketTimeout;
		this.idleTimeout = idleTimeout;
		createHttpClientConnectionManager(maxTotalConnections, maxRouteConnections, soTimeout);
	}

	public String conn(String url, int httpMethod, List<NameValuePair> params) {
		// HTTP客户端
		CloseableHttpClient httpClient = null;
		// HTTP请求
		HttpUriRequest httpUriRequest = null;
		// 应答内容
		String result = null;

		try {
			// HTTP客户端取得
			httpClient = getHttpClient();

			// 超时设置
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
					.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();

			// 根据HTTP请求方法进行相应的处理
			switch (httpMethod) {
			case CommonConstant.HTTP_METHOD_GET: // GET
				httpUriRequest = doGet(url, requestConfig, params);
				break;
			case CommonConstant.HTTP_METHOD_POST: // POST
				httpUriRequest = doPost(url, requestConfig, params);
				break;
			// case HTTP_METHOD_PUT: // PUT
			// break;
			// case HTTP_METHOD_PATCH: // PATCH
			// break;
			// case HTTP_METHOD_DELETE:// DELETE
			// break;
			default: // 默认为GET
				httpUriRequest = doGet(url, requestConfig, params);
				break;
			}

			// 请求头部设置
			httpUriRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=\"UTF-8\"");

			// 响应处理器
			BasicResponseHandler responseHandler = new BasicResponseHandler();

			// 执行请求并取得结果字符串，然后释放连接使其能够被连接池管理器回收
			result = httpClient.execute(httpUriRequest, responseHandler);
		} catch (Exception e) {
			// 发生异常时直接终止本次请求
			if (httpUriRequest != null) {
				httpUriRequest.abort();
			}
			throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
					CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
		} finally {

		}

		return result;
	}

	public void destroy() {
		log.info("☆☆☆destroy☆☆☆");
		idleConnectionMonitorThread.shutdown();
		connectionManager.shutdown();
	}

	private void createHttpClientConnectionManager(int maxTotalConnections, int maxRouteConnections, int soTimeout) {
		// 设置register
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		ConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();

		// TODO:由于需要证书，所以在需要SSL方式访问的时候再修改
		// KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		// FileInputStream instream = new FileInputStream(new
		// File("filepath"));//加载本地的证书进行https加密传输 TODO:filepath需要修改
		// try {
		// keyStore.load(instream, "password".toCharArray());//设置证书密码
		// TODO:password需要修改
		// } finally {
		// instream.close();
		// }
		//
		// // Trust own CA and all self-signed certs
		// SSLContext sslcontext = SSLContexts.custom()
		// .loadKeyMaterial(keyStore, "password".toCharArray()) //
		// TODO:filepath需要修改
		// .build();
		// // Allow TLSv1 protocol only
		// sslsf = new SSLConnectionSocketFactory(sslcontext,
		// new String[]{"TLSv1"},
		// null,
		// SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", plainsf).register("https", sslsf).build();
		// 创建连接池管理器
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
		// 设置最大连接数
		cm.setMaxTotal(maxTotalConnections);
		// 设置每个路由最大连接数
		cm.setDefaultMaxPerRoute(maxRouteConnections);
		// TODO:根据实际业务需要调整特定路由的最大连接数的情况，请在这里设置，设置如下例所示
		/*
		 * 例子 // 增加路由localhost:80的最大连接数至50 HttpHost localhost = new
		 * HttpHost("locahost", 80); cm.setMaxPerRoute(new HttpRoute(localhost),
		 * 50);
		 */
		// 设置超时时间
		SocketConfig.Builder scBuilder = SocketConfig.custom();
		scBuilder.setSoTimeout(soTimeout);
		cm.setDefaultSocketConfig(scBuilder.build());

		// 连接池管理器
		connectionManager = cm;

		// 用来关闭闲置连接的线程
		idleConnectionMonitorThread = new IdleConnectionMonitorThread(connectionManager, idleTimeout);
		idleConnectionMonitorThread.start();
	}

	/**
	 * 闲置连接监测线程
	 *
	 * @author xjp-59Store
	 */
	public static class IdleConnectionMonitorThread extends Thread {

		private final HttpClientConnectionManager connMgr;
		private final int idleTimeout;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr, int idleTimeout) {
			super();
			this.connMgr = connMgr;
			this.idleTimeout = idleTimeout;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(5000);
						// 关闭过期的连接
						connMgr.closeExpiredConnections();
						// 关闭空闲30秒以上的连接
						connMgr.closeIdleConnections(idleTimeout, TimeUnit.MILLISECONDS);
					}
				}
			} catch (InterruptedException ex) {
				// terminate
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}

	private CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}

	private HttpUriRequest doGet(String url, RequestConfig requestConfig, List<NameValuePair> params) {
		// url编辑（加请求参数）
		StringBuilder urlWork = new StringBuilder().append(url);
		int index = 0;
		for (NameValuePair param : params) {
			if (index == 0) {
				urlWork.append("?");
			} else {
				urlWork.append("&");
			}
			index++;
			urlWork.append(param.getName()).append("=").append(param.getValue());
		}

		// 设置请求URL
		HttpGet httpGet = new HttpGet(urlWork.toString());

		// 配置信息设置
		httpGet.setConfig(requestConfig);

		// 返回HTTP请求对象
		return httpGet;
	}

	private HttpUriRequest doPost(String url, RequestConfig requestConfig, List<NameValuePair> params)
			throws UnsupportedEncodingException {
		// 设置请求URL
		HttpPost httpPost = new HttpPost(url);

		// 设置请求参数
		if (params != null && params.size() > 0) {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		}

		// 配置信息设置
		httpPost.setConfig(requestConfig);

		// 返回HTTP请求对象
		return httpPost;
	}

	public String UploadFilePost(String url, MultipartFile multipartFile, String ext) {
		String retStr = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			StreamBody streamBody = new StreamBody(multipartFile.getOriginalFilename(), multipartFile.getBytes());
			StringBody stringBody = new StringBody(ext, ContentType.MULTIPART_FORM_DATA);
			// 以浏览器兼容模式运行，防止文件名乱码。
			HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("file", streamBody).addPart("ext", stringBody).setCharset(CharsetUtils.get("UTF-8"))
					.build();

			httpPost.setEntity(reqEntity);

			System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());
			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				// 打印响应状态
				System.out.println(response.getStatusLine());
				// 获取响应对象
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					// 打印响应长度
					System.out.println("Response content length: " + resEntity.getContentLength());
					// 打印响应内容
					retStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
					System.out.println(retStr);
				}
				// 销毁
				EntityUtils.consume(resEntity);
				return retStr;
			} finally {
				response.close();
				// convFile.delete();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retStr;
	}

	public String UploadFilePost_IOS(String url, MultipartFile multipartFile, String ext, String FileName) {
		String retStr = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			StreamBody streamBody = new StreamBody(FileName, multipartFile.getBytes());
			StringBody stringBody = new StringBody(ext, ContentType.MULTIPART_FORM_DATA);
			// 以浏览器兼容模式运行，防止文件名乱码。
			HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("file", streamBody).addPart("ext", stringBody).setCharset(CharsetUtils.get("UTF-8"))
					.build();

			httpPost.setEntity(reqEntity);

			System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());
			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				// 打印响应状态
				System.out.println(response.getStatusLine());
				// 获取响应对象
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					// 打印响应长度
					System.out.println("Response content length: " + resEntity.getContentLength());
					// 打印响应内容
					retStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
					System.out.println(retStr);
				}
				// 销毁
				EntityUtils.consume(resEntity);
				return retStr;
			} finally {
				response.close();
				// convFile.delete();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retStr;
	}
	/**
	 * 文档上传改造
	 * @param url 服务器地址
	 * @param filePath oss文件路径
	 */
	public String UploadFilePost(String url, String filePath) {
		String retStr = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			StringBody stringBody1 = new StringBody(filePath, ContentType.MULTIPART_FORM_DATA);
			// 以浏览器兼容模式运行，防止文件名乱码。
			HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("in_url", stringBody1).setCharset(CharsetUtils.get("UTF-8")).build();

			httpPost.setEntity(reqEntity);

			System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());
			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				// 打印响应状态
				System.out.println(response.getStatusLine());
				// 获取响应对象
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					// 打印响应长度
					System.out.println("Response content length: " + resEntity.getContentLength());
					// 打印响应内容
					retStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
				}
				// 销毁
				EntityUtils.consume(resEntity);
				return retStr;
			} finally {
				response.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retStr;
	}
}