package com.momassistant.utils;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    private static MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
    private static HttpClient client = new HttpClient(httpConnectionManager);

    static {
        //每主机最大连接数和总共最大连接数，通过hosfConfiguration设置host来区分每个主机
        client.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(8);
        client.getHttpConnectionManager().getParams().setMaxTotalConnections(300);
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(5000);
        client.getHttpConnectionManager().getParams().setTcpNoDelay(true);
        client.getHttpConnectionManager().getParams().setLinger(1);
        //失败的情况下会进行3次尝试,成功之后不会再尝试
        client.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
    }

    public static String executePostMethod(PostMethod postMethod, String json) {
        String url=null;
        InputStream is = null;
        byte[] responseJson = null;
        try {
            url=postMethod.getURI().toString();
            is = new ByteArrayInputStream(json.getBytes("utf-8"));
            postMethod.setRequestBody(is);
            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                responseJson = postMethod.getResponseBody();
                return new String(responseJson, "utf-8");
            }
        } catch (Exception e) {
            logger.error("HttpClientUtils.executePostMethod error. url="+url, e);
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.info("IOException in HttpClientUtils::executePostMethod()",e);
                }
            }
            if(postMethod instanceof HttpMethodBase){
                ((HttpMethodBase)postMethod).releaseConnection();
            }
        }
        return null;
    }


    public static String executeGetMethod(GetMethod getMethod) {
        String url=null;
        byte[] responseJson = null;
        try {
            url=getMethod.getURI().toString();
            int status = client.executeMethod(getMethod);
            if (status == HttpStatus.SC_OK) {
                responseJson = getMethod.getResponseBody();
                return new String(responseJson, "utf-8");
            }
        } catch (Exception e) {
            logger.error("HttpClientUtils.executePostMethod error. url="+url, e);
        }finally {
            if(getMethod instanceof HttpMethodBase){
                ((HttpMethodBase)getMethod).releaseConnection();
            }
        }
        return null;
    }

}
