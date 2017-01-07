package com.store59.printapi.controller.wechat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


//@Component
public class TokenManagerListener implements ServletContextListener{

//	@Value("${print.wechat.appid}")
//	private String appid ;
//	@Value("${print.wechat.secret}")
//	private String secret ;
//	@Autowired
//	TokenManager tokenManager;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//WEB容器 初始化时调用
//		tokenManager.init(appid, secret);
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//WEB容器  关闭时调用
//		TokenManager.destroyed();
	}
}
