package com.store59.printapi.model.param.ad;

import com.store59.printapi.common.constant.MessageConstant;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/4/20
 * @since 1.0
 */
public class AdFreeParam {
    private Integer siteId;
    @NotBlank(message = MessageConstant.ORDER_DETAILS_PARAM_BLANK)
    private String  details;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
