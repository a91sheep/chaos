package com.store59.printapi.service.wechat;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Resource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.common.wechat.TokenManager;
import com.store59.printapi.common.wechat.api.MessageAPI;
import com.store59.printapi.model.param.wechat.BaseResult;
import com.store59.printapi.model.param.wechat.message.NewsMessage;
import com.store59.printapi.model.param.wechat.message.NewsMessage.Article;
import com.store59.printapi.model.param.wechat.pic.PicWechat;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年5月12日 下午7:39:58 定时检测类
 */
@Service
public class SendMsgService {

    private Logger logger = Logger.getLogger(SendMsgService.class);
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;

    private static CopyOnWriteArraySet<String> list = new CopyOnWriteArraySet<String>();

    private String gap = "5";// 全局变量要加到配置文件中
    @Value("${print.wechat.appid}")
    private String appid;
    @Value("${print.wechat.cart.url}")
    private String cartUrl;
    @Value("${print.wechat.pay.openid.url}")
    private String payOpenidUrl;
    @Autowired
    TokenManager tokenManager;

//	private SendMsgService() {
//
//	}

    public void addList(String openid) {
        list.add(openid);
    }

    public synchronized void removeOpenid(String openid) {
        list.remove(openid);
    }

    @SuppressWarnings("deprecation")
    @Scheduled(fixedRate = 5000)
    public void sendMsg() {
        if (list.size() > 0)
            logger.info("开始判断推送,队列长度：" + list.size());
        for (String openid : list) {
            try {
                String picStr = valueOpsCache.get(CommonConstant.WECHAT_OPENID_PREFIX + openid);
                if (!StringUtil.isBlank(picStr)) {
                    PicWechat pic = JsonUtil.getObjectFromJson(picStr, new TypeReference<PicWechat>() {
                    });
                    if ((pic.getTimestamp() * 1000L + Long.parseLong(gap) * 1000) < System.currentTimeMillis()) {
                        // 此处开始推送消息
                        removeOpenid(openid);
                        //此处返回推送链接用户点击可以进入购物车页面
//					TextMessage textMsg = new TextMessage(openid, "您已成功上传共"+pic.getList().size()+"张照片待打印<a href=\"http://www.baidu.com/\">点击这里确认下单</a>");
                        List<Article> articles = new ArrayList<Article>();
                        Article art = new NewsMessage.Article("您已成功上传共" + pic.getList().size() + "张照片待打印"
                                , "请在一小时内点击这里确认下单,或者您可以继续上传待打印照片", String.format(payOpenidUrl, URLEncoder.encode(cartUrl + "?uid=" + valueOpsCache.get(CommonConstant.WECHAT_OPENID_UID_PREFIX + openid)))
                                , pic.getList().get(pic.getList().size() - 1).getPicUrl());
                        articles.add(art);
                        NewsMessage newsMsg = new NewsMessage(openid, articles);
                        logger.info("图片交互请求前，access_token=" + tokenManager.getToken(appid) + "appid=" + appid + "newsMsg:" + JsonUtil.getJsonFromObject(newsMsg));

                        BaseResult rs = MessageAPI.messageCustomSend(tokenManager.getToken(appid), newsMsg);
                        if (rs.getErrcode().equals("40003") || rs.getErrcode().equals("40001")) {
                            tokenManager.refurbish();
                        }
                        logger.info("推送结果：" + JsonUtil.getJsonFromObject(rs));
                    }
                } else {
                    removeOpenid(openid);
                }
            } catch (Exception e) {
                logger.info("推送消息出错：" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        List<Article> articles = new ArrayList<Article>();
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                + "wx854ecafa10524958" + "&redirect_uri=" + URLEncoder.encode("http://121.40.91.177:8081/#print?uid=1461401709857787") + "&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        url = "http://print.59store.net:8081/#print?uid=1461401709857787";
        Article art = new NewsMessage.Article("您已成功上传共1张照片待打印"
                , "订单号123456789012345678 点击确认下单", url, "http://print-identification.oss-cn-hangzhou.aliyuncs.com/5b8e5f9795ced28108a6abd9e663be9a.jpeg");
        articles.add(art);
        NewsMessage newsMsg = new NewsMessage("o6jRbwKWfFBAyptfuukYUqdQ0smk", articles);
        BaseResult rs = MessageAPI.messageCustomSend("QNVlX5THjMlcOw4tmTng4MTTH6TMD6P8MFLJwx0MbvsYBEAOT29CxnvSXgEkyWyXxU5m4jT0Zp5FeiZgHH5LFysA-RlL1vTspKfatgRzmEdkszQBVly1BXulK3M9Jb00NVRgADAQYU", newsMsg);
        System.out.println(JsonUtil.getJsonFromObject(rs));
    }
}
