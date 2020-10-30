package com.mm.android.deviceaddmodule.openapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;
import com.mm.android.deviceaddmodule.mobilecommon.utils.MD5Helper;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLException;

public class HttpSend {

    private static final String TAG = HttpSend.class.getSimpleName();

    public static JsonObject execute(Map<String, Object> paramsMap, String method,int timeOut) throws BusinessException {
        Map<String, Object> map = paramsInit(paramsMap);
        // 返回json
        JsonObject jsonObj = doPost(CONST.HOST + "/openapi/" + method, map,timeOut);
        LogUtil.debugLog(TAG, "response result：" + jsonObj.toString());
        if (jsonObj == null) {
            throw new BusinessException("openApi response is null");
        }
        JsonObject jsonResult = jsonObj.getAsJsonObject("result");
        if (jsonResult == null) {
            throw new BusinessException("openApi response is null");
        }
        String code = jsonResult.get("code").getAsString();
        if (!"0".equals(code)) {
            String msg = jsonResult.get("msg").getAsString();
            throw new BusinessException(code + msg);
        }
        try {
            JsonObject jsonData = jsonResult.getAsJsonObject("data");
            if (jsonData ==null){
                jsonData=new JsonObject();
            }
            return jsonData;
        } catch (Throwable e) {
            BusinessException businessException = new BusinessException(e);
            throw businessException;
        }
    }

    private static JsonObject doPost(String url, Map<String, Object> map, int timeOut) throws BusinessException {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        JsonObject jsonObject = new JsonObject();
        try {
            LogUtil.debugLog(TAG, "reqest: " + url + " data:" + json.toString());
            String openApi = HttpClient.post(url, json, "application/json", "OpenApi",timeOut);
            jsonObject =  new JsonParser().parse(openApi).getAsJsonObject();
        } catch (IOException e) {
            BusinessException b = new BusinessException(e);
            if (e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException
                    || e instanceof UnknownHostException
                    || e instanceof UnknownServiceException || e instanceof SSLException
                    || e instanceof SocketException) {
                b.errorDescription = "操作失败，请检查网络";
            }
            throw b;
        } catch (Throwable e) {
            BusinessException b = new BusinessException(e);
            throw b;
        }
        return jsonObject;
    }

    private static Map<String, Object> paramsInit(Map<String, Object> paramsMap) {
        long time = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();
        StringBuilder paramString = new StringBuilder();
        paramString.append("time:").append(time).append(",");
        paramString.append("nonce:").append(nonce).append(",");
        paramString.append("appSecret:").append(CONST.SECRET);
        String sign = "";
        // 计算MD5得值
        sign = MD5Helper.encodeToLowerCase(paramString.toString().trim());
        Map<String, Object> systemMap = new HashMap<String, Object>();
        systemMap.put("ver", "1.0");
        systemMap.put("sign", sign);
        systemMap.put("appId", CONST.APPID);
        systemMap.put("nonce", nonce);
        systemMap.put("time", time);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("system", systemMap);
        map.put("params", paramsMap);
        map.put("id", id);
        return map;
    }
}
