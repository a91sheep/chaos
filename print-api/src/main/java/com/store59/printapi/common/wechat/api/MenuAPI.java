package com.store59.printapi.common.wechat.api;

import java.awt.Menu;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;

import com.store59.kylin.utils.JsonUtil;
import com.store59.printapi.common.wechat.LocalHttpClient;
import com.store59.printapi.model.param.wechat.BaseResult;
import com.store59.printapi.model.param.wechat.menu.Button;
import com.store59.printapi.model.param.wechat.menu.MenuButtons;

public class MenuAPI extends BaseAPI {

	/**
	 * 创建菜单
	 * 
	 * @param access_token
	 * @param menuJson
	 *            菜单json 数据 例如{\
	 *            "button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"},{\"type\":\"click\",\"name\":\"歌手简介\",\"key\":\"V1001_TODAY_SINGER\"},{\"name\":\"菜单\",\"sub_button\":[{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.soso.com/\"},{\"type\":\"view\",\"name\":\"视频\",\"url\":\"http://v.qq.com/\"},{\"type\":\"click\",\"name\":\"赞一下我们\",\"key\":\"V1001_GOOD\"
	 *            }]}]}
	 * @return
	 */
	public static BaseResult menuCreate(String access_token, String menuJson) {
		HttpUriRequest httpUriRequest = RequestBuilder.post().setHeader(jsonHeader)
				.setUri(BASE_URI + "/cgi-bin/menu/create").addParameter(getATPN(), access_token)
				.setEntity(new StringEntity(menuJson, Charset.forName("utf-8"))).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, BaseResult.class);
	}

	/**
	 * 创建菜单
	 * 
	 * @param access_token
	 * @param menuButtons
	 * @return
	 */
	public static BaseResult menuCreate(String access_token, MenuButtons menuButtons) {
		String str = JsonUtil.getJsonFromObject(menuButtons);
		return menuCreate(access_token, str);
	}

	/**
	 * 获取菜单
	 * 
	 * @param access_token
	 * @return
	 */
	public static Menu menuGet(String access_token) {
		HttpUriRequest httpUriRequest = RequestBuilder.post().setUri(BASE_URI + "/cgi-bin/menu/get")
				.addParameter(getATPN(), access_token).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, Menu.class);
	}

	/**
	 * 删除菜单
	 * 
	 * @param access_token
	 * @return
	 */
	public static BaseResult menuDelete(String access_token) {
		HttpUriRequest httpUriRequest = RequestBuilder.post().setUri(BASE_URI + "/cgi-bin/menu/delete")
				.addParameter(getATPN(), access_token).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, BaseResult.class);
	}

	/**
	 * 创建个性化菜单
	 * 
	 * @param access_token
	 * @param menuButtons
	 * @return
	 */
	public static BaseResult menuAddconditional(String access_token, MenuButtons menuButtons) {
		String menuJson = JsonUtil.getJsonFromObject(menuButtons);
		HttpUriRequest httpUriRequest = RequestBuilder.post().setHeader(jsonHeader)
				.setUri(BASE_URI + "/cgi-bin/menu/addconditional").addParameter(getATPN(), access_token)
				.setEntity(new StringEntity(menuJson, Charset.forName("utf-8"))).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, BaseResult.class);
	}

	public static void main(String[] args) {
		Button[] button=new Button[3];
		Button b1=new Button();
		b1.setName("店长招募");
		b1.setType("view");
		b1.setUrl("http://m.yemao.59store.com/#apply/dorm?");

		//第二个开始
		Button b3=new Button();
		b3.setName("照片打印");
		b3.setType("view");
		List<Button>list_b_3=new LinkedList<Button>();
		Button subbutton_3_1=new Button();
		subbutton_3_1.setName("操作指南");
		subbutton_3_1.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNzIwMjc5NA==&mid=502219772&idx=1&sn=c043215bbb1e717a82529bbdebb0417d&scene=0&previewkey=QJxqVBey0PN3bRmAd1NGi8NS9bJajjJKzz%2F0By7ITJA%3D#wechat_redirect");
		subbutton_3_1.setType("view");
		Button subbutton_3_2=new Button();
		subbutton_3_2.setName("绑定帐号");
		subbutton_3_2.setType("click");
		subbutton_3_2.setKey("bind");
		list_b_3.add(subbutton_3_1);
		list_b_3.add(subbutton_3_2);
		b3.setSub_button(list_b_3);
		//第二个菜单结束
		
		
		//第三个菜单开始
		Button b2=new Button();
		b2.setName("更多服务");
		b2.setType("view");
		List<Button>list_b_2=new LinkedList<Button>();
		Button subbutton_2_3=new Button();
		subbutton_2_3.setName("投放广告");
		subbutton_2_3.setType("view");
		subbutton_2_3.setUrl("http://openid.wx.59store.com/wechat-openid/openid?mpName=my59store&returnUrl=http://printad.59store.com&returnAppUrl=http://m.yemao.59store.com");
		Button subbutton_2_1=new Button();
		subbutton_2_1.setName("文档打印");
		subbutton_2_1.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNzIwMjc5NA==&mid=502219771&idx=1&sn=6d19505811a84f5dac0e4596a1f1741a&scene=0&previewkey=QJxqVBey0PN3bRmAd1NGi8NS9bJajjJKzz%2F0By7ITJA%3D#wechat_redirect");
		subbutton_2_1.setType("view");
		Button subbutton_2_2=new Button();
		subbutton_2_2.setName("app下载");
		subbutton_2_2.setType("view");
		subbutton_2_2.setUrl("http://yemao.59store.com/share?hxfrom=app");
		
		list_b_2.add(subbutton_2_3);
		list_b_2.add(subbutton_2_1);
		list_b_2.add(subbutton_2_2);
		b2.setSub_button(list_b_2);
		//第三个菜单结束
		
		button[0]=b1;
		button[1]=b3;
		button[2]=b2;
		MenuButtons menu=new MenuButtons();
		menu.setButton(button);
		System.out.println(JsonUtil.getJsonFromObject(menu));
		//
		BaseResult rs=menuCreate("EzpbOySdlpSFjIlHvpgto2bcc35tFfjtwcbucMEcpgtBYR8bOOY4pFyzE5wYmou2QOf-zb_WMQbLI_TyVbYWSKvxTAcFya-llOFvE3lT1m_krVK3yT7VL52mJFtw8EyMDZIdADASOX", menu);
		System.out.println(JsonUtil.getJsonFromObject(rs));
	}
}