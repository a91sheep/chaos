package com.store59.base.common.api;

import com.store59.base.common.model.BanMatchResult;
import com.store59.kylin.common.model.Result;

/**
 * Created by heqingpan on 16/4/22.
 */
public interface BanWordApi {

    /**
     * 匹配指定文本中是否包含过滤词
     * @param content
     * @return
     */
    Result<BanMatchResult> matchBan(String content);
}
