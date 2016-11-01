package name.arche.commonandroidapplication.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import name.arche.commonandroidapplication.R;
import name.arche.commonandroidapplication.api.ApiClient;
import name.arche.commonandroidapplication.models.Repo;
import name.arche.retrofitutil.api.Result;
import name.arche.retrofitutil.api.RetrofitClient;
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

        String org = "square";
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("加载中..");

        //获取retrofit的github地址
        // https://api.github.com/orgs/square/repos
//        ApiClient.getInstance().listRepos(org) //获得一个List<Repo>
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

        ApiClient.getInstance().listReposByCallback(org,new RetrofitClient.CallBack(){
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onResponse(Result result) {
                //因为接口返回不符合Result格式，该处会闪退，Result具体内容根据实际情况进行调整
                dialog.dismiss();
                List<Repo> repos = (List<Repo>) result.getBody();
                for (Repo repo:repos){
                    if (repo.getName().contains("retrofit")){
                        hello.setText(repo.getUrl());
                    }
                }
            }

            @Override
            public void onProgressUpdate(long progress, long total, boolean done) {
                Log.d("zzf","progress:" + progress + ",total:" + total + ",current percent:" + (progress/total));
            }
        });
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
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                for (File folder : folders) {
//                    if (folder.isDirectory()) {
//                        File[] files = folder.listFiles();
//                        for (File file : files) {
//                            if (file.getName().endsWith(".png")) {
//                                final Bitmap bitmap = getBitmapFromFile(file);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        views.add(bitmap);
//                                    }
//                                });
//                            }
//                        }
//                    }
//
//                }
//            }
//        }.start();
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

    private Bitmap getBitmapFromFile(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void handleData() {

    }
}
