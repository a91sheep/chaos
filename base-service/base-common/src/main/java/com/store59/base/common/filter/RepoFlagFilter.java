/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.store59.base.common.filter;

import java.io.Serializable;
import java.util.List;

/**
 * repoFlag的filter
 *
 * @author <a href="mailto:zhongc@59store.com">士兵</a>
 * @version 1.0 15/11/17
 * @since 1.0
 */
public class RepoFlagFilter implements Serializable{

    private List<Integer> flagIds;  //flagId集合

    private Integer status;         //状态

    public List<Integer> getFlagIds() {
        return flagIds;
    }

    public void setFlagIds(List<Integer> flagIds) {
        this.flagIds = flagIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
