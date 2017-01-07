package com.store59.printapi.model.result;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/16
 * @since 1.0
 */
public class Time_slot {
    private String end;
    private String start;

    public void setEnd(String end){
        this.end = end;
    }
    public String getEnd(){
        return this.end;
    }
    public void setStart(String start){
        this.start = start;
    }
    public String getStart(){
        return this.start;
    }
}
