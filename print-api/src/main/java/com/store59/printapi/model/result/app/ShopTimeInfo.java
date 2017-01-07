package com.store59.printapi.model.result.app;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/24
 * @since 1.0
 */
public class ShopTimeInfo {
    /* 送货状态  */
    private Integer type;

    private String name;

    private long expect_start_time;

    private long expect_end_time;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExpect_start_time() {
        return expect_start_time;
    }

    public void setExpect_start_time(long expect_start_time) {
        this.expect_start_time = expect_start_time;
    }

    public long getExpect_end_time() {
        return expect_end_time;
    }

    public void setExpect_end_time(long expect_end_time) {
        this.expect_end_time = expect_end_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShopTimeInfo that = (ShopTimeInfo) o;

        if (expect_start_time != that.expect_start_time) return false;
        if (expect_end_time != that.expect_end_time) return false;
        if (!type.equals(that.type)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (int) (expect_start_time ^ (expect_start_time >>> 32));
        result = 31 * result + (int) (expect_end_time ^ (expect_end_time >>> 32));
        return result;
    }
}
