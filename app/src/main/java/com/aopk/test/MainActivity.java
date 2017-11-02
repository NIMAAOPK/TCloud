package com.aopk.test;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private TextView tvProgress;
    private ImageView img;
    private Button btnDownload;
private ;

    CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        img = (ImageView) findViewById(R.id.img_result);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDowmload();
            }
        });
        findViewById(R.id.btn_retrofit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RxJava_RetrofitActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void startDowmload() {






        HttpConnectionUtil.getApiService("http://i-2.shouji56.com/2015/5/6/")
                .getImg1("df357912-c71d-45fe-a8f7-28b27c4b5077.jpg")
                .compose(new ObservableTransformer<ResponseBody, Object>() {
                    @Override
                    public ObservableSource<Object> apply(Observable<ResponseBody> upstream) {
                        return null;
                    }
                }).subscribe(new Observer<ResponseBody>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });













        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                //Glide.with(MainActivity.this).load("http://i-2.shouji56.com/2015/5/6/df357912-c71d-45fe-a8f7-28b27c4b5077.jpg").into(img);
                for (int i = 0; i < 100; i++) {
                    if (i % 20 == 0) {
                        try {
                            Thread.sleep(500); //模拟下载的操作。
                        } catch (InterruptedException exception) {
                            if (!e.isDisposed()) {
                                e.onError(exception);
                            }
                        }
                        e.onNext(i);
                    }
                }
                e.onComplete();
            }
        });
        DisposableObserver<Integer> disposableObserver = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer value) {
                tvProgress.setText("下载进度::"+value);
            }

            @Override
            public void onError(Throwable e) {
                tvProgress.setText("下载出错");
            }

            @Override
            public void onComplete() {
                //img.setImageResource(R.mipmap.ic_launcher_round);
                tvProgress.setText("下载完成");
                Glide.with(MainActivity.this).load("http://i-2.shouji56.com/2015/5/6/df357912-c71d-45fe-a8f7-28b27c4b5077.jpg").into(img);
            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.from(new Executor() {
            @Override
            public void execute(@NonNull Runnable runnable) {
                runOnUiThread(runnable);
            }
        })).subscribe(disposableObserver);
        disposable.add(disposableObserver);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
