package name.arche.apiencryption;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Arche on 2016/12/19.
 */

public class EncryptionClient {

    private static native String signWithSalt(String sourceStr);
    static {
        System.loadLibrary("apiencryption");
    }

    public static String getSign(TreeMap<String,String> params){
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, String> entry:params.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length()-1);
        String sign = signWithSalt(sb.toString());
        return sign;
    }
}
