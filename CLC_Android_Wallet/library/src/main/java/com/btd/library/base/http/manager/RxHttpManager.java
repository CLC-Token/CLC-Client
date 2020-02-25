package com.btd.library.base.http.manager;


import com.btd.library.base.config.HttpConfig;
import com.btd.library.base.http.callback.HttpCallback;
import com.btd.library.base.http.exception.CreateInterceptorException;
import com.btd.library.base.http.subscriber.HttpSubscriber;
import com.btd.library.base.util.JsonHelpUtil;
import com.btd.library.base.util.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * 网络请求管理类
 * Created by yzy on 2018/10/18 14:55
 *
 */
public class RxHttpManager {
    private static RxHttpManager instance;

    private RxHttpManager(){}

    public static RxHttpManager getInstance(){
        if(instance == null){
            synchronized (RxHttpManager.class){
                if(instance == null){
                    instance = new RxHttpManager();

                }
            }
        }
        return instance;
    }

    ExecutorService fixedThreadPool=null;
    private void initThreadPool(){
        fixedThreadPool= Executors.newFixedThreadPool(5);
    }

    /**
     * 开始请求
     * @return 请求的返回值,需要时,可取消请求
     */
    public Subscription startHttp(Observable observable , HttpCallback httpCallback){
        if(observable == null){
            return null;
        }
        if(null==fixedThreadPool){
            initThreadPool();
        }

        //http请求回调
        HttpSubscriber subscriber = new HttpSubscriber(httpCallback);
        Observable newObservable = observable
               // .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.from(fixedThreadPool))
              //  .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

                //                .map(new Func1() {
                //                    @Override
                //                    public Object call(Object o) {
                //                        return null;
                //                    }
                //                })
         if(httpCallback != null){
             httpCallback.onSuccess(newObservable);
         }
        return newObservable.subscribe(subscriber);
    }

    private static final int CONNECT_TIME = 6;

    /**
     * 获取请求
     * @param baseUrl
     * @return
     */
    public Retrofit getRetrofit(String baseUrl){
        return getRetrofit(baseUrl , CONNECT_TIME);
    }

    public Retrofit getRetrofit(String baseUrl , int connectTime){
        //设置Https不检验证书
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = (hostname, session) -> true;

        //初始化Okhttp.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory() , x509TrustManager)
                    .hostnameVerifier(DO_NOT_VERIFY)
                    .connectTimeout(connectTime , TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(5,1,TimeUnit.SECONDS))
                  //  .cookieJar(getCookieJar())
                    .addInterceptor(new MyInterceptor());
        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(JsonHelpUtil.formatGson()
                        .disableHtmlEscaping().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //设置支持返回Observable
                .build();
    }

    /**
     * 自定义拦截器
     */
    private class MyInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            String url = oldRequest.url().toString();
            RequestBody requestBody =  oldRequest.body();
            okhttp3.Response response = chain.proceed(chain.request());
            ResponseBody values = response.body();
            if(values == null){
                return response;
            }
            byte[] resp = values.bytes();
            String json = new String(resp, "UTF-8");

            try {
                Buffer buffer = new Buffer();
                Charset charset = null;
                if(requestBody != null) {
                    requestBody.writeTo(buffer);
                    charset = Charset.forName("UTF-8");
                }
                LogUtils.i(url + "\n"
                        + (charset != null ? buffer.readString(charset):"")
                        + "\n" + json);
                if(response.code() == HttpConfig.ServerResponse.NOT_AUTH){
                    throw new CreateInterceptorException(json , response.code());
                }
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }finally {
                // 这里值得注意。由于前面value.bytes()把响应流读完并关闭了，所以这里需要重新生成一个response，否则数据就无法正常解析了
                response = response.newBuilder()
                        .body(ResponseBody.create(null, resp))
                        .build();
            }
            return response;
        }

        /**
         * 添加公共参数
         *
         * @param oldRequest
         * @return
         */
        private Request addParam(Request oldRequest) {

            HttpUrl.Builder builder = oldRequest.url()
                    .newBuilder();
            return oldRequest.newBuilder()
                    .method(oldRequest.method(), oldRequest.body())
                    .headers(oldRequest.headers())
//                    .addHeader("Cookie" , "wxj.sid=s%3AbTNdHvBVxlc9-B13cALlfMdGihfd2bPx.b0VW85UZQpdplwUJo5K3EMKn4wXR3S%2BRqlvREKetN4I")
                    .url(builder.build())
                    .build();
        }
    }

    private CookieJar mCookieJar;

    public void setCookieJar(CookieJar cookieJar){
        this.mCookieJar = cookieJar;
    }

    public CookieJar getCookieJar(){
        return mCookieJar;
    }
}
