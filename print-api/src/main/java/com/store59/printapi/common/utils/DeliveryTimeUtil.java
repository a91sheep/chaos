package com.store59.printapi.common.utils;

import com.store59.dorm.common.model.DormShop;
import com.store59.dorm.common.model.DormShopTime;
import com.store59.kylin.common.model.Result;
import com.store59.printapi.common.enums.DeliveryTypeEnum;
import com.store59.printapi.common.enums.DormShopStatusEnum;
import com.store59.printapi.common.enums.DormShopTimeStatusEnum;
import com.store59.printapi.common.enums.ResultStatus;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.app.ShopTimeInfo;
import com.store59.printapi.model.result.app.ShopTimeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/24
 * @since 1.0
 */
@Component
public class DeliveryTimeUtil {
    //    @Autowired
//    private DormShopApi dormShopService;
    private Logger logger = LoggerFactory.getLogger(DeliveryTimeUtil.class);

    public Datagram<ShopTimeView> getExpectTimeList(Result<DormShop> dormShopResult) {
        //根据shopId得到店铺的明细，主要使用店铺的营业状态
//        Result<DormShop> dormShopResult = dormShopService.findByShopId(shopId, true, false, false, false);
        if (ResultStatus.SUCCESS.getType() != dormShopResult.getStatus()) {
            logger.info("获取店铺信息失败:" + dormShopResult.getMsg());
            return DatagramHelper.getDatagram(dormShopResult.getStatus(), dormShopResult.getMsg());
        }
        DormShop dormShop = dormShopResult.getData();
        if (dormShop == null) {
            return DatagramHelper.getDatagram(ResultStatus.SUCCESS.getType(), "该店铺不存在");
        }
        ShopTimeView shopTimeView = new ShopTimeView();

        if (dormShop.getStatus() == DormShopStatusEnum.CANCEL.getType().byteValue()) {
            return DatagramHelper.getDatagram(ResultStatus.SUCCESS.getType(), "店铺已经不再经营");
        }

        if (dormShop.getStatus() == DormShopStatusEnum.CLOSED.getType().byteValue()) {
            return DatagramHelper.getDatagram(shopTimeView, ResultStatus.SUCCESS.getType(), "配送时间段获取成功");
        }

        Set<ShopTimeInfo> shopTimeBeanList = new LinkedHashSet<>();

        if (dormShop.getStatus() == DormShopStatusEnum.OPENED.getType().byteValue()) {
            ShopTimeInfo shopTimeBean = new ShopTimeInfo();
            shopTimeBean.setType(DeliveryTypeEnum.NOW.getType());
            shopTimeBean.setName(DeliveryTypeEnum.NOW.getDesc());
            shopTimeBeanList.add(shopTimeBean);
            shopTimeView.setDelivery_times(new ArrayList<>(shopTimeBeanList));
            return DatagramHelper.getDatagram(shopTimeView, ResultStatus.SUCCESS.getType(), "配送时间段获取成功");
        }

        if (dormShop.getStatus() == DormShopStatusEnum.AUTO.getType().byteValue()) {
            List<DormShopTime> dormShopTimeList = dormShop.getDormShopTimes();
            if (CollectionUtils.isEmpty(dormShopTimeList)) {
                return DatagramHelper.getDatagram(ResultStatus.NORMAL_ERROR.getType(), "该店铺无配送时间");
            }

            DormShopStatusEnum shopAutoStatus = getShopAutoStatus(dormShopTimeList);
            ShopTimeInfo shopTimeBean = new ShopTimeInfo();
            if (shopAutoStatus == DormShopStatusEnum.OPENED) {
                shopTimeBean.setType(DeliveryTypeEnum.NOW.getType());
                shopTimeBean.setName(DeliveryTypeEnum.NOW.getDesc());
                shopTimeBeanList.add(shopTimeBean);
            }
            //时间间隔
            Set<ShopTimeInfo> shopTimeBeanListAll = new LinkedHashSet<>();
            int interval = 15 * 60;
            dormShopTimeList.stream().filter(e -> e.getTimeSwitch().intValue() == DormShopTimeStatusEnum.ON.ordinal())
                    .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime())).forEach(e -> {
                long startTime = getTimestampByHm(e.getStartTime());
                long endTime = getTimestampByHm(e.getEndTime());
                double frequency = new BigDecimal(endTime - startTime)
                        .divide(new BigDecimal(interval), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
                for (int i = 0; i < frequency; i++) {
                    //得到第n个15分钟起止时间
                    long startTimeInside = startTime;
                    long endTimeInside = startTime + interval;
                    if (endTimeInside > endTime) {
                        endTimeInside = endTime;
                    }
                    ShopTimeInfo shopTime = new ShopTimeInfo();
                    shopTime.setType(DeliveryTypeEnum.BOOKING.getType());
                    shopTime.setName(getHmByTimestamp(startTimeInside) + "~" +
                            getHmByTimestamp(endTimeInside));
                    shopTime.setExpect_start_time(startTimeInside);
                    shopTime.setExpect_end_time(endTimeInside);
                    shopTimeBeanListAll.add(shopTime);
                    if (startTimeInside > System.currentTimeMillis() / 1000) {
                        shopTimeBeanList.add(shopTime);
                    }
                    startTime = endTimeInside;
                }
            });
            if (CollectionUtils.isEmpty(shopTimeBeanList)) {
                shopTimeBeanList.addAll(shopTimeBeanListAll);
            }
            shopTimeView.setDelivery_times(new ArrayList<>(shopTimeBeanList));
        }

        return DatagramHelper.getDatagram(shopTimeView, ResultStatus.SUCCESS.getType(), "配送时间段获取成功");
    }


    /**
     * 根据配送事件得到店铺配送状态
     *
     * @param dormShopTimeList
     * @return
     */
    private DormShopStatusEnum getShopAutoStatus(List<DormShopTime> dormShopTimeList) {
        if (CollectionUtils.isEmpty(dormShopTimeList)) {
            return DormShopStatusEnum.CLOSED;
        }
        dormShopTimeList = dormShopTimeList.stream().filter(e -> e.getTimeSwitch().intValue() == DormShopTimeStatusEnum.ON.getType())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dormShopTimeList)) {
            return DormShopStatusEnum.CLOSED;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        for (DormShopTime dormShopTime : dormShopTimeList) {
            //日历对象
            Calendar calendar = Calendar.getInstance();
            String now = format.format(calendar.getTime());
            //截取小时和分钟
            Integer nowTime = Integer.valueOf(now.substring(now.length() - 4));

            if (nowTime > dormShopTime.getStartTime() && nowTime < dormShopTime.getEndTime()) {
                return DormShopStatusEnum.OPENED;
            }
        }
        return DormShopStatusEnum.CLOSED;
    }

    /**
     * 根据小时分钟，得到当天的时间戳
     *
     * @param hourMinute 小时分钟<例：2100 即 21:00  900 即 9:00>
     * @return
     */
    private long getTimestampByHm(Integer hourMinute) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        //日历对象
        Calendar calendar = Calendar.getInstance();
        StringBuilder now = new StringBuilder().append(format.format(calendar.getTime()));

        //如果时间不足4位数,需要在前面加0
        DecimalFormat df = new DecimalFormat("0000");
        String hm = df.format(hourMinute);

        now.replace(8, now.length(), new StringBuffer().append(hm).append("00").toString());
        Date date = new Date();
        try {
            date = format.parse(now.toString());
        } catch (ParseException e) {
            logger.error("入参格式hourMinute有误{}", hourMinute);
        }
        return date.getTime() / 1000;

    }

    private String getHmByTimestamp(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(timestamp * 1000);
        StringBuilder hm = new StringBuilder(time.substring(8, 12)).insert(2, ":");
        return hm.toString();
    }
}
