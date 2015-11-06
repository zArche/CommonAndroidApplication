package name.arche.www.commonapplication.api;

/**
 * Created by Arche on 2015/11/2.
 */
public class URLs {

    //公网/生产服务器
    private final static String SERVER_PRODUCT = "www.arche.name";

    //局域网服务器
    private final static String LOCAL_TEST_SERVER = "192.168.0.1";

    //内存服务器
    private final static String INTERNEL_TEST_SERVER = "192.168.0.1";

    private static String HOST = LOCAL_TEST_SERVER;

    private static boolean isInternelTest = false;
    private static boolean isLocalServer = true;

    static {
        // 更改app当前状态所用的服务器， 分为本地调试、内测、公测， 默认为内测
        if (isInternelTest) {

            if (isLocalServer)
                URLs.HOST = URLs.LOCAL_TEST_SERVER; // 本地测试服务器
            else
                URLs.HOST = URLs.INTERNEL_TEST_SERVER; // 内测域名代理

        } else {
            URLs.HOST = URLs.SERVER_PRODUCT;
        }
    }

    private final static String HTTP = "http://";
    private final static String HTTPS = "https://";

    private final static String URL_SPLITTER = "/";

    public final static String PRODUCT_NAME = "test";
    public final static String API = "api";
    public final static String PRODUCT_API_PATH = PRODUCT_NAME + URL_SPLITTER + API;

    private final static String URL_HTTP_HOST = HTTP + HOST + URL_SPLITTER;
    private final static String URL_API_HTTP_HOST = URL_HTTP_HOST + PRODUCT_API_PATH + URL_SPLITTER;

    // 登录
    public final static String USER_LOGIN = URL_API_HTTP_HOST + "userLogin";

}
