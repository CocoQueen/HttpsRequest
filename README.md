# HttpsRequest
https请求服务器，分为安全的和不安全的两种


对于Android 7.0之前自定义或非CA证书的使用，一般有两种方式：
  1、自定义X509TurstManager和HostnameVerifier，替换原有的HttpsURLConnection中的校验类。
  2、在手机中安装证书。通过设置列表完成
  
  详细使用 在这篇博客中 有介绍 链接如下，敬请移步。
  http://blog.csdn.net/JerryWu145/article/details/78467424
