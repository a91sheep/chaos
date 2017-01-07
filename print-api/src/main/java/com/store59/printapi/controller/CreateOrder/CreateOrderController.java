package com.store59.printapi.controller.CreateOrder;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.model.param.createOrder.CreateOrderParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.LoginIdInfo;
import com.store59.printapi.model.result.UrlResult;
import com.store59.printapi.model.result.UserInfo;
import com.store59.printapi.service.createOrder.CreateOrderService;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/14
 * @since 1.0
 */
@RestController
@RequestMapping("/order/*")
public class CreateOrderController {
    @Autowired
    private CreateOrderService              createOrderService;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    private Logger logger = LoggerFactory.getLogger(CreateOrderController.class);

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public void fileUpLoad_ie(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || file.getSize() == 0) {
            throw new BaseException(CommonConstant.STATUS_REQUEST_FILE_INVALID, CommonConstant.MSG_REQUEST_FILE_NULL);
        }
        if (file.getOriginalFilename().endsWith(".doc") || file.getOriginalFilename().endsWith(".docx") ||
                file.getOriginalFilename().endsWith(".ppt") || file.getOriginalFilename().endsWith(".pptx") ||
                file.getOriginalFilename().endsWith(".pdf") || file.getOriginalFilename().endsWith(".jpg") ||
                file.getOriginalFilename().endsWith(".png") || file.getOriginalFilename().endsWith(".jpeg")) {
            logger.info("上传文件名称:" + file.getOriginalFilename() + ",文件格式符合要求");
        } else {
            logger.info("上传文件名:" + file.getOriginalFilename() + ",文件格式非法!");
            throw new BaseException(CommonConstant.STATUS_REQUEST_FILE_INVALID,
                    "上传文件名:" + file.getOriginalFilename() + ",文件格式非法!");
        }
        Datagram<UrlResult> datagram = createOrderService.fileUpLoad(file, false);
        response.setContentType("text/html");
        response.getWriter().write(JsonUtil.getJsonFromObject(datagram));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object createOrder(@Valid CreateOrderParam param, BindingResult result, @CookieValue(CommonConstant.KEY_COOKIE_NAME_TOKEN) String tokenId) {
        if (result.hasErrors()) {
            return DatagramHelper.getDatagram(CommonConstant.STATUS_REQUEST_DATA_INVALID,
                    result.getAllErrors().get(0).getDefaultMessage());
        }
        String loginids = valueOpsCache.get(CommonConstant.KEY_REDIS_TOKEN_PREFIX + tokenId);
        LoginIdInfo loginIdInfo = new LoginIdInfo();
        try {
            loginIdInfo = JsonUtil.getObjectFromJson(loginids, LoginIdInfo.class);
        } catch (Exception e) {
            throw new BaseException(CommonConstant.STATUS_CONVER_JSON, CommonConstant.MSG_CONVER_JSON);
        }
        Long uid = loginIdInfo.getUid();

        String userStr = valueOpsCache.get(CommonConstant.KEY_REDIS_USER_PREFIX + uid);
        UserInfo userInfo = JsonUtil.getObjectFromJson(userStr, UserInfo.class);
        return createOrderService.createOrder(param, uid, userInfo.getUname());
    }

    @RequestMapping("sign")
     public Object getOssSign(@RequestParam(required = false) String suffix, @RequestParam(required = false) String originName) {
        return createOrderService.getOssSign(suffix, originName);
    }

    @RequestMapping(value = "fileparams", method = RequestMethod.POST)
    public Object fileParams(@RequestParam(required = true) String url, @RequestParam(required = true) String sourceFileName) throws Exception {
        if (url.endsWith(".doc") || url.endsWith(".docx") ||
                url.endsWith(".ppt") || url.endsWith(".pptx") ||
                url.endsWith(".pdf") || url.endsWith(".jpg") ||
                url.endsWith(".png") || url.endsWith(".jpeg")) {
            logger.info("上传文件名称:" + url + ",文件格式符合要求");
        } else {
            logger.info("上传文件名:" + url + ",文件格式非法!");
            throw new BaseException(CommonConstant.STATUS_REQUEST_FILE_INVALID,
                    "上传文件名:" + url + ",文件格式非法!");
        }
        return createOrderService.fileParams(url, sourceFileName, false);
    }

}
