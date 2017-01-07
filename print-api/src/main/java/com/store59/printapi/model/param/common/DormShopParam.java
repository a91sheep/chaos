package com.store59.printapi.model.param.common;

import com.store59.printapi.common.constant.MessageConstant;

import javax.validation.constraints.NotNull;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 16/1/13
 * @since 1.0
 */
public class DormShopParam {

    @NotNull(message = MessageConstant.Site_Id)
    private Integer siteId;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
}
