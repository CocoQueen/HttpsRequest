package com.example.coco.httpsrequest.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by coco on 2017/11/7.
 * 安全的
 * 对证书进行校验
 */

public class MyX509TrustManager implements X509TrustManager {
    X509Certificate x509Certificate;

    public MyX509TrustManager(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        //检查证书是否有效
        for (X509Certificate certificate :
                chain) {
            certificate.checkValidity();
            try {
                certificate.verify(x509Certificate.getPublicKey());

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
