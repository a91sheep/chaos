package com.yunyin.print.main.api.common.exception;

import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.exception.ServiceException;
import com.store59.kylin.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/19
 * @since 1.0
 */
@ControllerAdvice
public class BaseExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BaseExceptionHandler.class);

    private byte DEFAULT_STATUS_ERROR = -1;

    /**
     * 发生异常时的统一处理（调用时服务器端处理时发生的异常）
     *
     * @param req
     * @param ex
     * @return 异常信息（最终会被转换成json格式）
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> handleBadRequest(HttpServletRequest req, Exception ex) {
        Result<Object> datagram = new Result<Object>();
        if (ex instanceof ServiceException) {
            //App业务异常
            log.error("业务异常:" + ex.getMessage());
            ServiceException e = (ServiceException) ex;
            datagram.setStatus(e.getStatus());
            datagram.setMsg(e.getMsg());
        } else if (ex instanceof BaseException) {
            //Pc 业务异常
            log.error("基础信息异常:" + ex.getMessage());
            BaseException e = (BaseException) ex;
            datagram.setStatus(e.getStatus());
            datagram.setMsg(e.getMsg());
        }
//        else if (ex instanceof MultipartException) {
//            try {
//                Collection<Part> parts = req.getParts();
//                if (parts != null && parts.size() > 0) {
//                    log.info("parts.size():******" + parts.size());
//                    for (Part part : parts) {
//                        String filename = part.getHeader("content-disposition");
//                        log.info("parts.filename:******" + filename);
//                        log.info("parts.getName():******" + part.getName());
//                    }
//                }
//            } catch (Exception e) {
//                log.info("不是文档上传请求......");
//            }
//            ex.printStackTrace();
//            log.error("文件上传异常:" + ex.getMessage());
//            datagram.setStatus(CommonConstant.STATUS_MAXFILEUPLOAD);
//            datagram.setMsg(CommonConstant.MSG_STATUS_MAXFILEUPLOAD + maxFileSize);
//            datagram.setIsApp(false);
//        }
        else {
            log.error("其余异常:" + ex.getMessage());
            datagram.setStatus(DEFAULT_STATUS_ERROR);
            datagram.setMsg(ex.getMessage());
        }
        if (datagram.getStatus() == -1) {
            log.error("系统发生异常[status=" + datagram.getStatus() + "，msg=" + datagram.getMsg() + "]", ex);
        } else {
            log.error("系统发生异常[status=" + datagram.getStatus() + "，msg=" + datagram.getMsg() + "]", ex);
        }
        return datagram;
    }
}
