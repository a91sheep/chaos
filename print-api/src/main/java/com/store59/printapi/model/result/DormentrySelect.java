/**
 * 
 */
package com.store59.printapi.model.result;

import java.util.List;

import com.store59.base.common.model.Dormentry;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月14日
 * @since 1.1
 */
public class DormentrySelect {
    private List<Dormentry> contents;

    public List<Dormentry> getContents() {
        return contents;
    }

    public void setContents(List<Dormentry> contents) {
        this.contents = contents;
    }
}
