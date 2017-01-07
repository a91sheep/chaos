package com.store59.printapi.controller.gala;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store59.printapi.service.gala.PrintGalaService;

/**
 * @author 作者:韬子 E-mail:haowt@59store.com
 * @version 创建时间：2016年4月18日 下午11:54:26 活动类
 */
@RestController
@RequestMapping("/gala/*")
public class PrintGalaController {

	@Autowired
	private PrintGalaService printGalaService;

	private Logger logger = org.slf4j.LoggerFactory.getLogger(PrintGalaController.class);

	@RequestMapping(value = "validate", method = RequestMethod.GET)
	public Object validate(@RequestParam(required = true) String phone, @RequestParam(required = false) String dormId) throws Exception {
		logger.debug("print gala 20160509 validate begin");
		return printGalaService.validateService(phone, dormId);
	}
	@RequestMapping(value = "payresult", method = RequestMethod.GET)
	public Object clientPayResult(@RequestParam(required = true) String orderId) throws Exception {
		logger.debug("print gala 20160509 order result begin");
		return printGalaService.clientPayResult(orderId);
	}
	
}
