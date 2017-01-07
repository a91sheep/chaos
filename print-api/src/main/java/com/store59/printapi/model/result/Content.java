package com.store59.printapi.model.result;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/1/16
 * @since 1.0
 */
public class Content {
    private int[] area;

    private int charge;

    private int threshold;

    private int threshold_switch;

    private List<Time_slot> time_slot;

    public int[] getArea() {
        return area;
    }

    public void setArea(int[] area) {
        this.area = area;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold_switch() {
        return threshold_switch;
    }

    public void setThreshold_switch(int threshold_switch) {
        this.threshold_switch = threshold_switch;
    }

    public List<Time_slot> getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(List<Time_slot> time_slot) {
        this.time_slot = time_slot;
    }
}
