package com.store59.printapi.common.exception;

import com.store59.kylin.common.exception.BaseException;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.model.result.Datagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Collection;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/13
 * @since 1.0
 */
@ControllerAdvice
public class CommonExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);
    @Value("${multipart.maxFileSize}")
    private String maxFileSize;

    /**
     * 发生异常时的统一处理（调用时服务器端处理时发生的异常）
     *
     * @param req
     * @param ex
     * @return 异常信息（最终会被转换成json格式）
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Datagram<Object> handleBadJsonRequest(HttpServletRequest req, Exception ex) {
        Datagram<Object> datagram = new Datagram<Object>();
        if (ex instanceof AppException) {
            //App业务异常
        	log.error("APP业务异常:" + ex.getMessage());
            AppException e = (AppException) ex;
            datagram.setStatus(e.getStatus());
            datagram.setMsg(e.getMsg());
            datagram.setIsApp(true);
        } else if (ex instanceof BaseException) {
            //Pc 业务异常
        	log.error("PC业务异常:" + ex.getMessage());
            BaseException e = (BaseException) ex;
            datagram.setStatus(e.getStatus());
            datagram.setMsg(e.getMsg());
            datagram.setIsApp(false);
        } else if (ex instanceof MultipartException) {
            try {
                Collection<Part> parts = req.getParts();
                if(parts != null && parts.size() > 0) {
                    log.info("parts.size():******" + parts.size());
                    for (Part part : parts) {
                        String filename = part.getHeader("content-disposition");
                        log.info("parts.filename:******" + filename);
                        log.info("parts.getName():******" + part.getName());
                    }
                }
            } catch (Exception e) {
                log.info("不是文档上传请求......");
            }
            ex.printStackTrace();
            log.error("文件上传异常:" + ex.getMessage());
            datagram.setStatus(CommonConstant.STATUS_MAXFILEUPLOAD);
            datagram.setMsg(CommonConstant.MSG_STATUS_MAXFILEUPLOAD + maxFileSize);
            datagram.setIsApp(false);
        } else {
        	log.error("其余异常:" + ex.getMessage());
            datagram.setStatus(CommonConstant.GLOBAL_STATUS_ERROR);
            datagram.setMsg(ex.getMessage());
            datagram.setIsApp(false);
        }
        if (datagram.getStatus() == -1) {
            log.error("系统发生异常[status=" + datagram.getStatus() + "，msg=" + datagram.getMsg() + "]", ex);
        } else {
            log.error("系统发生异常[status=" + datagram.getStatus() + "，msg=" + datagram.getMsg() + "]", ex);
        }
        return datagram;
    }
}