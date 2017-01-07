package com.store59.printapi.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.store59.printapi.common.constant.CommonConstant;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private RequestCommonInterceptor requestCommonInterceptor;
    @Autowired
    private AppLoginInterceptor appLoginInterceptor;
    @Autowired
    private CyclicAccessInterceptor cyclicAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestCommonInterceptor).addPathPatterns("/**");
        //登录拦截器只需拦截我的订单,取消订单的请求即可
        registry.addInterceptor(loginInterceptor).addPathPatterns("/order/list")
                .addPathPatterns("/order/cancel/**");
        //app
//        registry.addInterceptor(appLoginInterceptor).addPathPatterns("/print/order/list")
//                .addPathPatterns("/print/order/info")
//                .addPathPatterns("/print/order/cancel/**")
//                .addPathPatterns("/print/order/create");
        registry.addInterceptor(appLoginInterceptor).addPathPatterns("/print/order/cancel/**")
                .addPathPatterns("/print/order/create");

//        registry.addInterceptor(cyclicAccessInterceptor).addPathPatterns(CommonConstant.DORMSHOP_INI_URI)
//		.addPathPatterns(CommonConstant.MESSAGE_URI)
//		.addPathPatterns(CommonConstant.PDF_UPLOAD_URI);
    }

}
