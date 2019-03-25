package com.blg.dorm.common.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.blg.dorm.common.CommonLog;

/**
 * 钉钉消息发送
 *  https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.onTm7R&treeId=257&articleId=105735&docType=1
 */
public class DingDingUtil {
    private static final String uri = "https://oapi.dingtalk.com/robot/send?" ;
    public static String sendTextMessage(String content){
        String accessToken = PropertyReader.getContextProperty("DINGDING_ACCESS_TOKEN") ;
        if(!accessToken.isEmpty()){
            return sendTextMessage(accessToken, content) ;
        }
        return accessToken ;
    }
    public static String sendTextMessage(String accessToken, String content){
        try {
            String url = uri + "access_token=" + accessToken ;
            Hashtable<String,Object> message = new Hashtable<String,Object>() ;
            message.put("msgtype", "text") ;

            Hashtable<String,String> text = new Hashtable<String,String>() ;
            text.put("content", content) ;
            message.put("text", text) ;

            Hashtable<String,Object> at = new Hashtable<String,Object>() ;
            at.put("isAtAll", false) ;
            List<String> mobiles = new ArrayList<String>() ;
            at.put("atMobiles", mobiles) ;
            message.put("at", at) ;

            String msg = new JsonUtils().bean2json(message) ;
            String result = HttpClientUtil.getInstance().postUrlData(url, msg, "application/json", "utf-8") ;
            return result ;
        } catch (Exception e) {
            return CommonLog.getException(e) ;
        }
    }
}
