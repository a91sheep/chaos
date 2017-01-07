package com.store59.base.service;

import com.store59.base.common.model.BanMatchResult;
import com.store59.base.common.model.BanWord;
import com.store59.base.common.filter.BanWordFilter;
import com.store59.base.data.dao.BanWordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created on 2016-04-22.
 */
@Service
public class BanWordService {
    //5分钟更新一次内容
    static long timeOut=5*60*1000;
    CharNode treeNode=null;
    long lastUpdateTime=-1;

    @Autowired
    private BanWordDao banWordDao;


    synchronized void updateTree(){
        long now=System.currentTimeMillis();
        if(now-lastUpdateTime <= timeOut){
            return;
        }
        CharNode node= new CharNode();
        BanWordFilter filter = new BanWordFilter();
        filter.setDeny((byte)1);
        List<BanWord> banWords = findBanWordList(filter);
        for(BanWord word:banWords){
            node.addWord(word.getReplacefrom());
        }
        this.treeNode = node;
        lastUpdateTime = now;
    }

    public BanMatchResult matchBan(String content){
        if(treeNode==null || System.currentTimeMillis()-lastUpdateTime > timeOut){
            updateTree();
        }
        return this.treeNode.matchBan(content);
    }

    public BanWord findBanWord(Integer bid){
        return banWordDao.findBanWord(bid);
    }
    
    public List<BanWord> findBanWordList(BanWordFilter filter){
        return banWordDao.findBanWordList(filter);
    }

    public BanWord addBanWord(BanWord record){
        return banWordDao.addBanWord(record);
    }

    public Boolean updateBanWord(BanWord record){
        return banWordDao.updateBanWord(record);
    }

    public Boolean deleteBanWord(Integer bid){
        return banWordDao.deleteBanWord(bid);
    }


}

