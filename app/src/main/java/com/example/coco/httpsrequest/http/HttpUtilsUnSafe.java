package com.example.coco.httpsrequest.http;

import android.content.Context;

import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by coco on 2017/11/7.
 */

public class HttpUtilsUnSafe {
    private static HttpUtilsUnSafe safe;

    public HttpUtilsUnSafe() {

    }
    public static HttpUtilsUnSafe getInstance(){
        if (safe==null){
            synchronized (HttpUtilsUnSafe.class){
                if (safe==null){
                    safe=new HttpUtilsUnSafe();
                }
            }
        }
        return safe;
    }
    public interface OnRequestCallBack{
        void Successed(String s);
        void Failed(Exception e);
    }
    public void get(final Context context, final String path, final OnRequestCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    SSLContext tls = SSLContext.getInstance("TLS");
                    MyX509TrustManagerUnSafe manager;
                    manager = new MyX509TrustManagerUnSafe();
                    TrustManager[]trustManagers={manager};
                    tls.init(null,trustManagers,new SecureRandom());
                    SSLSocketFactory factory = tls.getSocketFactory();

                    conn.setSSLSocketFactory(factory);
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    StringBuilder sb=new StringBuilder();
                    int flag;
                    byte[] bytes = new byte[1024];
                    while ((flag=is.read(bytes))!=-1){
                        sb.append(new String(bytes,0,flag));
                    }
                    String s = sb.toString();
                    callBack.Successed(s);

                } catch (Exception e) {
                    e.printStackTrace();
                     callBack.Failed(e);
                }

            }
        }).start();
    }

}
