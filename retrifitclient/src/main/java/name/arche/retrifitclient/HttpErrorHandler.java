package name.arche.retrifitclient;


import com.google.gson.JsonParseException;
import org.json.JSONException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;

/**
 * 错误处理
 */
public class HttpErrorHandler {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int METHOD_BAND_USE = 405;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int REQUEST_BODY_TOO_LARGE = 413;
    private static final int NOT_SUPPORT_MEDIA_TYPE = 415;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int METHOD_NOT_IMPLEMENT = 501;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int NOT_SUPPORT_HTTP_VERSION = 505;

    public static Observable onError(Throwable e) {
        e.printStackTrace();
        Result result;
        String msg;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    msg = "Unauthorized!";
                    break;
                case FORBIDDEN:
                    msg = "Forbidden!";
                    break;
                case NOT_FOUND:
                    msg = "Not found!";
                    break;
                case METHOD_BAND_USE:
                    msg = "Method band for using";
                    break;
                case REQUEST_TIMEOUT:
                    msg = "Request time out!";
                    break;
                case REQUEST_BODY_TOO_LARGE:
                    msg = "Request body too large";
                    break;
                case NOT_SUPPORT_MEDIA_TYPE:
                    msg = "Not support media type";
                    break;
                case GATEWAY_TIMEOUT:
                    msg = "Gateway time out!";
                    break;
                case INTERNAL_SERVER_ERROR:
                    msg = "Server error!";
                    break;
                case METHOD_NOT_IMPLEMENT:
                    msg = "Method not implement";
                    break;
                case BAD_GATEWAY:
                    msg = "Bad gateway!";
                    break;
                case SERVICE_UNAVAILABLE:
                    msg = "Service unavailable!";
                    break;
                case NOT_SUPPORT_HTTP_VERSION:
                    msg = "Not support http version";
                    break;
                default:
                    msg = "Network error!";  //均视为网络错误
                    break;
            }
            result = new Result(httpException.code(), msg, null);
        } else if (e instanceof UnknownHostException) {
            msg = "网络或服务器错误!";
            result = new Result(-1, msg, null);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            msg = "Json parse error!";//均视为解析错误
            result = new Result(-1, msg, null);
        } else if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时，请检测网络设置!";
            result = new Result(-1, msg, null);
        } else if (e instanceof ConnectException) {
            msg = "连接服务器失败!";
            result = new Result(-1, msg, null);
        } else if (e instanceof EOFException) {
            msg = "EOF异常";
            result = new Result(-1, msg, null);
        } else if (e instanceof SocketException) {
            msg = "Socket连接异常";
            result = new Result(-1, msg, null);
        } else {
            msg = "未知错误!";//未知错误
            result = new Result(-1, msg, null);
        }
        return Observable.just(result);
    }

}
