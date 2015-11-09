package name.arche.www.commonapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import de.greenrobot.event.EventBus;
import name.arche.www.commonapplication.bean.User;

/**
 * Created by Arche on 2015/11/2.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean eventBusRegistered;
    /*==============Activity通用的成员变量提取到此处=================*/
    protected Context mContext;
    protected LayoutInflater mInflater;
    /*===============================================================*/

    /*=============================单例变量===========================*/
    protected static EventBus mEventBus;
    protected static User mCurUser;//静态，全局公用
    /*================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mInflater = getLayoutInflater();

        if(mEventBus == null)
            mEventBus = EventBus.getDefault();
        setContentView();
        initData(savedInstanceState);
        initViews();
        handleData();
    }


    /*===============提取Activity通用函数==================*/
    protected abstract void setContentView();

    /**
     *概述：对象接收、内存存储恢复、配置信息读取等数据初始化操作
     * **/
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 概述：UI初始化
     * **/
    protected abstract void initViews();

    /**
     * 概述：业务逻辑操作(网络请求等)
     * **/
    protected abstract void handleData();


    /**
     * 概述：注册EventBus
     * 只要调用该方法注册，便会在Activity销毁时注销EventBus,而不需要每次添加，或者忘记
     * TODO EventBus更多更强的功能提取
     * **/
    protected void registerEventBus(){
        if (mEventBus != null){
            mEventBus.register(this);
            eventBusRegistered = true;
        }

    }
    /*=====================================================*/



    /*======================Activity生命周期中重要的时刻===============================*/

    //可在此处加入贯穿整个生命周期变换的操作(如友盟统计)
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (eventBusRegistered)
            mEventBus.unregister(this);
        super.onDestroy();
    }

    /*==================================================================================*/

}
