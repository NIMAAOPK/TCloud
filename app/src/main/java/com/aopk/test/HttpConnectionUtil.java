package com.aopk.test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/9/29.
 */

public class HttpConnectionUtil {
    public static final int DEFAULT_TIMEOUT = 10;

    private static Retrofit getRetrofit(String baseUrl){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public static SystemApiService getApiService(String baseUrl){
        return getRetrofit(baseUrl).create(SystemApiService.class);
    }

    public interface SystemApiService{
        @GET("{picturename}")
        Observable<ResponseBody> getImg1(@Path("picturename") String picturename);

        @GET("{picturename}")
        Observable<ResponseBody> getImg2(@Path("picturename") String picturename);
    }
}
