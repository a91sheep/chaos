package com.store59.base.common.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * Created by heqingpan on 16/4/22.
 * 过滤词匹配结果
 */
@SuppressWarnings("serial")
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class BanMatchResult implements Serializable {
    /**
     * 是否匹配到过滤词
     */
    boolean isMatch = false;
    /**
     * 匹配到的过滤词
     */
    String  banWord = "";
    /**
     * 过滤词在原文本的开始位置
     */
    int startIndex;
    /**
     * 过滤词在原文本的截止位置
     */
    int endIndex;

    public String getBanWord() {
        return banWord;
    }

    public void setBanWord(String banWord) {
        this.banWord = banWord;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setIsMatch(boolean isMatch) {
        this.isMatch = isMatch;
    }
}
