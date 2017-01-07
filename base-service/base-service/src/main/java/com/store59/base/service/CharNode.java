package com.store59.base.service;


import com.store59.base.common.model.BanMatchResult;

import java.util.HashMap;

/**
 * Created by heqingpan on 16/4/22.
 */
public class CharNode {
    HashMap<Integer,CharNode> children=null;
    boolean isEndNode=false;

    public void addWord(String word){
        if(word==null || word.length()==0) {
            return;
        }
        CharNode node=this;
        CharNode tmp = null;
        for(char c:word.trim().toCharArray()){
            Integer code= (int)c;
            if(node.children==null){
                node.children = new HashMap<Integer, CharNode>();

                tmp = new CharNode();
                node.children.put(code,tmp);
                node=tmp;
            }
            else if(!node.children.containsKey(code)){
                tmp = new CharNode();
                node.children.put(code,tmp);
                node=tmp;
            }
            else{
                tmp = node.children.get(code);
                node=tmp;
            }
        }
        if(tmp!=null){
            tmp.isEndNode = true;
        }
    }

    public BanMatchResult matchBan(String content){
        BanMatchResult result = new BanMatchResult();
        result.setIsMatch(false);
        if(content==null || content.length()==0) {
            return result;
        }
        int i=0;
        int l=content.length();
        for(;i<l;i++){
            CharNode p = this;
            int j=i;
            while(j<l && p.children!=null ){
                Integer code = content.codePointAt(j);
                if(!p.children.containsKey(code)){
                    break;
                }
                p=p.children.get(code);
                j=j+1;
            }
            if(p!=this &&p.isEndNode){
                result.setIsMatch(true);
                result.setStartIndex(i);
                result.setEndIndex(j);
                result.setBanWord(content.substring(i, j));
                return result;
            }
        }
        return result;
    }
}
