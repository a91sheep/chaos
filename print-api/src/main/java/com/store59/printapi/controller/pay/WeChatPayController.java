package com.store59.printapi.controller.pay;

import com.store59.kylin.utils.SignatureUtils;
import com.store59.print.common.model.PrintOrder;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.CommonException;
import com.store59.printapi.service.order.MyOrderService;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 2016/01/14
 * @since 1.0
 */
@RestController
@RequestMapping("/payment/*")
public class WeChatPayController {

    private static Logger logger = LoggerFactory.getLogger(WeChatPayController.class);

    /**
    @Autowired
    private MyOrderService myOrderService;

    @Value("${wechat.openid.url}")
    private String wechatOpenidHost;

    @Value("${wechat.url}")
    private String wechatHost;

    @Value("${wechat.notify.url}")
    private String wechatNotifyhost;

    @Value("${wechat.success.url}")
    private String wechatSuccessHost;

    @Value("${wechat.error.url}")
    private String wechatErrorHost;

    @Value("${wechat.mpName}")
    private String mpName;

    @Value("${zhifu.key}")
    private String zhifuKey;
    */

    /**
    // 微信支付入口（接收一个订单号，然后获取 code ，获取 access_token，获取 openid）
    // 这样微信客户端就不直接向微信项目发起请求
    @RequestMapping(value = "/wxpay/{orderId}", method = RequestMethod.GET)
    public String paymentWxpay(@PathVariable("orderId") Long orderId) {
        logger.debug("接收到的参数 orderId：" + orderId);
        logger.debug("依赖注入的值：wechatOpenidHost" + wechatOpenidHost);
        URIBuilder uRIBuilder = new URIBuilder().setScheme("http")
                .setHost(wechatOpenidHost)
                .setPath("/wechat-openid/openid")
                .setParameter("mpName", mpName)
                .setParameter("returnUrl", wechatNotifyhost + "/payment/wxpay/pay?orderId=" + orderId);
        String redirectUrl = uRIBuilder.toString();
        logger.debug("重定向的地址：" + redirectUrl);
        return "redirect:" + redirectUrl;
    }*/

    /**
    @RequestMapping(value = "/wxpay/pay", method = RequestMethod.GET)
    public String wxpay(String orderId, String openid) {
        logger.debug("接收到的参数 openid：" + openid);
        logger.debug("接收到的参数 orderId：" + orderId);

        PrintOrder printOrder = myOrderService.getPrintOrderById(Long.valueOf(orderId));

        if (printOrder.getPayType() != CommonConstant.ORDER_PAYTYPE_WEIXINPAY) {
            throw new CommonException("该订单的支付类型不是微信");
        }
        if (printOrder.getStatus().byteValue() != CommonConstant.ORDER_STATUS_0) {
            throw new CommonException("微信：该订单已支付或已取消");
        }

        //传递到下一个方法的请求参数
        String foodName = "59store 打印店订单（" + orderId + "）";
        String moneyStr = printOrder.getTotalAmount().toString();

        // 支付完成以后，异步通知的 url
        String notifyUrl = wechatNotifyhost + "/payment/notify";
        // 支付成功以后，同步跳转的 url
        String successUrl = wechatSuccessHost + "/user/dormorder";
        // 支付失败以后，同步跳转的 url
        String errorUrl = wechatErrorHost + "/user/dormorder";

        // 构造请求参数的 Map 键值对，以方便加密
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("orderId", orderId); //
        paramsMap.put("openId", openid); //
        paramsMap.put("foodName", foodName);//
        paramsMap.put("money", moneyStr);
        paramsMap.put("notifyUrl", notifyUrl);
        paramsMap.put("successUrl", successUrl);
        paramsMap.put("errorUrl", errorUrl);

        // 对所有参数排序以后加密（这里须要微信支付的 key ）
        String sign = SignatureUtils.sign(paramsMap, zhifuKey);

        String redirectUrl = null;
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http").setHost(wechatHost).setPath("/wxpay/pay").setParameter("orderId", orderId).setParameter("openId", openid).setParameter("foodName", foodName).setParameter("money", moneyStr).setParameter("notifyUrl", notifyUrl).setParameter("successUrl", successUrl).setParameter("errorUrl", errorUrl).setParameter("sign", sign).build();
            redirectUrl = uri.toString();
            logger.debug("在微信客户端要重定向的地址：" + redirectUrl);
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        return "redirect:" + redirectUrl;
    }
    */

}
