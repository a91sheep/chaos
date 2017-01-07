/**
 * 
 */
package com.store59.printapi.common.enums;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月14日
 * @since 1.1
 */
public enum RetCodeEnum {
    UNKNOWN_ERROR(-1, "未知错误"), 
    SUCCESS(0, "成功"), 
    DB_CONNECT_EXCEPTION(1, "数据库连接异常"), 
    DB_EXCUTE_ERROR(2, "数据库执行异常"), 
    CACHE_ERROR(3, "缓存异常"), 
    INVALID_PARAMTERS(4, "参数错误"),    
    JSON_PARSE_ERROR(5, "JSON 转换异常"),
    SIGN_ERROR (6, "sign签名有误"),
    INVALID_TOKEN(7, "token无效"),
    INVALID__LOGIN(8, "登录无效"),
    SERVICE_REQUEST_EXCEPTION(9, "服务请求异常"),
    HAS_NO_RIGHT (10, "当前用户无访问权限"),
    
    // 用户相关
    USERNAME_PSWD_ERROR (3700,"账号和密码不匹配哦～"),
    OLDPASSWORD_ERROR (3701,"原密码错误"),
    
    // 包裹
    PARCEL_NOT_EXISTS (3702, "包裹不在库存中"),
    PARCEL_HAS_STOCKIN (3703, "包裹已入库"),
    PARCEL_HAS_STOCKEDOUT (3704, "包裹已出库"),
    PARCEL_NOT_PUTON (3705, "包裹未上架"),
    PARCELS_CANNOT_PUTON (3706, "包裹无法上架"),
    PARCELS_CANNOT_MOVE (3707, "包裹无法移库"),
    PARCEL_PRINT_INFO_CODE_NOTEXIT (3708, "该站点库存中无匹配的包裹"),
    PARCEL_PRINT_INFO_PROBLEM (3709, "您的包裹状态异常，请联系工作人员！"),
    PARCEL_PRINT_INFO_CODE_ERROR (3710, "不是本站点的取件码"),
    
    // 货架
    SHELFCELL_CODE_NOT_EXISTS (3711, "该条形码的货架层不存在"),
    SHELFCELL_NUM_FULL (3712, "您所在站点的货架层已满，请联系系统管理员"),
    
    // 人员
    USERNAME_EXISTS (3713, "账号已存在");
    
    private int status;
    private String msg;
    
    /**
     * 
     */
    private RetCodeEnum() {
        // TODO Auto-generated constructor stub
    }

    private RetCodeEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public static RetCodeEnum valueOf(Integer status) {
        if (status == null)
            return null;
        for (RetCodeEnum ret : RetCodeEnum.values()) {
            if (ret.getStatus() == status) {
                return ret;
            }
        }
        return null;
    }
}
