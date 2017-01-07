package com.store59.printapi.common.converter;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.store59.printapi.model.result.Datagram;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
public class HttpConverter extends FastJsonHttpMessageConverter {
    private static final Logger log = LoggerFactory.getLogger(HttpConverter.class);

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();

//        if (((Datagram<?>) obj).getStatus().intValue() != 0 && ((Datagram<?>) obj).getIsApp() != null && !((Datagram<?>) obj).getIsApp()) {
//            //pc修改http status
//            ServletServerHttpResponse response = (ServletServerHttpResponse) outputMessage;
//            response.setStatusCode(HttpStatus.BAD_REQUEST);
//            log.error("**************response.getStatusCode:" + response.getServletResponse().getStatus());
//        }
        try{
        ((Datagram<?>) obj).setIsApp(null);
        }catch(ClassCastException e){
        	log.error(JSON.toJSONString(obj, getFeatures()));
        }
        String text = JSON.toJSONString(obj, getFeatures());
        byte[] bytes = text.getBytes(getCharset());
        out.write(bytes);
    }
}