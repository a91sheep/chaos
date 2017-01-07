/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.store59.pay.service.wangshang;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.store59.kylin.utils.JsonUtil;
import com.store59.kylin.utils.StringUtil;
import com.store59.pay.mapper.BatchWithrawInfoMapper;
import com.store59.pay.mapper.PayDetailMapper;
import com.store59.pay.model.BatchWithrawInfo;
import com.store59.pay.model.PayDetail;
import com.store59.pay.service.config.WangshangConfig;
import com.store59.pay.service.enums.BatchWithdrawStateEnum;
import com.store59.pay.service.enums.PayDetailTypeCodeEnum;
import com.store59.pay.service.job.DownloadingJob;
import com.store59.pay.service.job.UploadingJob;
import com.store59.pay.service.util.ExcelUtil;
import com.store59.pay.service.util.SFTPUtil;
import com.store59.pay.util.lang.MapUtils;

/**
 * 批量代发
 * @author beangou
 *
 */
@Service
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class WithdrawServiceImpl implements WithdrawService {
    private static final Logger LOGGER  = LoggerFactory.getLogger(WithdrawServiceImpl.class);

    @Autowired
    private BatchWithrawInfoMapper masterBatchWithrawInfoMapper;
    
    @Autowired
    private PayDetailMapper masterPayDetailMapper;
    
    @Autowired
	private WangshangConfig wangshangConfig;
    
    @Autowired
    JavaMailSender mailSender;
    
    /** 私钥 RSA */
	@Value("${wangshang.rsaPrivateKey}")
    public String rsaPrivateKey;
	
    /** 网商银行测试环境公钥 RSA */
	@Value("${wangshang.bankRsaPublicKey}")
    public String bankRsaPublicKey; 
    
    public static final String currencyCode = "156";
    
	@Override
	public int insert(BatchWithrawInfo record) {
		LOGGER.info(JsonUtil.getJsonFromObject(record));
		return masterBatchWithrawInfoMapper.insertSelective(record);
	}
	
	/**
	 * 账户余额校验
	 * @param totalAmount
	 * @return
	 */
	private Map<String, Object> balanceCheck(BigDecimal totalAmount) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		//账户余额查询
        String function = "ant.ebank.acount.balance.query";

        XmlUtil xmlUtil = new XmlUtil();
        Map<String, String> form = new HashMap<String, String>();
        form.put("function", function);
        form.put("reqTime", new Timestamp(System.currentTimeMillis()).toString());
        //reqMsgId每次报文必须都不一样
        form.put("reqMsgId", UUID.randomUUID().toString());
        
        form.put("cardNo", wangshangConfig.getCardNo());
        form.put("currencyCode", currencyCode);
        form.put("cashExCode","CSH");//CSH钞

        //业务层签名规则：对除partner、sign本身之外的业务接口字段进行签名，如已无其他业务接口字段则对空字符串签名；如有则按以下文档中业务字段顺序签名。如有字段a，值A，字段b，值B，则签名原串为”a=A||b=B”,||为分隔符。若a字段值为空则签名原串为”a=||b=B”。
        String sign = Sign.sign("cardNo=" + form.get("cardNo") + "||currencyCode=" + form.get("currencyCode") + 
        		"||cashExCode="+ form.get("cashExCode"), false, wangshangConfig.getConfYinqizhilian(), wangshangConfig.getConfTopESA());
        form.put("sign", sign);//签名

        //封装报文
        String param = xmlUtil.format(form, function);
        LOGGER.info("网商账户余额查询请求报文=>{}", param);
        if (HttpsMain.isSign) {//生产环境需进行rsa签名
            param = XmlSignUtil.sign(param, rsaPrivateKey);
        }
        //发送请求
        String response = HttpsMain.httpsReq(wangshangConfig.getReqUrl(), param);
        LOGGER.info("网商账户余额查询返回报文=>{}", response);
        if (HttpsMain.isSign && !XmlSignUtil.verify(response, bankRsaPublicKey)) {
        	//生产环境需进行rsa验签
        	LOGGER.info("网商账户余额查询rsa验签失败，返回错误");
        	resultMap.put("result", false);
        	resultMap.put("resultMsg", "网商账户余额查询rsa验签失败, 稍后重试");
            return resultMap;
        }
        //解析报文
        Map<String, Object> resMap = xmlUtil.parse(response, function);
        Object availableBalanceObj = resMap.get("availableBalance");
        if(availableBalanceObj == null || StringUtil.isEmpty(availableBalanceObj.toString())) {
        	LOGGER.info("网商账户余额查询，返回为空");
        	resultMap.put("result", false);
        	resultMap.put("resultMsg", "网商账户余额查询rsa验签失败, 稍后重试");
            return resultMap;
        }
        String availableBalanceStr = availableBalanceObj.toString();
        //可用余额:分
        BigDecimal balance=new BigDecimal(availableBalanceStr);
        if(balance.compareTo(totalAmount.multiply(new BigDecimal(100))) < 1) {
        	LOGGER.info("网商账户余额查询不足，余额为：{}, 批次申请总额为：{}", balance, totalAmount.multiply(new BigDecimal(100)));
        	resultMap.put("result", false);
        	resultMap.put("resultMsg", "网商账户余额查询不足,请充值");
        }else {
        	resultMap.put("result", true);
        }
		return resultMap;
	}
	
	
	
	// 提交批量数据，先保存到批量表，然后保存到每一笔的记录（批量插入）
	@Override
	public Map<String, Object> batchWithdraw(String batchNo, String[] withdrawInfo) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		int len = withdrawInfo.length;
		BigDecimal totalAmount = new BigDecimal(0); // 单位：元
		// 收款人账号（必填）收款人姓名（必填）	收款人开户行名称（必填）	付款金额（单位：元）（必填） 对账ID (必填)
    	// 开户行联行号	收款人网点名称	身份证号	手机号 支付宝账号 备注
		List<PayDetail> list = new ArrayList<>();
		for (int i = 0; i < len; i++) {
			String[] detailArr = withdrawInfo[i].split("@#%");
			PayDetail detail = new PayDetail();
			if(StringUtil.isEmpty(detailArr[0]) || StringUtil.isEmpty(detailArr[1]) || StringUtil.isEmpty(detailArr[2]) || StringUtil.isEmpty(detailArr[3])) {
				resultMap.put("resultMsg", "收款人账号、收款人姓名、收款人开户行名称、付款金额必填【index=" + (i+1) + "】");
				resultMap.put("result", false);
				return resultMap;
			}
			BigDecimal amount = new BigDecimal(detailArr[3]).divide(new BigDecimal(100));
			detail.setType(PayDetailTypeCodeEnum.WITHDRAW.getCode());
			detail.setReceivorNo(detailArr[0]);
			detail.setReceivorName(detailArr[1]);
			detail.setReceivorOpenBankName(detailArr[2]);
			detail.setAmount(amount);
			detail.setCheckingId(detailArr[4]);
			
			detail.setOpenBankCode(detailArr[5]);
			detail.setReceivorBranchName(detailArr[6]);
			detail.setReceivorCertNo(detailArr[7]);
			detail.setReceivorMobile(detailArr[8]);
			detail.setReceivorAlipayNo(detailArr[9]);
			detail.setRemark(detailArr[10]);
			
			detail.setTradeNo(batchNo);
			
			list.add(detail);
			totalAmount = totalAmount.add(amount);
			detail = null;
		}
		// 校验余额是否充足
		resultMap = balanceCheck(totalAmount);
		if(!(boolean)resultMap.get("result")) {
			return resultMap;
		}
		// 插入批量数据表
		BatchWithrawInfo batchInfo = new BatchWithrawInfo();
		batchInfo.setTradeNo(batchNo);
		batchInfo.setTotalAmount(totalAmount);
		batchInfo.setTotalCount(len);
		
		int batchInfoNum = masterBatchWithrawInfoMapper.insertSelective(batchInfo);
		if(batchInfoNum < 1) {
			resultMap.put("resultMsg", "插入batchWithrawInfo数据失败");
			resultMap.put("result", false);
			return resultMap;
		}
		
		// 批量插入每一笔详情
		int detailNum = masterPayDetailMapper.batchInsert(list);
		if(detailNum < 1) {
			resultMap.put("resultMsg", "插入payDetail数据失败");
			resultMap.put("result", false);
			return resultMap;
		}
		
		// 生成文件并启动任务上传名册文件
		String[] title = {"收款人账号", "收款人姓名", "收款人开户行名称", "付款金额（单位：元）", "开户行联行号", "收款人网点名称", "身份证号", "手机号", "支付宝账号", "备注", "对账ID"};
		String path = this.getClass().getClassLoader().getResource("").getPath();
    	String pathName = path + "files/upload/"+new SimpleDateFormat("yyyyMMdd").format(new Date()).toString()+"_"+batchNo+".xls";
    	String pathNameEncrypted = path + "files/upload/"+batchNo+"_encrypted.xls";
		// 生成excl文件, 保存到服务器
		ExcelUtil.exportExcel(pathName, title, list);
		
		FileInputStream fiss = new FileInputStream(pathName);
        FileOutputStream fo = new FileOutputStream(pathNameEncrypted);
        byte[] buff = new byte[fiss.available()];
        int size = fiss.read(buff);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(buff, 0, size);
        fiss.read(buff);
        fiss.close();
        byte[] encData = bos.toByteArray();
        SignFile.sign(encData,true,fo,wangshangConfig.getConfYinqizhilian(), wangshangConfig.getConfTopESA());
		// 启动上传文件的任务
        new Thread(new UploadingJob(pathNameEncrypted, batchNo, masterBatchWithrawInfoMapper, wangshangConfig)).start();
        resultMap.put("result", true);
		return resultMap;
	}
	
	@Override 
	public Map<String, Object> smscode(String bizNo, String bizName, String certNo) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String function = wangshangConfig.getSmscodeFunction();
        XmlUtil xmlUtil = new XmlUtil();
        Map<String, String> form = new HashMap<String, String>();
        form.put("function", function);
        form.put("reqTime", new Timestamp(System.currentTimeMillis()).toString());
        //reqMsgId每次报文必须都不一样
        form.put("reqMsgId", UUID.randomUUID().toString());
        //商户流水号，银行保证幂等，此值相同第二次进行业务会抛出异常，若不同视为另一笔业务，切记！
        form.put("bizNo", bizNo);
        form.put("bizName", bizName);
        form.put("certNo", certNo);
        //业务层签名规则：对除partner、sign本身之外的业务接口字段进行签名，如已无其他业务接口字段则对空字符串签名；如有则按以下文档中业务字段顺序签名。如有字段a，值A，字段b，值B，则签名原串为”a=A||b=B”,||为分隔符。若a字段值为空则签名原串为”a=||b=B”。
        String sign = Sign.sign("bizNo=" + form.get("bizNo") + "||bizName=" + bizName + "||certNo=" + certNo, false, wangshangConfig.getConfYinqizhilian(), wangshangConfig.getConfTopESA());
        form.put("sign", sign);//签名
        //封装报文
        String param = xmlUtil.format(form, function);
        if (HttpsMain.isSign) {//生产环境需进行rsa签名
            param = XmlSignUtil.sign(param, rsaPrivateKey);
        }
        LOGGER.info("发短信请求报文:{}", param);
        //发送请求
        String response = HttpsMain.httpsReq(wangshangConfig.getReqUrl(), param);
        LOGGER.info("发短信返回报文:{}", response);
        if (HttpsMain.isSign && !XmlSignUtil.verify(response, bankRsaPublicKey)) {
        	//生产环境需进行rsa验签
        	map.put("result", false);
        	map.put("resultMsg", "验签失败");
        	return map;
        }
        //解析报文
        Map<String, Object> resMap = xmlUtil.parse(response, function);
        String resultInfoStr = resMap.get("resultInfo").toString();
        Map<String, String> resultInfoMap = MapUtils.getMap(resultInfoStr);
        if(resultInfoMap.get("resultStatus") != null && "S".equals(resultInfoMap.get("resultStatus"))) {
        	map.put("result", true);
        	map.put("resultMsg", "success");
        }else {
        	map.put("result", false);
        	map.put("resultMsg", resultInfoMap.get("resultMsg"));
        }
        return map;
	}
	
	@Override
	public BatchWithrawInfo selectByTradeNo(String batchNo) {
		return masterBatchWithrawInfoMapper.selectByTradeNo(batchNo);
	}
	
	@Override
	public Map<String, Object> withdrawConfirm(String batchNo, String bizNo, String smsCode, String totalCount, String totalAmount, String currencyCode, String remark) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// 校验余额是否充足
		map = balanceCheck(new BigDecimal(totalAmount).divide(new BigDecimal(100)));
		if(!(boolean)map.get("result")) {
			return map;
		}
		boolean result = false;
        String function = wangshangConfig.getConfirmFunction();
        String fileName = "h2h_batchPay_"+wangshangConfig.getAccountId()+"_"+batchNo+".xls";

        XmlUtil xmlUtil = new XmlUtil();
        Map<String, String> form = new HashMap<String, String>();
        form.put("function", function);
        form.put("reqTime", new Timestamp(System.currentTimeMillis()).toString());
        //reqMsgId每次报文必须都不一样
        form.put("reqMsgId", UUID.randomUUID().toString());
        //商户流水号，银行保证幂等，此值相同第二次进行业务会抛出异常，若不同视为另一笔业务，切记！
        form.put("bizNo", bizNo);
        form.put("smsCode", smsCode);
        form.put("fileName", fileName);
        form.put("totalCount", totalCount);
        form.put("totalAmount", totalAmount);
        form.put("currencyCode", currencyCode);
        form.put("remark", remark);
        form.put("companyCardNo", wangshangConfig.getCardNo());
        //业务层签名规则：对除partner、sign本身之外的业务接口字段进行签名，如已无其他业务接口字段则对空字符串签名；如有则按以下文档中业务字段顺序签名。如有字段a，值A，字段b，值B，则签名原串为”a=A||b=B”,||为分隔符。若a字段值为空则签名原串为”a=||b=B”。
        String signParam = "bizNo=" + form.get("bizNo") + "||smsCode=" + form.get("smsCode") + "||fileName="
                + form.get("fileName") + "||totalCount=" + form.get("totalCount")
                + "||totalAmount=" + form.get("totalAmount") + "||currencyCode="
                + form.get("currencyCode") + "||remark=" + form.get("remark")
                + "||companyCardNo=" + form.get("companyCardNo");
        String sign = Sign.sign(signParam, false, wangshangConfig.getConfYinqizhilian(), wangshangConfig.getConfTopESA());
        form.put("sign", sign);//签名
        //封装报文
        String param = xmlUtil.format(form, function);
        if (HttpsMain.isSign) {//生产环境需进行rsa签名
            param = XmlSignUtil.sign(param, rsaPrivateKey);
        }
        LOGGER.info("代发确认参数报文=>{}", param);
        //发送请求
        String response = HttpsMain.httpsReq(wangshangConfig.getReqUrl(), param);
        LOGGER.info("batchNo={},bizNo={}，代发确认接口返回报文=>{}", batchNo, bizNo, response);
        if (HttpsMain.isSign) {
        	//生产环境需进行rsa验签
            if (!XmlSignUtil.verify(response, bankRsaPublicKey)) {
            	map.put("result", result);
            	map.put("resultMsg", "XmlSignUtil.verify验签失败");
            	return map;
            }
        }
        //解析报文
        Map<String, Object> resMap = xmlUtil.parse(response, function);
        //对于list类型返回数据需要先base64解码，再用JsonArray解析
        Object batchTransNoObj = resMap.get("batchTransNo");
        LOGGER.info("batchTransNo={}", batchTransNoObj);
        Object resultInfo = resMap.get("resultInfo");
        BatchWithrawInfo info = new BatchWithrawInfo();
        info.setTradeNo(batchNo);
        if(batchTransNoObj != null && !StringUtil.isEmpty(batchTransNoObj.toString())) {
        	// 更新batchInfo状态,以及
        	info.setBatchTransNo(batchTransNoObj.toString());
        	info.setState(BatchWithdrawStateEnum.CONFIRMED_SUCCESS.getCode());
        	masterBatchWithrawInfoMapper.updateByTradeNoSelective(info);
        	// 返回batchTransNo后，启动任务开始下载结果文件
        	new Thread(new DownloadingJob(batchNo, batchTransNoObj.toString(), masterBatchWithrawInfoMapper, masterPayDetailMapper, wangshangConfig, mailSender)).start();
        	result = true;
        }else {
        	info.setState(BatchWithdrawStateEnum.CONFIRMED_FAILURE.getCode());
        	masterBatchWithrawInfoMapper.updateByTradeNoSelective(info);
        }
        map.put("resultMsg", MapUtils.getMap(resultInfo.toString()).get("resultMsg"));
        map.put("result", result);
        return map;
	}
	
	// 下载结果文件
	@Override
	public boolean downloadResultFile(String batchTransNo, String batchNo) throws Exception {
		boolean result = true;
		InputStream is = new SFTPUtil(wangshangConfig).downloadResultFile(""+batchTransNo, null);
		ExcelUtil.readExcel(is, batchTransNo, batchNo);
		return result;
	}

}
