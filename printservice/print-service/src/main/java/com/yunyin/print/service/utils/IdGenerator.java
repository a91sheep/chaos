package com.yunyin.print.service.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/14
 * @since 1.0
 */
public abstract class IdGenerator {
    /**
     * 2017-01-01的毫秒数.
     */
    private static final long BEGIN_MILLISECONDS = 1483200000000L;

    /**
     * 业务标识位数.
     */
    private static final long BIZ_ID_BITS = 7L;

    /**
     * 随机数标识位数.
     */
    private static final long RANDOM_ID_BITS = 15L;

    /**
     * 随机数ID最大值.
     */
    private static final long MAX_RANDOM_ID = -1L ^ (-1L << RANDOM_ID_BITS);

    /**
     * 业务标识左移位数.
     */
    private final static long BIZ_ID_LEFT_SHIFT_BITS = RANDOM_ID_BITS;

    /**
     * 时间戳左移位数.
     */
    private final static long TIMESTAMP_LEFT_SHIFT_BITS = RANDOM_ID_BITS + BIZ_ID_BITS;

    /**
     * 分片值的长度.
     */
    private static final int SHARDING_VALUE_LENGTH = 4;

    /**
     * 默认业务类型
     */
    private static final byte DEFAULT_BIZTYPE = 1;

    /**
     * 生成订单号.
     * <p/>
     * <p>
     * 订单号(23位) = 19位Long类型id ＋ 买家id后4位.
     * </p>
     *
     * @param bizType    业务编码
     * @param buyerId    买家id
     * @param createTime 订单创建时间
     * @return 订单号
     */
    public static String generateId(byte bizType, String buyerId, Date createTime) {
        long id = generateId(bizType, createTime);

        String sharding = StringUtils.leftPad(StringUtils.right(buyerId, SHARDING_VALUE_LENGTH), SHARDING_VALUE_LENGTH, "0");
        return StringUtils.leftPad(String.valueOf(id), 19, "0") + sharding;
    }

    public static String generateId(String buyerId, Date createTime) {
        return generateId(DEFAULT_BIZTYPE, buyerId, createTime);
    }

    /**
     * 64位ID = 42(毫秒) + 7(业务编码) + 15(随机数).
     *
     * @param bizType    业务编码
     * @param createTime 订单创建时间
     * @return id
     */
    private static long generateId(byte bizType, Date createTime) {
        long timestamp = createTime.getTime() - BEGIN_MILLISECONDS;
        long bizId = bizType;
        long randomId = RandomUtils.nextLong(0, MAX_RANDOM_ID + 1);
        return (timestamp << TIMESTAMP_LEFT_SHIFT_BITS) | (bizId << BIZ_ID_LEFT_SHIFT_BITS) | randomId;
    }
}
