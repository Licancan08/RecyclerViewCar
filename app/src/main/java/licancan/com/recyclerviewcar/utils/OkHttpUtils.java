package licancan.com.recyclerviewcar.utils;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by robot on 2017/10/25.
 */

public class OkHttpUtils {
    private static OkHttpClient okHttpClient;
    //私有化的构造器
    private OkHttpUtils(){}
    public static OkHttpClient getInstance()
    {
        if(okHttpClient==null)
        {
            synchronized (OkHttpUtils.class)
            {
                //缓存的目录
                File sd_cache=new File(Environment.getExternalStorageDirectory(),"ok_cache");
                //缓存存储的大小
                int cacheSize=1024*1024*10;
                //OkHttp3拦截器
                HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.i("拦截器xxx", message.toString());
                    }
                });
                //OkHttp3的拦截器日志分类  4种
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                okHttpClient=new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30,TimeUnit.SECONDS)
                        .connectTimeout(60,TimeUnit.SECONDS)
                        .cache(new Cache(sd_cache,cacheSize))
                        .build();
            }
        }
        return okHttpClient;
    }
    //get请求
    public static void doGet(String url, Callback callback)
    {
        OkHttpClient okHttpClient=getInstance();
        //创建Request
        Request request=new Request.Builder()
                .url(url)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(callback);
    }
    //post请求
    public static void doPost(String url, Map<String, String> params, Callback callback)
    {
        OkHttpClient okHttpClient=getInstance();

        FormBody.Builder builder=new FormBody.Builder();
        if(params!=null)
        {
            for(String key:params.keySet())
            {
                builder.add(key,params.get(key));
            }
        }

        Request request=new Request.Builder()
                .post(builder.build())
                .url(url)
                .build();

        Call call=okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
