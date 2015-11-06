package name.arche.www.commonapplication.api;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arche on 2015/11/2.
 *
 * 概述：自定义的RequestUtils
 * 用于获取Volley所需的Request对象、全局Token值的设置和获取
 *
 */
public class RequestUtils {

    private static String mToken;

    /*
    * mReqBody:请求体 当如果是xml提交时，调用setReqBody赋值，重写Request的getBody方法，而不必传入reqParams
    * */
    private static String mReqBody = "";

    private static String mCharSet = "utf-8";// 约定的字符编码

    public static JsonObjectRequest getJsonObjectRequest(final int method,String url,final Map<String,String>headerParams,
                                                         final Map<String,String>reqParams,JSONObject jsonRequest,Response.Listener<JSONObject>listener,Response.ErrorListener errorListener){
        //Get请求，参数拼接url
        if (method == Request.Method.GET && reqParams != null)
            url = getURL(url,reqParams);

        Log.d("url",url);

        if (jsonRequest != null)
            Log.d("jsonRequest",jsonRequest.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,url,jsonRequest,listener,errorListener){

            //如果是Post,设置请求参数
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*
                * 如果有其他所有接口都通用的请求参数，在此处添加
                * Example:Map<String,String> params = new HashMap<>();
                *           params.put("commonParam","commonParam");
                *           params.putAll(reqParams);
                *           return params;
                * */
                if (method == Method.POST && reqParams != null)
                    return reqParams;
                return super.getParams();
            }

            //设置请求头，在此处设置全局token，而不必每次调用时在headerParams中设置
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> reqHeaderParams = new HashMap<String,String>();
                /*
                * 如果还有其他接口通用头参数，在此处添加
                * */
                if (!TextUtils.isEmpty(mToken))
                    reqHeaderParams.put("Authorization",mToken);
                if (headerParams != null)
                    reqHeaderParams.putAll(headerParams);

                if (reqHeaderParams != null && reqHeaderParams.size() != 0)
                    Log.d("reqHeaderParams",new JSONObject(reqHeaderParams).toString());

                return reqHeaderParams;
            }

            //设置请求体，当以xml格式提交时，以该函数提交参数
            @Override
            public byte[] getBody() {
                byte[] reqBodyByteArr = null;
                if (!TextUtils.isEmpty(mReqBody)){
                    try {
                        reqBodyByteArr = mReqBody.getBytes(mCharSet);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mReqBody = ""; //重置
                }
                if (reqBodyByteArr !=   null)
                    return reqBodyByteArr;
                return super.getBody();
            }
        };

        //设置超时时长以及重试次数
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10*1000,1,1.0f));

        return jsonObjectRequest;
    }

    /*
    * 获取StringRequest,方式同获取JsonObjectRequest
    * */
    public static StringRequest getStringRequest(final int method,String url,final Map<String,String> headerParams,
                                                 final Map<String,String>reqParams,Response.Listener<String>listener,Response.ErrorListener errorListener){
        if (method == Request.Method.GET && reqParams != null)
            url = getURL(url, reqParams);

        Log.d("url", url);

        StringRequest stringRequest = new StringRequest(method,url,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (method == Method.POST && reqParams != null)
                    return reqParams;
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> reqHeaderParams = new HashMap<String, String>();

                if (!TextUtils.isEmpty(mToken))
                    reqHeaderParams.put("Authorization", mToken);

                if (headerParams != null)
                    reqHeaderParams.putAll(headerParams);

                Log.d("reqHeaderParams", new JSONObject(reqHeaderParams).toString());
                return reqHeaderParams;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));

        return stringRequest;
    }

    public static void setCharSet(String charSet){
        mCharSet = charSet;
    }

    public static void setReqBody(String reqBody){
        if (TextUtils.isEmpty(reqBody))
            return;
        mReqBody = reqBody;
    }

    public static String getToken(){
        return mToken;
    }

    public static void setToken(String token){
        if (TextUtils.isEmpty(token))
            return;
        mToken = token;
    }

    /**
     * 概述：Get请求-URL拼接
     * @param url
     * @param params
     * **/
    private static String getURL(String url,Map<String,String>params){
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");

        if (params != null && params.size() != 0){
            for (Map.Entry<String,String> entry : params.entrySet()){
                try {
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),mCharSet));
                } catch (UnsupportedEncodingException e) {
                    Log.e("RequestError", "GET请求, 参数编码错误");
                    e.printStackTrace();
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
