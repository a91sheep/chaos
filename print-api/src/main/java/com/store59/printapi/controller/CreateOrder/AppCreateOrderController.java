package com.store59.printapi.controller.CreateOrder;

import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.constant.Constant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.model.param.AppBaseParam;
import com.store59.printapi.model.param.createOrder.AppCreateOrderParam;
import com.store59.printapi.model.result.app.TokenInfo;
import com.store59.printapi.service.createOrder.AppCreateOrderService;
import com.store59.printapi.service.createOrder.CreateOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/14
 * @since 1.0
 */
@RestController
@RequestMapping("/print/*")
public class AppCreateOrderController {
    @Autowired
    private AppCreateOrderService           appCreateOrderService;
    @Autowired
    private CreateOrderService              createOrderService;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    private Logger logger = LoggerFactory.getLogger(AppCreateOrderController.class);

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public Object fileUpLoad_ie(HttpServletRequest request, HttpServletResponse response, MultipartFile file, AppBaseParam param) throws IOException {
        if (file == null || file.isEmpty() || file.getSize() == 0) {
            throw new AppException(CommonConstant.STATUS_REQUEST_FILE_INVALID, CommonConstant.MSG_REQUEST_FILE_NULL);
        }
        if (file.getOriginalFilename().endsWith(".doc") || file.getOriginalFilename().endsWith(".docx") ||
                file.getOriginalFilename().endsWith(".ppt") || file.getOriginalFilename().endsWith(".pptx") ||
                file.getOriginalFilename().endsWith(".pdf") || file.getOriginalFilename().endsWith(".jpg") ||
                file.getOriginalFilename().endsWith(".png") || file.getOriginalFilename().endsWith(".jpeg")) {
            logger.info("上传文件名称:" + file.getOriginalFilename() + ",文件格式符合要求");
        } else {
            logger.info("上传文件名:" + file.getOriginalFilename() + ",文件格式非法!");
            throw new AppException(CommonConstant.STATUS_REQUEST_FILE_INVALID,
                    "上传文件名:" + file.getOriginalFilename() + ",文件格式非法!");
        }
        return createOrderService.fileUpLoad(file, true);
    }

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public Object fileUpLoad_IOS(HttpServletRequest request, HttpServletResponse response ,MultipartFile file) throws IOException {
        List<Part> list = new ArrayList<>();
        try {
            list = (List<Part>) request.getParts();
        } catch (ServletException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        Part part = list.get(0);
        if (part.getInputStream() == null || part.getSize() == 0) {
            throw new AppException(CommonConstant.STATUS_REQUEST_FILE_INVALID, CommonConstant.MSG_REQUEST_FILE_NULL);
        }
        logger.info("ios上传文件名称:"+ part.getSubmittedFileName());
        if (part.getSubmittedFileName().endsWith(".doc") || part.getSubmittedFileName().endsWith(".docx") ||
                part.getSubmittedFileName().endsWith(".ppt") || part.getSubmittedFileName().endsWith(".pptx") ||
                part.getSubmittedFileName().endsWith(".pdf") || part.getSubmittedFileName().endsWith(".jpg") ||
                part.getSubmittedFileName().endsWith(".png") || part.getSubmittedFileName().endsWith(".jpeg")) {
            logger.info("上传文件名称:" + part.getSubmittedFileName() + ",文件格式符合要求");
        } else {
            logger.info("上传文件名:" + part.getSubmittedFileName() + ",文件格式非法!");
            throw new AppException(CommonConstant.STATUS_REQUEST_FILE_INVALID,
                    "上传文件名:" + part.getSubmittedFileName() + ",文件格式非法!");
        }
        return createOrderService.fileUpLoad_IOS(part);
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public Object createOrder(@Valid AppCreateOrderParam param, BindingResult result) {
        if (result.hasErrors()) {
            return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
                    result.getAllErrors().get(0).getDefaultMessage(), true);
        }
        TokenInfo tokenInfo = JsonUtil.getObjectFromJson(valueOpsCache.get(Constant.KEY_REDIS_TOKEN_PREFIX + param.getToken()), TokenInfo.class);
        if (tokenInfo.getUid() == null || tokenInfo.getUid() == 0) {
            throw new AppException(Constant.STATUS_TOKEN_UID_NULL, Constant.MSG_TOKEN_UID_NULL);
        }
        Long uid = tokenInfo.getUid();
        return appCreateOrderService.createOrder_App(param, uid);
    }
}
