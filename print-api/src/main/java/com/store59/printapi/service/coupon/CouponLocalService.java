package com.store59.printapi.service.coupon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store59.ad.common.api.AdApi;
import com.store59.base.common.api.DormentryApi;
import com.store59.base.common.model.Dormentry;
import com.store59.coupon.enums.CouponAllowEnum;
import com.store59.coupon.enums.CouponScope;
import com.store59.coupon.enums.CouponStatus;
import com.store59.coupon.model.businessObject.Coupon;
import com.store59.coupon.model.businessObject.CouponAllow;
import com.store59.coupon.model.param.CouponAllowParam;
import com.store59.coupon.model.param.CouponQueryParam;
import com.store59.coupon.model.param.CouponUseParam;
import com.store59.coupon.remoting.CouponService;
import com.store59.dorm.common.api.DormEntryShopApi;
import com.store59.dorm.common.api.DormShopApi;
import com.store59.dorm.common.api.DormShopDeliveryApi;
import com.store59.dorm.common.api.DormShopPriceApi;
import com.store59.dorm.common.data.ConstDorm;
import com.store59.dorm.common.filter.DormEntryShopFilter;
import com.store59.dorm.common.model.*;
import com.store59.kylin.common.exception.BaseException;
import com.store59.kylin.common.model.Result;
import com.store59.kylin.utils.JsonUtil;
import com.store59.print.common.data.PrintConst;
import com.store59.printapi.common.constant.CommonConstant;
import com.store59.printapi.common.exception.AppException;
import com.store59.printapi.common.utils.DatagramHelper;
import com.store59.printapi.common.utils.DeliveryTimeUtil;
import com.store59.printapi.common.utils.StringUtil;
import com.store59.printapi.model.param.coupon.AppCoupon;
import com.store59.printapi.model.param.createOrder.AppCreateOrderDetailParam;
import com.store59.printapi.model.param.createOrder.AppCreateOrderParam;
import com.store59.printapi.model.result.Datagram;
import com.store59.printapi.model.result.app.AppCreateOrderResult;
import com.store59.printapi.model.result.app.ShopTimeInfo;
import com.store59.printapi.model.result.app.ShopTimeView;
import com.store59.printapi.model.result.order.AdFreeResult;
import com.store59.printapi.model.result.order.AppCouponList;
import com.store59.printapi.service.BaseService;
import com.store59.printapi.service.createOrder.AdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/3/11
 * @since 1.0
 */
@Service
public class CouponLocalService extends BaseService {

    @Autowired
    private CouponService                   couponService;
    @Autowired
    private DormShopPriceApi                dormShopPriceApi;
    @Autowired
    private DormShopDeliveryApi             dormShopDeliveryApi;
    @Autowired
    private DormShopApi                     dormShopService;
    @Autowired
    private DormentryApi                    dormentryService;
    @Value("${store59.footer.num.limit}")
    private int                             footerNumLimit;
    @Autowired
    private AdApi                           adApi;
    @Autowired
    private DormEntryShopApi                dormentryShopService;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOpsCache;
    @Autowired
    private AdService                       adService;

    @Value("${cookie.max.age}")
    private int cookieMaxAge;

    private Logger logger = LoggerFactory.getLogger(CouponLocalService.class);

    public Datagram<?> getCouponListByUid(Long uid, String isAll, Double amount, String type) {
        CouponQueryParam couponQuery = new CouponQueryParam();
        couponQuery.setUid(uid);
        List<String> scopes = new ArrayList<>();
        // scopes.add(String.valueOf(CouponScope.COMMON.getScope())); // 通用优惠券
        scopes.add(String.valueOf(CouponScope.PRINTER.getScope())); // 打印店优惠券
        List<CouponAllowParam> allows = new ArrayList<CouponAllowParam>();
        CouponAllowParam allowPara = new CouponAllowParam();
        allowPara.setAllowType(CouponAllowEnum.ALLOW_BUSINESS);
        allowPara.setAllowValues(scopes);// 使用范围
        allows.add(allowPara);

        if ("no".equals(isAll)) { // 需要过滤使用金额门槛
            CouponAllowParam allowAmount = new CouponAllowParam();
            allowAmount.setAllowType(CouponAllowEnum.ALLOW_THRESHOLD);
            List<String> amountsList = new ArrayList<>();
            amountsList.add(String.valueOf(amount));
            allowAmount.setAllowValues(amountsList);// 金额门槛
            allows.add(allowAmount);
            couponQuery.setCouponAllowParams(allows);
            couponQuery.setActiveStatus(CommonConstant.COUPON_ACTIVE_STATUS_1);
            couponQuery.setExpireStatus(CommonConstant.COUPON_EXPIRE_STATUS_0);
            couponQuery.setStatus(CouponStatus.UNUSED.getStatus());

            Result<List<Coupon>> result = couponService.getCouponList(couponQuery);
            if (result == null || result.getStatus() != 0) {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            }
            if (result.getData() == null) {
                return DatagramHelper.getDatagramWithSuccess(new ArrayList<AppCoupon>());
            }

            List<Coupon> retList = result.getData();
            List<Coupon> fResult = new ArrayList<Coupon>();
            List<Coupon> fResult_used = new ArrayList<Coupon>();
            List<Coupon> fResult_exp = new ArrayList<Coupon>();
            for (Coupon fr : retList) {
                // 判断优惠券类型是否对
                boolean tmp = true;
                if (fr.getExtension() == null
                        || (fr.getExtension() != null
                        && (fr.getExtension().equals("0") || fr.getExtension().equals("")))
                        || (fr.getExtension() != null && !fr.getExtension().equals("1")
                        && !fr.getExtension().equals("2"))) {

                } else {
                    if ((fr.getExtension() != null && fr.getExtension().equals("0")
                            || (!type.equals("0") && ((fr.getExtension() != null && !fr.getExtension().equals(type)
                            && !fr.getExtension().equals("0"))
                            || (fr.getExtension() == null && !type.equals("2")))))) {
                        tmp = false;
                    }
                }
                if (tmp) {
                    if (fr.getExpireTime().getTime() < System.currentTimeMillis()) {
                        fResult_exp.add(fr);
                    } else {
                        if (fr.getStatus() != 0) {
                            fResult_used.add(fr);
                        } else {
                            fResult.add(fr);
                        }
                    }
                }
            }
            Collections.sort(fResult, new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {

                    if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                        return -1;
                    }
                    if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                        if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                            return -1;
                        }
                    }
                    return 1;
                }
            });
            Collections.sort(fResult_exp, new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {

                    if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                        return -1;
                    }
                    if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                        if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                            return -1;
                        }
                    }
                    return 1;
                }
            });
            Collections.sort(fResult_used, new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {

                    if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                        return -1;
                    }
                    if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                        if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                            return -1;
                        }
                    }
                    return 1;
                }
            });

            if (fResult_used.size() > 0)
                fResult.addAll(fResult_used);
            if (fResult_exp.size() > 0)
                fResult.addAll(fResult_exp);
            return DatagramHelper.getDatagramWithSuccess(fResult);
        }
        couponQuery.setCouponAllowParams(allows);
        Result<List<Coupon>> result = couponService.getCouponList(couponQuery);
        if (result == null || result.getStatus() != 0) {
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        if (result.getData() == null) {
            return DatagramHelper.getDatagramWithSuccess(new ArrayList<AppCoupon>());
        }

        List<Coupon> fResult = new ArrayList<Coupon>();
        List<Coupon> fResult_used = new ArrayList<Coupon>();
        List<Coupon> fResult_exp = new ArrayList<Coupon>();
        for (Coupon fr : result.getData()) {
            // 判断优惠券类型是否对
            if (fr.getExpireTime().getTime() < System.currentTimeMillis()) {
                fResult_exp.add(fr);
            } else {
                if (fr.getStatus() != 0) {
                    fResult_used.add(fr);
                } else {
                    fResult.add(fr);
                }
            }
        }
        Collections.sort(fResult, new Comparator<Coupon>() {
            @Override
            public int compare(Coupon o1, Coupon o2) {

                if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                    return -1;
                }
                if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                    if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                        return -1;
                    }
                }
                return 1;
            }
        });
        Collections.sort(fResult_exp, new Comparator<Coupon>() {
            @Override
            public int compare(Coupon o1, Coupon o2) {

                if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                    return -1;
                }
                if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                    if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                        return -1;
                    }
                }
                return 1;
            }
        });
        Collections.sort(fResult_used, new Comparator<Coupon>() {
            @Override
            public int compare(Coupon o1, Coupon o2) {

                if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                    return -1;
                }
                if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                    if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                        return -1;
                    }
                }
                return 1;
            }
        });

        if (fResult_used.size() > 0)
            fResult.addAll(fResult_used);
        if (fResult_exp.size() > 0)
            fResult.addAll(fResult_exp);
        return DatagramHelper.getDatagramWithSuccess(fResult);
    }

    public Coupon getCouponByCodeAndUid(Long uid, String code, Double amount) {
        CouponQueryParam couponQuery = new CouponQueryParam();
        couponQuery.setUid(uid);
        couponQuery.setActiveStatus(CommonConstant.COUPON_ACTIVE_STATUS_1);
        couponQuery.setExpireStatus(CommonConstant.COUPON_EXPIRE_STATUS_0);
        couponQuery.setStatus(CouponStatus.UNUSED.getStatus());
        couponQuery.setCode(code); // 优惠券号
        List<String> scopes = new ArrayList<>();
        // scopes.add(String.valueOf(CouponScope.COMMON.getScope())); // 通用优惠券
        scopes.add(String.valueOf(CouponScope.PRINTER.getScope())); // 打印店优惠券
        List<CouponAllowParam> allows = new ArrayList<CouponAllowParam>();
        CouponAllowParam allowPara = new CouponAllowParam();
        allowPara.setAllowType(CouponAllowEnum.ALLOW_BUSINESS);
        allowPara.setAllowValues(scopes);// 使用范围
        allows.add(allowPara);
        CouponAllowParam allowAmount = new CouponAllowParam();
        allowAmount.setAllowType(CouponAllowEnum.ALLOW_THRESHOLD);
        List<String> amountsList = new ArrayList<>();
        amountsList.add(String.valueOf(amount));
        allowAmount.setAllowValues(amountsList);// 金额门槛
        allows.add(allowAmount);
        couponQuery.setCouponAllowParams(allows);
        Result<List<Coupon>> result = couponService.getCouponList(couponQuery);
        if (result.getStatus() != 0 || result.getData() == null || result.getData().size() <= 0) {
//			throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, "优惠券不可用！");
            return null;
        }
        return result.getData().get(0);
    }

    public Coupon getBestCouponByUidAndAmount(Long uid, Double amount) {
        Coupon coupon = null;
        CouponQueryParam couponQuery = new CouponQueryParam();
        couponQuery.setUid(uid);
        // couponQuery.setStatus(CouponStatus.UNUSED.getStatus()); // 未使用
        // couponQuery.setCouponStatus(CouponStatus.UNUSED.getStatus()); // 未使用
        // couponQuery.setActiveStatus(CommonConstant.COUPON_ACTIVE_STATUS_1);
        // // 已启用
        // couponQuery.setExpireStatus(CommonConstant.COUPON_EXPIRE_STATUS_0);
        // // 未过期
        couponQuery.setActiveStatus(CommonConstant.COUPON_ACTIVE_STATUS_1);
        couponQuery.setExpireStatus(CommonConstant.COUPON_EXPIRE_STATUS_0);
        couponQuery.setStatus(CouponStatus.UNUSED.getStatus());
        List<String> scopes = new ArrayList<>();
        // scopes.add(String.valueOf(CouponScope.COMMON.getScope())); // 通用优惠券
        scopes.add(String.valueOf(CouponScope.PRINTER.getScope())); // 打印店优惠券
        List<CouponAllowParam> allows = new ArrayList<CouponAllowParam>();
        CouponAllowParam allowPara = new CouponAllowParam();
        allowPara.setAllowType(CouponAllowEnum.ALLOW_BUSINESS);
        allowPara.setAllowValues(scopes);// 使用范围
        allows.add(allowPara);
        CouponAllowParam allowAmount = new CouponAllowParam();
        allowAmount.setAllowType(CouponAllowEnum.ALLOW_THRESHOLD);
        List<String> amountsList = new ArrayList<>();
        amountsList.add(String.valueOf(amount));
        allowAmount.setAllowValues(amountsList);// 金额门槛
        allows.add(allowAmount);
        couponQuery.setCouponAllowParams(allows);
        Result<List<Coupon>> result = couponService.getCouponList(couponQuery);
        if (result.getStatus() != 0) {
            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, result.getMsg());
        }
        List<Coupon> list = result.getData();

        if (list != null && list.size() > 0) {
            List<Coupon> filterList = new ArrayList<>();
            if (filterList.size() == 0) {
                return null;
            }
            // 对金额排序
            logger.info("对优惠券的金额从高到低排序");
            Collections.sort(filterList, new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {
                    if (o2.getDiscount().compareTo(o1.getDiscount()) > 0) {
                        return 1;
                    }
                    return -1;
                }
            });
            double maxDisCount = filterList.get(0).getDiscount().doubleValue();

            // 如果有相同最大金额,取最早过期的
            List<Coupon> time_list = new ArrayList<>();
            for (Coupon coupon1 : filterList) {
                if (coupon1.getDiscount().doubleValue() == maxDisCount) {
                    time_list.add(coupon1);
                } else {
                    break;
                }
            }
            if (time_list.size() == 1) {
                coupon = time_list.get(0);
            } else {
                logger.info("对优惠券的最大相同金额按过期时间从小到大排序");
                Collections.sort(time_list, new Comparator<Coupon>() {
                    @Override
                    public int compare(Coupon o1, Coupon o2) {
                        if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() > 0) {
                            return 1;
                        }
                        return -1;
                    }
                });
                coupon = time_list.get(0);
            }
        }
        return coupon;
    }

    public boolean useCoupon(Long uid, String code, String useOrderId, Double amount, Integer siteId, String shopId) {
        CouponUseParam couponUse = new CouponUseParam();
        couponUse.setCode(code);
        // couponUse.setStatus(CouponStatus.USED.getStatus());
        couponUse.setUseUid(uid);
        couponUse.setUseOrderId(useOrderId);
        couponUse.setUseOrderType(7); // 7表示打印店,等服务层更新枚举后再使用枚举值
        List<String> scopes = new ArrayList<>();
        // scopes.add(String.valueOf(CouponScope.COMMON.getScope())); // 通用优惠券
        scopes.add(String.valueOf(CouponScope.PRINTER.getScope())); // 打印店优惠券
        List<CouponAllowParam> allows = new ArrayList<CouponAllowParam>();
        CouponAllowParam allowPara = new CouponAllowParam();
        allowPara.setAllowType(CouponAllowEnum.ALLOW_BUSINESS);
        allowPara.setAllowValues(scopes);// 使用范围
        allows.add(allowPara);
        CouponAllowParam allowAmount = new CouponAllowParam();
        allowAmount.setAllowType(CouponAllowEnum.ALLOW_THRESHOLD);
        List<String> amountsList = new ArrayList<>();
        amountsList.add(String.valueOf(amount));
        allowAmount.setAllowValues(amountsList);// 金额门槛
        allows.add(allowAmount);

        CouponAllowParam allowSite = new CouponAllowParam();
        allowSite.setAllowType(CouponAllowEnum.ALLOW_SCHOOL);
        List<String> siteList = new ArrayList<>();
        siteList.add(String.valueOf(siteId));
        allowSite.setAllowValues(siteList);
        allows.add(allowSite);

        CouponAllowParam allowshop = new CouponAllowParam();
        allowshop.setAllowType(CouponAllowEnum.ALLOW_SHOP);
        List<String> shopList = new ArrayList<>();
        shopList.add(shopId);
        allowshop.setAllowValues(shopList);
        allows.add(allowshop);

        couponUse.setCouponAllowParams(allows);
        Result<List<Coupon>> result = couponService.useCoupon(couponUse);
        if (result.getStatus() != 0) {
            logger.info("更新优惠券状态为已使用失败!!!优惠券code:{}", couponUse.getCode());
            throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, result.getMsg());
        }
        logger.info("更新优惠券状态为已使用结果：" + JsonUtil.getJsonFromObject(result));
        return true;
    }

    public boolean useCoupon_app(Long uid, String code, String useOrderId, Double amount, Integer siteId,
                                 String shopId) {
        CouponUseParam couponUse = new CouponUseParam();
        couponUse.setCode(code);
        // couponUse.setStatus(CouponStatus.USED.getStatus());
        couponUse.setUseUid(uid);
        couponUse.setUseOrderId(useOrderId);
        couponUse.setUseOrderType(7); // 7表示打印店,等服务层更新枚举后再使用枚举值
        List<String> scopes = new ArrayList<>();
        // scopes.add(String.valueOf(CouponScope.COMMON.getScope())); // 通用优惠券
        scopes.add(String.valueOf(CouponScope.PRINTER.getScope())); // 打印店优惠券
        List<CouponAllowParam> allows = new ArrayList<CouponAllowParam>();
        CouponAllowParam allowPara = new CouponAllowParam();
        allowPara.setAllowType(CouponAllowEnum.ALLOW_BUSINESS);
        allowPara.setAllowValues(scopes);// 使用范围
        allows.add(allowPara);
        CouponAllowParam allowAmount = new CouponAllowParam();
        allowAmount.setAllowType(CouponAllowEnum.ALLOW_THRESHOLD);
        List<String> amountsList = new ArrayList<>();
        amountsList.add(String.valueOf(amount));
        allowAmount.setAllowValues(amountsList);// 金额门槛
        allows.add(allowAmount);

        CouponAllowParam allowSite = new CouponAllowParam();
        allowSite.setAllowType(CouponAllowEnum.ALLOW_SCHOOL);
        List<String> siteList = new ArrayList<>();
        siteList.add(String.valueOf(siteId));
        allowSite.setAllowValues(siteList);
        allows.add(allowSite);

        CouponAllowParam allowshop = new CouponAllowParam();
        allowshop.setAllowType(CouponAllowEnum.ALLOW_SHOP);
        List<String> shopList = new ArrayList<>();
        shopList.add(shopId);
        allowshop.setAllowValues(shopList);
        allows.add(allowshop);

        couponUse.setCouponAllowParams(allows);
        Result<List<Coupon>> result = couponService.useCoupon(couponUse);
        if (result.getStatus() != 0) {
            logger.info("更新优惠券状态为已使用失败!!!优惠券code:{}", JsonUtil.getJsonFromObject(result));
            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION, result.getMsg());
        }
        logger.info("更新优惠券状态为已使用结果：" + JsonUtil.getJsonFromObject(result));
        return true;
    }

    public Datagram<AppCreateOrderResult> getUsefulCoupon(AppCreateOrderParam param, Long uid) {
        AppCreateOrderResult createOrderResult = new AppCreateOrderResult();
        createOrderResult.setDoc_type((int) param.getDoc_type());
        // 0不开启 1开启免费打印
        logger.info("创建购物车,items = {}", param.getItems().toString());
        List<AppCreateOrderDetailParam> list = JsonUtil.getObjectFromJson(param.getItems().toString(),
                new TypeReference<List<AppCreateOrderDetailParam>>() {
                });

        if (param.getOpen_ad() == null) {
            param.setOpen_ad((byte) 0);
        }
        int bw_page_num = 0;
        int free_page_num = 0;
//		if (param.getOpen_ad().byteValue() == CommonConstant.AD_STATUS_OPEN) {}

        // 获取学校id
        // 获取楼栋信息
        DormEntryShopFilter dormEntryShopFilter = new DormEntryShopFilter();
        dormEntryShopFilter.setShopId(Integer.valueOf(param.getShop_id()));
        Result<List<DormEntryShop>> entryRet = dormentryShopService.findByFilter(dormEntryShopFilter);
        if (entryRet.getStatus() != 0 || entryRet.getData().size() == 0) {
            logger.error("获取用户所在楼栋信息失败,楼栋id=" + param.getDormentry_id());
            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        DormEntryShop dormEntryShop = entryRet.getData().get(0);
        Result<Dormentry> dormentryResult = null;
        String dormentryStr = valueOpsCache
                .get(CommonConstant.PRINT_OPTYMIZE_DROMENTRYS + dormEntryShop.getDormentryId());
        if (StringUtil.isBlank(dormentryStr)) {
            dormentryResult = dormentryService.getDormentry(dormEntryShop.getDormentryId());
            if (dormentryResult.getStatus() != 0) {
                logger.error("获取用户所在楼栋信息失败,楼栋id=" + dormEntryShop.getDormentryId());
                throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            } else {
                valueOpsCache.set(CommonConstant.PRINT_OPTYMIZE_DROMENTRYS + dormEntryShop.getDormentryId(),
                        JsonUtil.getJsonFromObject(dormentryResult.getData()), cookieMaxAge + 10, TimeUnit.SECONDS);
            }
        } else {
            dormentryResult = new Result<Dormentry>();
            dormentryResult.setData(JsonUtil.getObjectFromJson(dormentryStr, Dormentry.class));
        }
        // TODO 广告内容
        AdFreeResult adFreeResult = adService.calFreePagesApp(param, dormentryResult.getData().getSiteId(),
                new HashMap<>(), uid, 1);
        free_page_num = adFreeResult.getFreePages();


        // 获取店铺信息&此处添加缓存
        Result<DormShop> result = null;
        result = dormShopService.findByShopId(Integer.valueOf(param.getShop_id()), true, false, true, true);// 包含店铺价格,配送信息
        if (result.getData().getStatus() == ConstDorm.DORM_SHOP_STATUS_CLOSE) {
            throw new AppException(CommonConstant.STATUS_DORM_CLOSED_ERROR, CommonConstant.MSG_DORM_CLOSED_ERROR);
        }
        if (result.getStatus() != 0) {
            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        // 店铺价格
        List<DormShopPrice> dormShopPrices = result.getData().getDormShopPrices();
        if (dormShopPrices == null || dormShopPrices.size() == 0) {
            throw new AppException(CommonConstant.STATUS_SHOPPRINCE_ISBLANK, "店铺打印价格为空");
        }
        Map<String, Double> priceMap = new HashMap<>();
        for (DormShopPrice shopPrice : dormShopPrices) {
            priceMap.put(shopPrice.getType() + "", shopPrice.getUnitPrice());
        }

        createOrderResult.setDelivery_type(param.getDelivery_type());
        // 处理详情
        operateDetails(list, priceMap, createOrderResult);

        createOrderResult.setItems(list);

        // 计算配送费
        Map<String, DormShopDelivery> deliveryMap = new HashMap<>();
        for (DormShopDelivery delivery : result.getData().getDormShopDeliveries()) {
            if (delivery.getStatus().byteValue() == 1) { /// 启用
                deliveryMap.put(delivery.getMethod() + "", delivery);
            }
        }

        // 如果创建购物车没有传配送方式,默认为店长配送
        if (param.getSend_type() == null) {
            if (deliveryMap.get(CommonConstant.DELIVERY_TYPE_DORMER + "") != null
                    && deliveryMap.get(CommonConstant.DELIVERY_TYPE_DORMER + "").getStatus().byteValue() == 1) {
                createOrderResult.setSend_type(CommonConstant.DELIVERY_TYPE_DORMER);
            } else {
                createOrderResult.setSend_type(CommonConstant.DELIVERY_TYPE_YOURSELF);
            }
        } else {
            createOrderResult.setSend_type(param.getSend_type());
        }

        if (Byte.valueOf(createOrderResult.getSend_type()).byteValue() == CommonConstant.DELIVERY_TYPE_DORMER) {
            DormShopDelivery dormShopDelivery = deliveryMap.get(createOrderResult.getSend_type() + "");
            if (dormShopDelivery.getThreshold() != null && dormShopDelivery.getThresholdSwitch() != null
                    && 1 == dormShopDelivery.getThresholdSwitch().intValue() && createOrderResult.getDocument_amount()
                    .doubleValue() >= dormShopDelivery.getThreshold().doubleValue()) {
                // 满足免配送费条件
                logger.info("dormShopDelivery.getThreshold():" + dormShopDelivery.getThreshold());
                logger.info("documentAmount.doubleValue():" + createOrderResult.getDocument_amount().doubleValue());
                logger.info("#########满足免配送费条件###########");
                createOrderResult.setDelivery_amount(0.00);
            } else {
                createOrderResult.setDelivery_amount(dormShopDelivery.getCharge());
            }
        } else {
            createOrderResult.setDelivery_amount(0.00);
        }

        // 计算订单下的总金额=文档价格+配送费-优惠券金额
        double freeAdAmounut = 0;
        if (param.getOpen_ad() == 1 && free_page_num > 0) {
            createOrderResult.setOpen_ad(CommonConstant.AD_STATUS_OPEN);
            BigDecimal freePageNum = new BigDecimal(free_page_num);
            double bwSingleUnitPrice = priceMap.get(PrintConst.PRINT_TYPE_BW_SINGLE + "").doubleValue();
            BigDecimal BwSinglePrintPrice = new BigDecimal(bwSingleUnitPrice);// 黑白单面
            freeAdAmounut = BwSinglePrintPrice.multiply(freePageNum).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } else {
        }
        createOrderResult.setOpen_ad(param.getOpen_ad());
        createOrderResult.setFree_amount(freeAdAmounut);
        createOrderResult.setAd_page_num(free_page_num);
        BigDecimal freeAmount = new BigDecimal(freeAdAmounut);
        BigDecimal oldDocumentAmount = new BigDecimal(createOrderResult.getDocument_amount());
        double lastDocumentAmount = oldDocumentAmount.subtract(freeAmount).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        createOrderResult.setDocument_amount(oldDocumentAmount.doubleValue());
        BigDecimal documentAmount = new BigDecimal(lastDocumentAmount); // 文档的金额=原始文档价格
        createOrderResult.setDoc_coupon_amount(lastDocumentAmount);
        // -
        // 免费打印价格
        BigDecimal deliveryAmount = new BigDecimal(createOrderResult.getDelivery_amount());

        Coupon coupon = new Coupon();
        if (uid.intValue() != 0) {// 已经登录
            if (!StringUtil.isBlank(param.getCoupon_code())) {
                coupon = getCouponByCodeAndUid(uid, param.getCoupon_code(), lastDocumentAmount);
            } else {
                // 最优优惠券匹配规则
                // 没有传优惠券code,选择最优优惠券 param:uid,文档价格
                // coupon = getBestCouponByUidAndAmount(uid,
                // createOrderResult.getDocument_amount());
            }

//			if (coupon == null && !StringUtil.isBlank(param.getCoupon_code())) {
//				throw new AppException(CommonConstant.SATTUS_COUPON_NOT_VALID,
//						CommonConstant.MSG_SATTUS_COUPON_NOT_VALID + param.getCoupon_code());
//			}
            if (coupon != null) {
                createOrderResult.setCoupon_code(coupon.getCode());
                createOrderResult
                        .setCoupon_discount(coupon.getDiscount() == null ? null : coupon.getDiscount().doubleValue());
                // if (coupon.getDiscount().doubleValue() >
                // coupon.getThreshold().doubleValue()) {
                // throw new
                // AppException(CommonConstant.SATTUS_COUPON_NOT_VALID,
                // "优惠券折扣金额不能大于门槛金额,优惠券code:" + coupon.getCode());
                // }
            }

            Double couponDiscount = 0.00;
            if (coupon != null) {
                couponDiscount = coupon.getDiscount() == null ? 0.00 : coupon.getDiscount().doubleValue();
            }
            BigDecimal couponAmount = new BigDecimal(-couponDiscount);

            // 优惠券只能减免文档价格
            double lastDocAmount = documentAmount.add(couponAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
            if (lastDocAmount <= 0) {
                lastDocAmount = 0;
                createOrderResult.setCoupon_discount(documentAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
            }

            double totalAmount = BigDecimal.valueOf(lastDocAmount).add(deliveryAmount).setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            if (totalAmount <= 0) {
                totalAmount = 0.01;
            }
            createOrderResult.setTotal_amount(totalAmount);
        } else {
            // 未登录,不能使用优惠券
            double totalAmount = documentAmount.add(deliveryAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
            if (totalAmount < 0) {
                totalAmount = 0.01;
            }
            createOrderResult.setTotal_amount(totalAmount);
        }

        /**
         * 处理配送时间相关逻辑
         */
        // 店长配送
        if (CommonConstant.DELIVERY_TYPE_DORMER == createOrderResult.getSend_type()) {
            // 如果前端已经选择了配送时间,则使用前端时间
            if (!StringUtil.isBlank(param.getExpect_time_name())) {
                createOrderResult.setExpect_time_name(param.getExpect_time_name());
                createOrderResult.setExpect_start_time(param.getExpect_start_time());
                createOrderResult.setExpect_end_time(param.getExpect_end_time());
            } else {
                // 对营业时间进行处理
                DeliveryTimeUtil util = new DeliveryTimeUtil();
                Datagram<ShopTimeView> datagram = util.getExpectTimeList(result);
                List<ShopTimeInfo> delivery_times = new ArrayList<>();
                if (datagram.getData() != null) {
                    delivery_times = datagram.getData().getDelivery_times();
                }

                if (delivery_times.size() > 0) {
                    ShopTimeInfo shopTimeInfo = delivery_times.get(0);

                    createOrderResult.setDelivery_type(shopTimeInfo.getType());
                    if (shopTimeInfo.getType() == 2) {
                        // 立即送出
                        createOrderResult.setExpect_time_name(shopTimeInfo.getName());
                    } else {
                        // 预定
                        createOrderResult.setExpect_time_name(shopTimeInfo.getName());
                        createOrderResult.setExpect_start_time(shopTimeInfo.getExpect_start_time());
                        createOrderResult.setExpect_end_time(shopTimeInfo.getExpect_end_time());
                    }
                }
            }
        }

        // 上门自取
        if (CommonConstant.DELIVERY_TYPE_YOURSELF == createOrderResult.getSend_type()) {
            DormShopDelivery dormShopDelivery = deliveryMap.get(createOrderResult.getSend_type() + "");
            // 上门自取,需要获取取货地址和取货时间
            // 取货地址
            createOrderResult.setPick_address(dormShopDelivery.getAddress());

            if (StringUtil.isBlank(param.getExpect_time_name())) {
                // 上门自取需要返回自取时间和地点
                List<DormShopTime> dormShopTimes = result.getData().getDormShopTimes();
                StringBuilder sb = new StringBuilder();
                for (DormShopTime shopTime : dormShopTimes) {
                    if (shopTime.getTimeSwitch().byteValue() == 1) {
                        int left_start = shopTime.getStartTime() % 100;
                        int pre_start = (shopTime.getStartTime() - left_start) / 100;
                        if (left_start < 10) {
                            sb.append(pre_start + ":0" + left_start + "~");
                        } else {
                            sb.append(pre_start + ":" + left_start + "~");
                        }

                        int left_end = shopTime.getEndTime() % 100;
                        int pre_end = (shopTime.getEndTime() - left_end) / 100;
                        if (left_end < 10) {
                            sb.append(pre_end + ":0" + left_end + " ");
                        } else {
                            sb.append(pre_end + ":" + left_end + " ");
                        }
                    }
                }
                createOrderResult.setExpect_time_name(sb.toString());
            } else {
                createOrderResult.setExpect_time_name(param.getExpect_time_name());
            }
        }
        createOrderResult.setShop_id(param.getShop_id());
        if (uid != null) {
            Object couponObj = null;
            try {
                if (coupon != null) {
                    couponObj = getAppCouponListByUid(uid, "no", lastDocumentAmount,
                            param.getDoc_type() == 1 ? "2" : "1");
                    Datagram<AppCouponList> couponrs = (Datagram<AppCouponList>) couponObj;
                    if (couponrs != null && couponrs.getStatus() == 0 && couponrs.getData() != null
                            && couponrs.getData().getCoupons() != null && couponrs.getData().getCoupons().size() > 0) {
                        createOrderResult.setCoupon_had(1);
                    } else {
                        createOrderResult.setCoupon_had(0);
                    }
                } else {
                    createOrderResult.setCoupon_had(0);
                }
            } catch (Exception e) {
                logger.info("获取是否有优惠券可用失败：" + JsonUtil.getJsonFromObject(couponObj));
                createOrderResult.setCoupon_had(0);
            }
        }
        return DatagramHelper.getDatagram(createOrderResult, 0, "", true);
    }

    private AppCoupon CouponToAppCoupon(Coupon coupon) {
        String threshold = null;
        String scope = null;
        for (CouponAllow allow : coupon.getCouponAllows()) {
            if (allow.getAllowType().getType() == CouponAllowEnum.ALLOW_THRESHOLD.getType()) {
                threshold = allow.getAllowValues().get(0);
            }
            if (allow.getAllowType().getType() == CouponAllowEnum.ALLOW_BUSINESS.getType()) {
                scope = allow.getAllowValues().get(0);
            }
        }
        AppCoupon appCoupon = new AppCoupon();
        appCoupon.setThreshold(threshold == null ? null : Double.parseDouble(threshold));
        appCoupon.setActive_date(coupon.getActiveTime().getTime() / 1000);
        appCoupon.setAdd_time(coupon.getCreateTime().getTime() / 1000);
        appCoupon.setCode(coupon.getCode());
        appCoupon.setDiscount(coupon.getDiscount().doubleValue());
        // appCoupon.setDiscount_apply_rids(coupon.getDiscountApplyRids());
        appCoupon.setExpire_date(coupon.getExpireTime().getTime() / 1000);
        appCoupon.setItem_id(coupon.getId());
        appCoupon.setPhone(coupon.getPhone());
        appCoupon.setScope(scope == null ? null : Integer.parseInt(scope));
        // appCoupon.setSource(coupon.getSource());
        appCoupon.setStatus(coupon.getStatus());
        Long now = System.currentTimeMillis() / 1000;
        if (coupon.getStatus().intValue() == 1) {
            appCoupon.setNew_status(2);
        } else if (coupon.getStatus().intValue() == 0) {
            appCoupon.setNew_status(1);
            if (coupon.getActiveTime().getTime() / 1000 > now.intValue()) {
                appCoupon.setNew_status(0);
            }
            if (coupon.getExpireTime().getTime() / 1000 < now.intValue()) {
                appCoupon.setNew_status(3);
            }
        }
        appCoupon.setText(coupon.getTitle());
        appCoupon.setTips(coupon.getTip());
        // appCoupon.setType(coupon.getType());
        appCoupon.setUse_uid(coupon.getUseUid());
        appCoupon.setUse_time(coupon.getUseTime() == null ? null : (int) (coupon.getUseTime().getTime() / 1000));
        appCoupon.setUse_order_type(coupon.getOrderType());
        appCoupon.setUse_order_id(coupon.getOrderId());
        appCoupon.setUid(coupon.getUid());
        appCoupon.setActive_time(coupon.getActiveTime().getTime() / 1000);
        appCoupon.setExpire_time(coupon.getExpireTime().getTime() / 1000);
        appCoupon.setTip(coupon.getTip());
        appCoupon.setExtension(coupon.getExtension() == null ? "2" : coupon.getExtension());
        return appCoupon;
    }

    public void operateDetails(List<AppCreateOrderDetailParam> list, Map<String, Double> priceMap,
                               AppCreateOrderResult createOrderResult) {

        int printNum = 0;
        int printPages = 0;
        Double documentAmount = 0.00;
        for (AppCreateOrderDetailParam detail : list) {

            double price = getSingleDetailAmount(priceMap.get(detail.getPrint_type() + "").doubleValue(), detail);
            detail.setPrice(price);
            detail.setOrigin_price(price);
            detail.setAmount(detail.getPrice() * detail.getQuantity());
            // 打印规格描述
            StringBuilder specSB = new StringBuilder();
            if (detail.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_BW_SINGLE) {
                specSB.append("黑白单面，");
            } else if (detail.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE) {
                specSB.append("黑白双面，");
            } else if (detail.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_COLOR_SINGLE) {
                specSB.append("彩色单面，");
            } else if (detail.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
                specSB.append("彩色双面，");
            } else if (detail.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_PHOTO) {
                specSB.append("A6，");
            }
            // 缩印
            if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_none) {
                specSB.append("不缩印，");
            } else if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_two) {
                specSB.append("一页两面，");
            } else if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_four) {
                specSB.append("一页四面，");
            } else if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_six) {
                specSB.append("一页六面，");
            } else if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_nine) {
                specSB.append("一页九面，");
            } else if (detail.getReduced_type().byteValue() == PrintConst.re_print_photo_default) {
                specSB.append("默认");
            }
            if (detail.getReduced_type().byteValue() != PrintConst.re_print_photo_default)
                specSB.append(detail.getPrint_page()).append("页");
            detail.setSpecifications(specSB.toString());
            printNum += detail.getQuantity();
            printPages += detail.getPrint_page() * detail.getQuantity();
            documentAmount = Double.sum(documentAmount, detail.getAmount());
        }
        createOrderResult.setPrint_num(printNum);
        createOrderResult.setPrint_pages(printPages); // 页数*份数
        createOrderResult.setDocument_amount(documentAmount);
    }

    /**
     * 计算单份文档价格
     *
     * @param detail
     * @return
     */
    private double getSingleDetailAmount(double printPrice, AppCreateOrderDetailParam detail) {

        Integer pdf_pageNum = detail.getPage(); // 缩印前pdf页数
        Double final_pageNum = 0.00;
        // 是否缩印
        if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_two) { // 二合一
            final_pageNum = Math.ceil(pdf_pageNum / 2.0);
        }
        if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_four) { // 四合一
            final_pageNum = Math.ceil(pdf_pageNum / 4.0);
        }
        if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_six) { // 六合一
            final_pageNum = Math.ceil(pdf_pageNum / 6.0);
        }
        if (detail.getReduced_type().byteValue() == PrintConst.re_print_type_nine) { // 九合一
            final_pageNum = Math.ceil(pdf_pageNum / 9.0);
        }
        if (detail.getReduced_type().byteValue() == PrintConst.re_print_photo_default) {// 照片打印默认光面
            final_pageNum = 1.00;
        }
        // 考虑单双页
        if (detail.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_BW_DOUBLE
                || detail.getPrint_type().byteValue() == PrintConst.PRINT_TYPE_COLOR_DOUBLE) {
            if (detail.getPrint_type().byteValue() == PrintConst.re_print_type_none) {
                final_pageNum = Math.ceil(pdf_pageNum / 2.0);
            } else {
                final_pageNum = Math.ceil(final_pageNum / 2.0);
            }
        }
        // 单价*pdf页数 = 单份价格
        Double amount = 0.00;
        if (final_pageNum.doubleValue() == 0) {
            BigDecimal bd1 = (BigDecimal.valueOf(printPrice)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bd2 = (BigDecimal.valueOf(pdf_pageNum)).setScale(2, RoundingMode.HALF_UP);
            amount = bd1.multiply(bd2).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } else {
            BigDecimal bd1 = (BigDecimal.valueOf(printPrice)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bd2 = (BigDecimal.valueOf(final_pageNum)).setScale(2, RoundingMode.HALF_UP);
            amount = bd1.multiply(bd2).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return amount;
    }

    public Datagram<?> getAppCouponListByUid(Long uid, String isAll, Double amount, String type) {
        CouponQueryParam couponQuery = new CouponQueryParam();
        couponQuery.setUid(uid);
        List<String> scopes = new ArrayList<>();
        // scopes.add(String.valueOf(CouponScope.COMMON.getScope())); // 通用优惠券
        scopes.add(String.valueOf(CouponScope.PRINTER.getScope())); // 打印店优惠券
        List<CouponAllowParam> allows = new ArrayList<CouponAllowParam>();
        CouponAllowParam allowPara = new CouponAllowParam();
        allowPara.setAllowType(CouponAllowEnum.ALLOW_BUSINESS);
        allowPara.setAllowValues(scopes);// 使用范围
        allows.add(allowPara);
        if ("no".equals(isAll) || "0".equals(isAll)) { // 需要过滤使用金额门槛
            couponQuery.setActiveStatus(CommonConstant.COUPON_ACTIVE_STATUS_1);
            couponQuery.setExpireStatus(CommonConstant.COUPON_EXPIRE_STATUS_0);
            couponQuery.setStatus(CouponStatus.UNUSED.getStatus());
            CouponAllowParam allowAmount = new CouponAllowParam();
            allowAmount.setAllowType(CouponAllowEnum.ALLOW_THRESHOLD);
            List<String> amountsList = new ArrayList<>();
            amountsList.add(String.valueOf(amount));
            allowAmount.setAllowValues(amountsList);// 金额门槛
            allows.add(allowAmount);
            couponQuery.setCouponAllowParams(allows);
            Result<List<Coupon>> result = couponService.getCouponList(couponQuery);
            if (result == null || result.getStatus() != 0) {
                throw new BaseException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                        CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
            }
            if (result.getData() == null) {
                AppCouponList list = new AppCouponList();
                list.setCoupons(new ArrayList<AppCoupon>());
                return DatagramHelper.getDatagramWithSuccess(list);
            }

            List<Coupon> retList = result.getData();
            List<Coupon> fResult = new ArrayList<Coupon>();
            List<Coupon> fResult_used = new ArrayList<Coupon>();
            List<Coupon> fResult_exp = new ArrayList<Coupon>();
            for (Coupon fr : retList) {
                // 判断优惠券类型是否对
                boolean tmp = true;
                if (fr.getExtension() == null
                        || (fr.getExtension() != null
                        && (fr.getExtension().equals("0") || fr.getExtension().equals("")))
                        || (fr.getExtension() != null && !fr.getExtension().equals("1")
                        && !fr.getExtension().equals("2"))) {

                } else {
                    if ((fr.getExtension() != null && fr.getExtension().equals("0")
                            || (!type.equals("0") && ((fr.getExtension() != null && !fr.getExtension().equals(type)
                            && !fr.getExtension().equals("0"))
                            || (fr.getExtension() == null && !type.equals("2")))))) {
                        tmp = false;
                    }
                }
                if (tmp) {
                    if (fr.getExpireTime().getTime() < System.currentTimeMillis()) {
                        fResult_exp.add(fr);
                    } else {
                        if (fr.getStatus() != 0) {
                            fResult_used.add(fr);
                        } else {
                            fResult.add(fr);
                        }
                    }
                }
            }
            Collections.sort(fResult, new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {

                    if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                        return -1;
                    }
                    if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                        if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                            return -1;
                        }
                    }
                    return 1;
                }
            });
            Collections.sort(fResult_exp, new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {

                    if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                        return -1;
                    }
                    if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                        if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                            return -1;
                        }
                    }
                    return 1;
                }
            });
            Collections.sort(fResult_used, new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {

                    if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                        return -1;
                    }
                    if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                        if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                            return -1;
                        }
                    }
                    return 1;
                }
            });

            if (fResult_used.size() > 0)
                fResult.addAll(fResult_used);
            if (fResult_exp.size() > 0)
                fResult.addAll(fResult_exp);
            AppCouponList appCoupon = new AppCouponList();
            appCoupon.setCoupons(getAppCoupon(fResult));
            return DatagramHelper.getDatagramWithSuccess(appCoupon);
        }
        couponQuery.setCouponAllowParams(allows);
        Result<List<Coupon>> result = couponService.getCouponList(couponQuery);
        if (result == null || result.getStatus() != 0) {
            throw new AppException(CommonConstant.STATUS_SERVICE_REQUEST_EXCEPTION,
                    CommonConstant.MSG_SERVICE_REQUEST_EXCEPTION);
        }
        if (result.getData() == null) {
            AppCouponList list = new AppCouponList();
            list.setCoupons(new ArrayList<AppCoupon>());
            return DatagramHelper.getDatagramWithSuccess(list);
        }
        List<Coupon> fResult = new ArrayList<Coupon>();
        List<Coupon> fResult_used = new ArrayList<Coupon>();
        List<Coupon> fResult_exp = new ArrayList<Coupon>();
        for (Coupon fr : result.getData()) {
            // 判断优惠券类型是否对
            if (fr.getExpireTime().getTime() < System.currentTimeMillis()) {
                fResult_exp.add(fr);
            } else {
                if (fr.getStatus() != 0) {
                    fResult_used.add(fr);
                } else {
                    fResult.add(fr);
                }
            }
        }
        Collections.sort(fResult, new Comparator<Coupon>() {
            @Override
            public int compare(Coupon o1, Coupon o2) {

                if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                    return -1;
                }
                if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                    if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                        return -1;
                    }
                }
                return 1;
            }
        });
        Collections.sort(fResult_exp, new Comparator<Coupon>() {
            @Override
            public int compare(Coupon o1, Coupon o2) {

                if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                    return -1;
                }
                if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                    if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                        return -1;
                    }
                }
                return 1;
            }
        });
        Collections.sort(fResult_used, new Comparator<Coupon>() {
            @Override
            public int compare(Coupon o1, Coupon o2) {

                if (o1.getDiscount().compareTo(o2.getDiscount()) > 0) {
                    return -1;
                }
                if (o1.getDiscount().compareTo(o2.getDiscount()) == 0) {
                    if (o1.getExpireTime().getTime() - o2.getExpireTime().getTime() < 0) {
                        return -1;
                    }
                }
                return 1;
            }
        });

        if (fResult_used.size() > 0)
            fResult.addAll(fResult_used);
        if (fResult_exp.size() > 0)
            fResult.addAll(fResult_exp);
        AppCouponList appCoupon = new AppCouponList();
        appCoupon.setCoupons(getAppCoupon(fResult));
        return DatagramHelper.getDatagramWithSuccess(appCoupon);
    }

    public List<AppCoupon> getAppCoupon(List<Coupon> fResult) {
        List<AppCoupon> lst = new LinkedList<AppCoupon>();
        for (Coupon coupon : fResult) {
            lst.add(CouponToAppCoupon(coupon));
        }
        return lst;
    }
}
