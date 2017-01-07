/**
 * 
 */
package com.store59.printapi.model.result;

import java.util.List;

/**
 * 
 * @author <a href="mailto:zhouxq@59store.com">山泉</a>
 * @version 1.1 2016年1月14日
 * @since 1.1
 */
public class SelectBox {
    private List<SelectBoxInfo> contents;

    public List<SelectBoxInfo> getContents() {
        return contents;
    }

    public void setContents(List<SelectBoxInfo> contents) {
        this.contents = contents;
    }
}
