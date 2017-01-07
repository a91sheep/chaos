package com.store59.pay.web.form;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 翼支付异步通知消息
 * Created by 凌云
 * lly835@163.com
 * 2016-04-24 18:59
 */
public class TianyipayAsyncNotifyForm {
    /** 翼支付网关平台交易流水号. */
    private String UPTRANSEQ;

    /** 翼支付网关平台交易日期, 格式：yyyyMMDD. */
    private String TRANDATE;

    /** 处理结果码, 结果码为“0000” 表示支付成功，其他值则表示支付失败. */
    private String RETNCODE;

    /** 处理结果解释码, 对支付结果的说明码. */
    private String RETNINFO;

    /** 订单请求交易流水号, 商户传过来的. */
    private String ORDERREQTRANSEQ;

    /** 商户订单号, 商户传过来的. */
    private String ORDERSEQ;

    /** 订单总金额, 单位: 元. */
    private BigDecimal ORDERAMOUNT;

    /** 产品金额, 单位: 元. */
    private String PRODUCTAMOUNT;

    /** 附加金额, 单位: 元. */
    private String ATTACHAMOUNT;

    /** 币种, 默认填 RMB. */
    private String CURTYPE;

    /** 加密方式, 0: 不加密, 1: MD5摘要(默认). */
    private Integer ENCODETYPE;

    /** 商户附加信息. */
    private String ATTACH;

    /** 数字签名(1时有效). */
    private String SIGN;

    /** 商户号. */
    private String MERCHANTID;

    /** 银行编码. */
    private String BANKID;

    /** 产品号, 如：账单、账号、卡号等. */
    private String PRODUCTNO;

    /** 银行账户标识, 账单支付的手机号码. */
    private String BANKACCID;

    /** 订单有效期标志. */
    private String ORDERVALIDITYFLAG;

    public String getUPTRANSEQ() {
        return UPTRANSEQ;
    }

    public void setUPTRANSEQ(String UPTRANSEQ) {
        this.UPTRANSEQ = UPTRANSEQ;
    }

    public String getTRANDATE() {
        return TRANDATE;
    }

    public void setTRANDATE(String TRANDATE) {
        this.TRANDATE = TRANDATE;
    }

    public String getRETNCODE() {
        return RETNCODE;
    }

    public void setRETNCODE(String RETNCODE) {
        this.RETNCODE = RETNCODE;
    }

    public String getRETNINFO() {
        return RETNINFO;
    }

    public void setRETNINFO(String RETNINFO) {
        this.RETNINFO = RETNINFO;
    }

    public String getORDERREQTRANSEQ() {
        return ORDERREQTRANSEQ;
    }

    public void setORDERREQTRANSEQ(String ORDERREQTRANSEQ) {
        this.ORDERREQTRANSEQ = ORDERREQTRANSEQ;
    }

    public String getORDERSEQ() {
        return ORDERSEQ;
    }

    public void setORDERSEQ(String ORDERSEQ) {
        this.ORDERSEQ = ORDERSEQ;
    }

    public BigDecimal getORDERAMOUNT() {
        return ORDERAMOUNT;
    }

    public void setORDERAMOUNT(BigDecimal ORDERAMOUNT) {
        this.ORDERAMOUNT = ORDERAMOUNT;
    }

    public String getPRODUCTAMOUNT() {
        return PRODUCTAMOUNT;
    }

    public void setPRODUCTAMOUNT(String PRODUCTAMOUNT) {
        this.PRODUCTAMOUNT = PRODUCTAMOUNT;
    }

    public String getATTACHAMOUNT() {
        return ATTACHAMOUNT;
    }

    public void setATTACHAMOUNT(String ATTACHAMOUNT) {
        this.ATTACHAMOUNT = ATTACHAMOUNT;
    }

    public String getCURTYPE() {
        return CURTYPE;
    }

    public void setCURTYPE(String CURTYPE) {
        this.CURTYPE = CURTYPE;
    }

    public Integer getENCODETYPE() {
        return ENCODETYPE;
    }

    public void setENCODETYPE(Integer ENCODETYPE) {
        this.ENCODETYPE = ENCODETYPE;
    }

    public String getATTACH() {
        return ATTACH;
    }

    public void setATTACH(String ATTACH) {
        this.ATTACH = ATTACH;
    }

    public String getSIGN() {
        return SIGN;
    }

    public void setSIGN(String SIGN) {
        this.SIGN = SIGN;
    }

    public String getMERCHANTID() {
        return MERCHANTID;
    }

    public void setMERCHANTID(String MERCHANTID) {
        this.MERCHANTID = MERCHANTID;
    }

    public String getBANKID() {
        return BANKID;
    }

    public void setBANKID(String BANKID) {
        this.BANKID = BANKID;
    }

    public String getPRODUCTNO() {
        return PRODUCTNO;
    }

    public void setPRODUCTNO(String PRODUCTNO) {
        this.PRODUCTNO = PRODUCTNO;
    }

    public String getBANKACCID() {
        return BANKACCID;
    }

    public void setBANKACCID(String BANKACCID) {
        this.BANKACCID = BANKACCID;
    }

    public String getORDERVALIDITYFLAG() {
        return ORDERVALIDITYFLAG;
    }

    public void setORDERVALIDITYFLAG(String ORDERVALIDITYFLAG) {
        this.ORDERVALIDITYFLAG = ORDERVALIDITYFLAG;
    }

    @Override
    public String toString() {
        return "TianyipayAsyncNotifyForm{" +
                "UPTRANSEQ='" + UPTRANSEQ + '\'' +
                ", TRANDATE=" + TRANDATE +
                ", RETNCODE='" + RETNCODE + '\'' +
                ", RETNINFO='" + RETNINFO + '\'' +
                ", ORDERREQTRANSEQ='" + ORDERREQTRANSEQ + '\'' +
                ", ORDERSEQ='" + ORDERSEQ + '\'' +
                ", ORDERAMOUNT='" + ORDERAMOUNT + '\'' +
                ", PRODUCTAMOUNT='" + PRODUCTAMOUNT + '\'' +
                ", ATTACHAMOUNT='" + ATTACHAMOUNT + '\'' +
                ", CURTYPE='" + CURTYPE + '\'' +
                ", ENCODETYPE=" + ENCODETYPE +
                ", ATTACH='" + ATTACH + '\'' +
                ", SIGN='" + SIGN + '\'' +
                ", MERCHANTID='" + MERCHANTID + '\'' +
                ", BANKID='" + BANKID + '\'' +
                ", PRODUCTNO='" + PRODUCTNO + '\'' +
                ", BANKACCID='" + BANKACCID + '\'' +
                ", ORDERVALIDITYFLAG='" + ORDERVALIDITYFLAG + '\'' +
                '}';
    }
}
