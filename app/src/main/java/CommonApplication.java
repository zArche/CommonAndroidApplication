import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import name.arche.www.commonapplication.api.MyVolley;
import name.arche.www.commonapplication.database.DatabaseHelper;

/**
 * Created by Arche on 2015/11/3.
 */
public class CommonApplication extends Application {
    public static CommonApplication pThis;

    @Override
    public void onCreate() {
        super.onCreate();
        pThis = this;

        init();
    }

    /*==============初始化操作===================*/
    //SDK的初始化操作加在此处
    private void init(){
        MyVolley.init(this);
    }
    /*============================================*/


    protected static DatabaseHelper mDatabaseHelper = null;

    public static DatabaseHelper getHelper(){
        if (mDatabaseHelper == null){
            mDatabaseHelper = OpenHelperManager.getHelper(pThis,DatabaseHelper.class);
        }

        return mDatabaseHelper;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mDatabaseHelper != null){
            OpenHelperManager.releaseHelper();
            mDatabaseHelper = null;
        }
    }
}
