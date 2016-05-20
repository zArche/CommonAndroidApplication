package name.arche.commonandroidapplication.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import name.arche.commonandroidapplication.utils.ActivityUtils;

/**
 * Created by arche on 2016/4/7.
 */
public abstract class BaseActivity extends AppCompatActivity {

    //==========单例变量===========
//    protected static User mUser;
    //=============================

    //==========Activity通用成员变量==========
    protected Context mContext;
    //============================

    @Override
    @Subscribe
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        mContext = this;
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        ActivityUtils.addActivity(this);

        // 显示返回按钮并隐藏图标
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }



        initViews();
        initData(savedInstanceState);
        handleData();


    }


    //==============Activity通用函数=======================
    protected abstract void setContentView();
    protected abstract void initData(Bundle savedInstanceState);
    protected abstract void initViews();
    protected abstract void handleData();
    //=====================================================


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        ActivityUtils.finishActivity(this);
        super.onDestroy();
    }
}
