/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.converter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.store59.pay.util.annotation.Converter;

/**
 * 模型转换器注解bean的后置处理器.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月5日
 * @since 1.0
 */
@Component
public class ModelConverterAnnotationBeanPostProcessor implements BeanPostProcessor {

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (AnnotationUtils.findAnnotation(bean.getClass(), Converter.class) != null) {
            if (!(bean instanceof ModelConverter<?, ?>)) {
                throw new BeanInitializationException(
                        "初始化bean[name=" + beanName + "]失败：[" + bean.getClass().getName() + "]类未实现[" + ModelConverter.class.getName() + "]接口");
            }
            ModelConverterUtils.register((ModelConverter<?, ?>) bean);
        }

        return bean;
    }

}
