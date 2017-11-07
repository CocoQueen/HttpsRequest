package com.example.coco.httpsrequest.http;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by coco on 2017/11/7.
 */

public class HttpUtilsSafe2 {
    private static HttpUtilsSafe2 safe;

    public HttpUtilsSafe2() {

    }
    public static HttpUtilsSafe2 getInstance(){
        if (safe==null){
            synchronized (HttpUtilsSafe2.class){
                if (safe==null){
                    safe=new HttpUtilsSafe2();
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
                    //改为s
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    //初始化SSLContext
                    SSLContext tls = SSLContext.getInstance("TLS");

                    String defaultType = KeyStore.getDefaultType();
                    KeyStore instance = KeyStore.getInstance(defaultType);
                    instance.load(null);
                    instance.setCertificateEntry("srca",getX509Certificate(context));
                    String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();//获取默认算法
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(defaultAlgorithm);
                    tmf.init(instance);


                    TrustManager[]trustManagers=tmf.getTrustManagers();
                    tls.init(null,trustManagers,new SecureRandom());
                    //ssl工厂
                    SSLSocketFactory factory = tls.getSocketFactory();
                    //添加一个主机名称校验器
                    conn.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            if (hostname.equals("kyfw.12306.cn")){
                                return true;
                            }
                            return false;
                        }
                    });
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
    //拿到自己的证书
    X509Certificate getX509Certificate(Context context) throws IOException, CertificateException {
        InputStream is = context.getAssets().open("srca.cer");
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) factory.generateCertificate(is);
        return certificate;
    }

}
