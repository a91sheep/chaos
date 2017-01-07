/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.util.oxm;

/**
 * XML映射异常.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2016年1月12日
 * @since 1.0
 */
public class XmlMappingException extends RuntimeException {
    private static final long serialVersionUID = 5646429882658609385L;

    /**
     * 无参构造.
     */
    public XmlMappingException() {
        super();
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public XmlMappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public XmlMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public XmlMappingException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public XmlMappingException(Throwable cause) {
        super(cause);
    }

}
