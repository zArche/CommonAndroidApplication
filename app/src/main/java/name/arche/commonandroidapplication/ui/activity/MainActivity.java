package name.arche.commonandroidapplication.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import name.arche.commonandroidapplication.R;
import name.arche.commonandroidapplication.api.ApiClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.hello)
    TextView hello;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String company_id = "100";
//        String user_id = "10002";
//        ApiClient.getApiService().getUserInfo(company_id,user_id)
//                .subscribeOn(Schedulers.io())//在Schedulers.io运行发布者请求
//                .filter(result -> result.isSuccess()) //如果请求成功，返回code=0
//                .map(result1 -> result1.getBody(User.class))//获得返回的User信息实体
//                .observeOn(AndroidSchedulers.mainThread())//观察者在安卓主线程观察
//                .subscribe(user -> Toast.makeText(mContext,user.getUser_name(),Toast.LENGTH_SHORT).show());//此时已经回到安卓主线程
//        /**
//         * 以callback方式处理请求结果
//         * **/
//        ApiClient.getApiService().getUsers(company_id)
//                .enqueue(new Callback<Result<List<User>>>() {
//                    @Override
//                    public void onResponse(Call<Result<List<User>>> call, Response<Result<List<User>>> response) {
//                            List<User> users = response.body().getBody();
//                            for (User user : users){
//                                if (String.valueOf(user.getUser_id()).equals(user_id))
//                                    Toast.makeText(mContext,user.getUser_name(),Toast.LENGTH_SHORT).show();
//                            }
//                    }
//                    @Override
//                    public void onFailure(Call<Result<List<User>>> call, Throwable t) {
//
//                    }
//                });

        String org = "square";
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("加载中..");

        //获取retrofit的github地址
        // https://api.github.com/orgs/square/repos
        ApiClient.getApiService().listRepos(org) //获得一个List<Repo>
                .subscribeOn(Schedulers.io()) //在io线程请求网络
                .flatMap(repos -> Observable.from(repos))//循环遍历list
                .filter(repo -> repo.getName().contains("retrofit"))//遍历每一个repo,只保留名字包含retrofit的
                .map(repo -> repo.getUrl())//获得名字为retrofit的url地址
                .doOnSubscribe(new Action0() { //开始前show一下dialog   doOnSubscribe所在的线程为离他最近subscribeOn的线程，因此在下面回到主线程
                    @Override
                    public void call() {
                        dialog.show();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//doOnSubscribe 回到主线程用来show dialog
                .doOnCompleted(new Action0() { //完成后dismiss dialog
                    @Override
                    public void call() {
                        dialog.dismiss();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//在主线程observe
                .subscribe(s -> hello.setText(s));
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        List<Bitmap> views = new ArrayList<>();
//        File dir = new File("/sdcard");
//        File[] folders = dir.listFiles();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                for (File folder : folders) {
//                    File[] files = folder.listFiles();
//                    for (File file : files) {
//                        if (file.getName().endsWith(".png")) {
//                            final Bitmap bitmap = getBitmapFromFile(file);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    views.add(bitmap);
//                                }
//                            });
//                        }
//                    }
//                }
//            }}.start();
//
//        Observable.from(folders)
//                .subscribeOn(Schedulers.io())
//                .filter(folder -> folder.isDirectory())
//                .flatMap(folder -> Observable.from(folder.listFiles()))
//                .filter(file -> file.getName().endsWith("png"))
//                .map(file -> getBitmapFromFile(file))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(bitmap -> views.add(bitmap));
    }

    private Bitmap getBitmapFromFile(File file){
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }
    @Override
    protected void initViews() {

    }

    @Override
    protected void handleData() {

    }
}
