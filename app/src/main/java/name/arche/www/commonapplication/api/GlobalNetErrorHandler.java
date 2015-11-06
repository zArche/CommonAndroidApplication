package name.arche.www.commonapplication.api;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import name.arche.www.commonapplication.R;

/**
 * 概述: 自定义Error处理、主要处理：拦截401(Authorization Error)错误， 重获token的机制
 */
public class GlobalNetErrorHandler implements ErrorListener {

    private static GlobalNetErrorHandler mInstance = null;
    private static Context mContext;

    private GlobalNetErrorHandler() {
    }

    /**
     * 概述: 单例、全局唯一
     */
    public static GlobalNetErrorHandler getInstance(Context context) {
        mContext = context;

        if (mInstance == null) {

            synchronized (GlobalNetErrorHandler.class) {
                if (mInstance == null)
                    mInstance = new GlobalNetErrorHandler();
            }
        }

        return mInstance;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyErrorHelper.handleError(error, mContext); // 拦截/处理 error信息
    }

    public static class VolleyErrorHelper {

        public static void handleError(VolleyError error, Context context) {
            String errorMsg = getMessage(error, context);

            if (!TextUtils.isEmpty(errorMsg)){
                Toast.makeText(context,errorMsg,Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Returns appropriate message which is to be displayed to the user
         * against the specified error object.
         *
         * @param error
         * @param context
         * @return
         */
        public static String getMessage(VolleyError error, Context context) {
            if (error instanceof TimeoutError)
                return "";
                //return context.getResources().getString(R.string.network_timeout_req_again);
            else if (isServerProblem(error))
                return handleServerError(error, context);
            else if (isNetworkProblem(error)) {
                return context.getResources().getString(R.string.bad_network);
            }

            return context.getResources().getString(R.string.bad_network);
        }

        /**
         * Determines whether the error is related to network
         *
         * @param error
         * @return
         */
        private static boolean isNetworkProblem(VolleyError error) {
            return (error instanceof NetworkError) || (error instanceof NoConnectionError);
        }

        /**
         * Determines whether the error is related to server
         *
         * @param error
         * @return
         */
        private static boolean isServerProblem(VolleyError error) {
            return (error instanceof ServerError) || (error instanceof AuthFailureError);
        }

        /**
         * Handles the server error, tries to determine whether to show a stock
         * message or to show a message retrieved from the server.
         *
         * @param context
         * @return
         */
        private static String handleServerError(VolleyError error, final Context context) {

            NetworkResponse response = error.networkResponse;

            if (response != null) {
                switch (response.statusCode) {
//				case 404:
//				case 422:
                    case 401:

                        // 调用登陆函数重新获取token

                        return "";
                    default:
                        return context.getResources().getString(R.string.bad_network);
                }
            }
            return context.getResources().getString(R.string.bad_network);
        }
    }
}