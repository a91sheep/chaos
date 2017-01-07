package com.yunyin.print.main.api.model.param.pc;

import com.yunyin.print.main.api.common.constant.MessageConstant;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/22
 * @since 1.0
 */
public class CreateOrderParam {
    @NotBlank(message = MessageConstant.SITE_ID)
    private Long   siteId;
    @NotBlank(message = MessageConstant.ORDER_DETAILS_PARAM_BLANK)
    private String details;

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
