package com.github.gaodeha.common.util;


import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    private static int SocketTimeout = 3000;//3秒
    private static int ConnectTimeout = 3000;//3秒
    private static Boolean SetTimeOut = true;

    private static CloseableHttpClient getHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    /**
     * get
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> queries) throws IOException {
        String requestUri = buildUri(url, queries);
        HttpGet httpGet = new HttpGet(requestUri);
        return executeHttpRequest(httpGet);
    }



    /** post
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @param params  post form 提交的参数
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> queries, Map<String, String> params) throws IOException {
        String requestUri = buildUri(url, queries);
        //指定url,和http方式
        HttpPost httpPost = new HttpPost(requestUri);
        //添加参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null && params.keySet().size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        //请求数据
        return executeHttpRequest(httpPost);
    }


    /** post
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @param data  提交的参数json
     * @return
     * @throws IOException
     */
    public static String postJSON(String url, Map<String, String> queries, Object data) throws IOException {
        String requestUri = buildUri(url, queries);
        //指定url,和http方式
        HttpPost httpPost = new HttpPost(requestUri);
        //添加参数
        StringEntity postingString = new StringEntity(JSON.toJSONString(data));
        httpPost.setEntity(postingString);
        httpPost.setHeader("Content-type", "application/json");
        return executeHttpRequest(httpPost);
    }

    private static String buildUri(String url, Map<String, String> queries) {
        StringBuilder sb = new StringBuilder(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + (String) entry.getKey() + "=" + (String) entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + (String) entry.getKey() + "=" + (String) entry.getValue());
                }
            }
        }
        return sb.toString();
    }


    private static String executeHttpRequest(HttpRequestBase request) {
        String responseBody = null;
        //请求数据
        CloseableHttpResponse response = null;
        if (SetTimeOut) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(SocketTimeout)
                    .setConnectTimeout(ConnectTimeout).build();//设置请求和传输超时时间
            request.setConfig(requestConfig);
        }
        try {
            response = getHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity, Consts.UTF_8);
            } else {
                System.out.println("http return status error:" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignore) {
                }
            }
        }
        return responseBody;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(HttpClientUtils.get("https://www.baidu.com", null));
    }
}
