package com.store59.printapi.common.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.store59.printapi.common.constant.Constant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.utils.StringUtil;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
@Component
public class AppLoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> cache;

    /**
     * 在执行Controller里面的处理逻辑之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 调用父类处理
        boolean ret = super.preHandle(request, response, handler);
        if (!ret) {
            return false;
        }

        String token = request.getParameter("token");
        String info = cache.get(Constant.KEY_REDIS_TOKEN_PREFIX + token);
        if (StringUtil.isBlank(info)) {
            throw new AppException(Constant.STATUS_TOKEN_INVALID, Constant.MSG_TOKEN_INVALID);
        }
//        TokenInfo tokenInfo = JsonUtil.getObjectFromJson(cache.get(Constant.KEY_REDIS_TOKEN_PREFIX + token), TokenInfo.class);
//
//        if (tokenInfo.getUid() == null || tokenInfo.getUid() == 0) {
//            throw new BaseException(Constant.STATUS_TOKEN_UID_NULL, Constant.MSG_TOKEN_UID_NULL);
//        }

        return true;
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
