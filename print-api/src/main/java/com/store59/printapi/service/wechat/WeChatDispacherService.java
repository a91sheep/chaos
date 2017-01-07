package com.store59.printapi.service.wechat;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store59.printapi.common.wechat.enums.MsgTypeEnum;
import com.store59.printapi.model.param.wechat.message.EventMessage;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com 
 * @version 创建时间：2016年5月20日 下午3:49:02 
 * 微信消息分发类
 */
@Service
public class WeChatDispacherService {
	@Autowired
	PicService picService;
	@Autowired
	TextService textService;
	@Autowired
	EventDispacherService eventService;

	Logger logger=Logger.getLogger(WeChatDispacherService.class);
	
	public String msgDispacher(EventMessage eventMessage){
		String result="success";
		String msgType=eventMessage.getMsgType();
		switch (MsgTypeEnum.MsgTOMsg(msgType.toUpperCase())){
		case TEXT: 
			try{
	             textService.textCheck(eventMessage);
	            }catch(Exception e){
	            	logger.error("处理pic消息出现问题："+e.getMessage());
	            }
			break;
		case IMAGE:
			try{
	             picService.picCheck(eventMessage);
	            }catch(Exception e){
	            	logger.error("处理pic消息出现问题："+e.getMessage());
	            }
			break;
		case VOICE:
			break;
		case VIDEO:
			break;
		case LINK:
			break;
		case EVENT:
			try{
				eventService.eventDispacher(eventMessage);
			}catch(Exception e){
				logger.error("处理subcribe消息出现问题"+e.getMessage());
			}
			break;
		case LOCATION:
			break;
		default:
			break;
		}
		return result;
	}
}
