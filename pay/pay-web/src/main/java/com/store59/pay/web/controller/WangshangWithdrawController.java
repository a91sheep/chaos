/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.pay.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.kylin.utils.StringUtil;
import com.store59.pay.model.BatchWithrawInfo;
import com.store59.pay.service.wangshang.WithdrawService;
import com.store59.pay.web.enums.ViewResultCodeEnum;
import com.store59.pay.web.model.ViewResult;

/**
 * 网商银行-提现
 * @author beangou
 *
 */
@Controller
@RequestMapping(value = "/wangshang")
public class WangshangWithdrawController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WangshangWithdrawController.class);

    @Autowired
    private WithdrawService withdrawService;
    
    private static final String SALT = "JYYuuhezT8BBwUKEv87kZmkj";

    /**
     * ERP提交批量提现名单
     * 
     * @param payForm
     * bizNo 业务流水号，上传文件时就生成（可以根据每一笔提现的对账id生成），保证每一次不同的提现 业务流水号不一样即可
     * 这样可以在确认批量转发时做必要的验证
     * 付款金额（传过来单位：分）（必填） 注意转成元！！！
     * @param result
     * @return
     * @throws Exception
     */
    
    public static void main(String[] args) {
		System.out.println(DigestUtils.md5Hex("4668989894468960000000221212127620160619610000000000001212121279"+SALT));
//    	System.out.println(DigestUtils.md5Hex("23232323"+"dc6bb89e1222f5c4ec42314354fe9ca9ab7252c9"));
		
		System.out.println(new BigDecimal(200.00).compareTo(new BigDecimal("20000").divide(new BigDecimal(100))));
	}
    
    @RequestMapping(value = { "/batch/withdraw"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, String>> batchWithdraw(@RequestParam(value = "batchNo") String batchNo, @RequestParam(value = "withdrawInfo[]") String[] withdrawInfo, 
    		@RequestParam(value = "sign") String sign) {
    	ViewResult<Map<String, String>> viewResult = new ViewResult<>();
    	String mySign = DigestUtils.md5Hex(batchNo+SALT);
        if(!mySign.equals(sign)) {
        	viewResult.setMsg("加密验证不通过");
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		return viewResult.toResult();
        }
    	
    	int len = withdrawInfo.length;
    	if(len == 0) {
    		viewResult.setMsg("withdrawInfo不能为空");
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		return viewResult.toResult();
    	}
    	
    	if(len > 200) {
    		viewResult.setMsg("withdrawInfo数组长度不能超过200");
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		return viewResult.toResult();
    	}
    	
    	//定长32位数字字符
    	if(batchNo.length() != 32) {
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		return viewResult.toResult();
    	}
    	
    	// 将提交来的数据保存到支付系统数据库，并开启一个任务，上传名册文件到sftp服务器
    	// 让调用方以固定格式传过来，以逗号分开，按顺序传，然后set到对象中，再保存到数据库
    	// 收款人账号（必填）收款人姓名（必填）	收款人开户行名称（必填）	付款金额（单位：元）（必填）
    	// 开户行联行号	收款人网点名称	身份证号	手机号 支付宝账号 备注 对账ID
    	Map<String, Object> resutlMap = null;
		try {
			resutlMap = withdrawService.batchWithdraw(batchNo, withdrawInfo);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("batchWithdraw失败, {}", e);
			resutlMap = new HashMap<>();
			resutlMap.put("result", false);
			resutlMap.put("resultMsg", "申请批量代发失败");
		}
    	if((boolean)resutlMap.get("result")) {
    		viewResult.setResultCode(ViewResultCodeEnum.SUCCESS);
    	}else {
    		viewResult.setResultCode(ViewResultCodeEnum.CREAT_WITHDRAW_RECORD_FAIL);
    		viewResult.setMsg(resutlMap.get("resultMsg").toString());
    	}
        return viewResult.toResult();
    }
    
    /**
     * ERP获取短信验证码
     * @param payForm
     * 测试环境 certNo：430830198907201280
     * @param result -- 参数问题：appid比较唯一的信息，存在支付系统，不让ERP传，还有成员机构编号也是；
     * 							传参包括：业务流水号、业务名称、身份证号
     * 业务流水号：商户业务的流水号，在确认业务操作时，带上与此相同流水号
     * 业务名称： P_BATCH_SALARY（批量提现）		
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/smscode"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, String>> smscode(@RequestParam(value = "bizNo") String bizNo, @RequestParam(value = "bizName") String bizName,
    		@RequestParam(value = "certNo") String certNo) {
    	ViewResult<Map<String, String>> viewResult = new ViewResult<>();
    	Map<String, Object> result = null;
		try {
			result = withdrawService.smscode(bizNo, bizName, certNo);
		} catch (Exception e) {
			LOGGER.error("获取短信验证码失败{}", e);
			e.printStackTrace();
		}
		if((boolean)result.get("result")) {
    		viewResult.setResultCode(ViewResultCodeEnum.SUCCESS);
    	}else {
    		viewResult.setResultCode(ViewResultCodeEnum.SMSCODE_FAIL);
    		viewResult.setMsg(result.get("resultMsg").toString());
    	}
        return viewResult.toResult();
    }
    
    /**
     * ERP请求调用批量代发确认接口
     * @param payForm
     * @param result 代发笔数、总金额 让ERP传，我这边也做保存，做校验
     * currencyCode： 币种（消费金额的币种，只支持人民币金额）156
     * @return
     * @throws IOException 
     * @throws DocumentException 
     * @throws NoSuchProviderException 
     * @throws NoSuchAlgorithmException 
     * @throws KeyManagementException 
     * @throws Exception
     */
    @RequestMapping(value = { "/withdraw/confirm"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, String>> withdrawConfirm(@RequestParam(value = "batchNo") String batchNo, @RequestParam(value = "bizNo") String bizNo, @RequestParam(value = "smsCode") String smsCode, 
    		@RequestParam(value = "totalCount") int totalCount, @RequestParam(value = "totalAmount") int totalAmount,
    		@RequestParam(value = "currencyCode") String currencyCode, String remark, @RequestParam(value = "sign") String sign) {
    	ViewResult<Map<String, String>> viewResult = new ViewResult<>();
    	String mySign = DigestUtils.md5Hex(batchNo+bizNo+SALT);
    	if(!mySign.equals(sign)) {
        	viewResult.setMsg("加密验证不通过");
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		return viewResult.toResult();
        }
    	if(batchNo.length() != 32) {
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		return viewResult.toResult();
    	}
    	BatchWithrawInfo info = withdrawService.selectByTradeNo(batchNo);
    	if(info == null) {
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		viewResult.setMsg("不存在该批次号");
    		return viewResult.toResult();
    	}
    	if(info.getTotalAmount().compareTo(new BigDecimal(totalAmount).divide(new BigDecimal(100))) != 0) {
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		viewResult.setMsg("该批次的总金额不正确");
    		return viewResult.toResult();
    	}
    	if(info.getTotalCount().compareTo(totalCount) != 0) {
    		viewResult.setResultCode(ViewResultCodeEnum.PARAM_INVALID);
    		viewResult.setMsg("该批次的总笔数不正确");
    		return viewResult.toResult();
    	}
    	if(StringUtil.isEmpty(remark)) {
    		remark = "代发工资";
    	}
    	
    	Map<String, Object> resultMap = null;
    	boolean result = false;
    	try {
    		resultMap = withdrawService.withdrawConfirm(batchNo, bizNo, smsCode, String.valueOf(totalCount), com.store59.pay.web.utils.StringUtil.formatMoney(totalAmount), currencyCode, remark);
		} catch (Exception e) {
			LOGGER.error("确认接口异常：", e);
			result = false;
			e.printStackTrace();
		}
    	if(resultMap != null) {
    		result = (boolean)resultMap.get("result");
    	}
        if(result) {
        	viewResult.setResultCode(ViewResultCodeEnum.SUCCESS);
        }else {
        	viewResult.setMsg(resultMap.get("resultMsg") == null ? "确认批量代发失败" : resultMap.get("resultMsg").toString());
        	viewResult.setResultCode(ViewResultCodeEnum.WITHDRAW_CONFIRM_FAIL);
        }
        
        return viewResult.toResult();
    }
    
    @RequestMapping(value = { "/download/resultfile"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, String>> downloadResultfile(@RequestParam(value = "batchTransNo") String batchTransNo, @RequestParam(value = "batchNo") String batchNo) {
    	boolean result = false;
    	ViewResult<Map<String, String>> viewResult = new ViewResult<>();
    	try {
    		result = withdrawService.downloadResultFile(batchTransNo, batchNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(result) {
    		viewResult.setResultCode(ViewResultCodeEnum.SUCCESS);
    	}else {
    		viewResult.setResultCode(ViewResultCodeEnum.DOWNLOAD_RESULT_FILE_FAIL);
    	}
    	return viewResult.toResult();
    }
    
}
