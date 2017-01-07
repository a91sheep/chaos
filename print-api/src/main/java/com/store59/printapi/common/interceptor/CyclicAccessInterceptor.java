package com.store59.printapi.common.interceptor;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.CommonException;

@Component
public class CyclicAccessInterceptor extends HandlerInterceptorAdapter {
    @Value("${cookie.max.age}")
    private int cookieMaxAge;

    private int hours=12;//定义session过期时间
	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOpsCache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getRequestURI().indexOf(CommonConstant.DORMSHOP_INI_URI)>=0) {
			dormShopInterceptor(request);
		}else{
			CommonInterceptor(request,request.getRequestURI());
		}
		return super.preHandle(request, response, handler);
	}
	/**
	 * 添加防刷开始的标志位，因为HTTP的无状态
	 * @param request
	 */
	private void dormShopInterceptor(HttpServletRequest request){
		HttpSession session=request.getSession(true);
    	if(valueOpsCache.get(session.getId()+CommonConstant.MESSAGE_URI)==null){
    		valueOpsCache.set(session.getId()+CommonConstant.MESSAGE_URI,
    				CommonConstant.MESSAGE_TIME_INI,hours,
                    TimeUnit.HOURS);
    	}
    	if(valueOpsCache.get(session.getId()+CommonConstant.PDF_UPLOAD_URI)==null){
    		valueOpsCache.set(session.getId()+CommonConstant.PDF_UPLOAD_URI,
    				CommonConstant.MESSAGE_TIME_INI,hours,
    				TimeUnit.HOURS);
    	}
	}
	/**
	 * 短信,文件上传拦截防刷通用判断
	 * @param request
	 */
	private void CommonInterceptor(HttpServletRequest request,String uri) {
		// if(request.getAttribute("Referer")!=null
		// &&request.getAttribute("Referer").equals(CommonConstant.MESSAGE_DOMAIN)){
		String contextPath=request.getContextPath();
		uri=uri.substring(uri.indexOf(contextPath)+contextPath.length());
		String key = request.getSession().getId()+uri;
		String preTime = valueOpsCache.get(key);
		System.out.println(preTime);
		if (preTime != null && preTime.equals(CommonConstant.MESSAGE_TIME_INI)) {
			valueOpsCache.set(key, String.valueOf(System.currentTimeMillis()),hours,
            TimeUnit.HOURS);
		} else {
			if (preTime == null
					|| System.currentTimeMillis() - Long.parseLong(preTime) <= CommonConstant.MESSAGE_TIME_GAP) {
				throw new CommonException("非法操作");
			} else {
				valueOpsCache.set(key, String.valueOf(System.currentTimeMillis()),hours,
	                    TimeUnit.HOURS);
			}
		}
		// }else{
		// throw new CommonException("非法操作");
		// }
	}
}
