package name.arche.www.commonapplication.api;

import android.support.v4.app.NavUtils;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Arche on 2015/11/2.
 *
 * 概述：API客户端接口：用于访问网络
 */
public class ApiClient {

    private static Response.Listener<JSONObject> mListener;
    private static Response.ErrorListener mErrorListener;
    private static OnReqStartListener mOnReqStartListener;


    /**
     * 概述：请求开始时的回调监听器
     * **/
    public interface OnReqStartListener{
        public void onReqStart();
    }


    /**
     * 设置Listeners，用于网络请求的回调
     * **/
    public static void setListeners(OnReqStartListener onReqStartListener,Response.Listener listener,Response.ErrorListener errorListener){
        mOnReqStartListener = onReqStartListener;
        mListener = listener;
        mErrorListener = errorListener;
    }

    private static String ROOT_ELEMENT_XML = "xml";
    private static String ROOT_ELEMENT_ROOT = "root";
    /**
     * 概述：将键值对转换为XML
     *
     * @param params
     */
    private static String map2XML(HashMap<String, Object> params, String rootElement) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");

        sb.append("<" + rootElement + ">");
        Set<String> keys = params.keySet();
        for (String key : keys) {
            sb.append("<" + key + ">" + params.get(key) + "</" + key + ">");
        }
        sb.append("</" + rootElement + ">");

        return sb.toString();
    }

    /**
     * 概述：GET请求
     * **/
    private static void _GET(String url,Map<String ,String> headerParams,Map<String,String> reqParams){
        if (mListener != null && mErrorListener != null){
            if (mOnReqStartListener != null)
                mOnReqStartListener.onReqStart();// 请求开始的回调

            MyVolley.addToRequestQueue(RequestUtils.getJsonObjectRequest(Request.Method.GET,url,headerParams,reqParams,null,mListener,mErrorListener));
        }
    }

    /**
     * 概述：带回调监听器的GET请求方式
     * 当在Activity中没有调用ApiClient.setListeners()函数设置监听器时，可以创建匿名对象的方式传入监听器
     * **/
    private static void _GET_WITH_LISTENERS(String url, Map<String, String> headerParams, Map<String, String> reqParams,
                                            OnReqStartListener reqStartListener, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        if (responseListener != null && errorListener != null) {
            if (reqStartListener != null)
                reqStartListener.onReqStart();

            MyVolley.addToRequestQueue(RequestUtils.getJsonObjectRequest(Request.Method.GET, url, headerParams, reqParams, null, responseListener,
                    errorListener));
        }
    }

    /**
     * 概述: 字符串GET请求
     */
    private static void _GET_WITH_LISTENERS_REQ_STRING(String url, Map<String, String> headerParams, Map<String, String> reqParams,
                                                       OnReqStartListener reqStartListener, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {

        if (responseListener != null && errorListener != null) {
            if (reqStartListener != null)
                reqStartListener.onReqStart();

            MyVolley.addToRequestQueue(RequestUtils.getStringRequest(Request.Method.GET, url, headerParams, reqParams, responseListener,
                    errorListener));
        }
    }

    /**
     * 概述: POST请求
     */
    private static void _POST(String url, Map<String, String> headerParams, Map<String, String> reqParams, Map<String, Object> reqBodyParams) {
        if (mListener != null && mErrorListener != null) {
            if (mOnReqStartListener != null)
                mOnReqStartListener.onReqStart(); // 请求开始的回调

            JSONObject reqJsonObject = null;
            if (reqBodyParams != null)
                reqJsonObject = new JSONObject(reqBodyParams);

            MyVolley.addToRequestQueue(RequestUtils.getJsonObjectRequest(Request.Method.POST, url, headerParams, reqParams,
                    reqJsonObject == null ? null : reqJsonObject, mListener, mErrorListener));
        }
    }

    /**
     * 概述: 带回调监听器的POST请求方式
     * 当在Activity中没有调用ApiClient.setListeners()函数设置监听器时，可以创建匿名对象的方式传入监听器
     */
    private static void _POST_WITH_LISTENERS(String url, Map<String, String> headerParams, Map<String, String> reqParams,
                                             Map<String, Object> reqBodyParams, OnReqStartListener reqStartListener, Response.Listener<JSONObject> responseListener,
                                             Response.ErrorListener errorListener) {
        if (responseListener != null && errorListener != null) {
            if (reqStartListener != null)
                reqStartListener.onReqStart(); // 请求开始的回调

            JSONObject reqJsonObject = null;

            if (reqBodyParams != null)
                reqJsonObject = new JSONObject(reqBodyParams);

            MyVolley.addToRequestQueue(RequestUtils.getJsonObjectRequest(Request.Method.POST, url, headerParams, reqParams,
                    reqJsonObject == null ? null : reqJsonObject, responseListener, errorListener));
        }
    }


    /**
     * 登陆接口调用
     * **/
    public static void login(String userId,String passwd, OnReqStartListener reqStartListener, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        Map<String, Object> reqBodyParams = new HashMap<String, Object>();
        reqBodyParams.put("userId", userId);
        reqBodyParams.put("passwd", passwd);

        _POST_WITH_LISTENERS(URLs.USER_LOGIN, null, null, reqBodyParams, reqStartListener, responseListener, errorListener);
    }

}
