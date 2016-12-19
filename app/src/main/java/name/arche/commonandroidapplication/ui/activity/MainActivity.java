package name.arche.commonandroidapplication.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import name.arche.commonandroidapplication.R;
import name.arche.commonandroidapplication.api.ApiClient;
import name.arche.retrifitclient.RetrofitTransformer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.hello)
    TextView hello;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String org = "square";
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("加载中..");

        /**
         * 示例接口：
         *      获取retrofit的github地址
         *      https://api.github.com/orgs/square/repos
         * **/
//        ApiClient.getApiServer().listRepos(org) //获得一个List<Repo>
//                .subscribeOn(Schedulers.io()) //在io线程请求网络
//                .flatMap(repos -> Observable.from(repos))//循环遍历list
//                .filter(repo -> repo.getName().contains("retrofit"))//遍历每一个repo,只保留名字包含retrofit的
//                .map(repo -> repo.getUrl())//获得名字为retrofit的url地址
//                .doOnSubscribe(new Action0() { //开始前show一下dialog   doOnSubscribe所在的线程为离他最近subscribeOn的线程，因此在下面回到主线程
//                    @Override
//                    public void call() {
//                        dialog.show();
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())//doOnSubscribe 回到主线程用来show dialog
//                .doOnCompleted(new Action0() { //完成后dismiss dialog
//                    @Override
//                    public void call() {
//                        dialog.dismiss();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())//在主线程observe
//                .subscribe(s -> hello.setText(s));


        dialog.show();
        ApiClient.getApiServer().listRepos(org)
                .compose(new RetrofitTransformer<>())
                .flatMap(repos -> Observable.from(repos))
                .filter(repo -> repo.getName().contains("retrofit"))
                .map(repo -> repo.getUrl())
                .subscribe(s -> {
                    dialog.dismiss();
                    hello.setText(s);
                });
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void handleData() {

    }
}
