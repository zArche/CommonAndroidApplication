package name.arche.retrifitclient;
import java.util.ArrayList;

/**
 * Created by arche on 2016/4/1.
 */
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        if (this.code == 0) {
            return true;
        }
        return false;
    }

    public static <T> T createInstance(Class<T> cls) {
        T obj = null;
        try {
            obj = cls.newInstance();
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    public T getBody(Class<T> cls) {
        if (data == null) {
            try {
                data = createInstance(cls);
            } catch (Exception e) {
                return null;
            }
        }
        return data;
    }
    public T getBody() {
        if (data == null) {
            try {
                data = (T) new ArrayList<>();
                // 如果转换失败，则还是返回null
            } catch (ClassCastException e) {
                return null;
            }
        }
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String reason) {
        this.message = reason;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
