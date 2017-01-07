package com.store59.printapi.common.interceptor;

import com.store59.kylin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.Collection;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
@Component
public class RequestCommonInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(RequestCommonInterceptor.class);

    /**
     * 在执行Controller里面的处理逻辑之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 打印access信息
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod());
        sb.append(" ");
        sb.append(request.getRequestURI());
        String query = request.getQueryString();
        if (query != null && query.length() > 0) {
            sb.append("?");
            sb.append(query);
        }
        if (log.isInfoEnabled()) {
            log.info(sb.toString());
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 在执行Controller里面的逻辑后返回视图之前执行（Controller里产生异常的话就不执行）
     */
    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        // TODO:自定义处理
    }

    /**
     * 在Controller返回视图后执行（Controller里即使产生异常也执行）
     */
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);

        // TODO:自定义处理

    }

    /**
     * 会在Controller方法异步执行时开始执行
     */
    @Override
    public void afterConcurrentHandlingStarted(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);

        // TODO:自定义处理

    }
}
