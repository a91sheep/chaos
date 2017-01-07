package com.store59.printapi.service.wechat;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store59.printapi.model.param.wechat.message.EventMessage;

@Service
public class EventDispacherService {
	@Autowired
	EventClickService clickService;
	@Autowired
	SubcribeService subcribeService;

	Logger logger=Logger.getLogger(EventDispacherService.class);
	
	public String eventDispacher(EventMessage eventMessage){
		String result="success";
		switch (eventMessage.getEvent()){
		case "subscribe": 
			try{
				subcribeService.sbucribe(eventMessage);
	            }catch(Exception e){
	            	logger.error("处理subscribe消息出现问题："+e.getMessage());
	            }
			break;
		case "CLICK":
			try{
				clickService.menuClick(eventMessage);
	            }catch(Exception e){
	            	logger.error("处理菜单click消息出现问题："+e.getMessage());
	            }
			break;
		default:
			break;
		}
		return result;
	}
}
