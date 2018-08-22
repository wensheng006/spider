package novel.spider.impl;

import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import novel.spider.NovelSiteEnum;
import novel.spider.entitys.Chapter;
import novel.spider.util.NovelSpiderHttpGet;
import novel.spider.util.NovelSpiderUtil;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
public abstract class AbstractSpider {
	/**
	 * 抓取指定小说网站的内容
	 * @param url
	 * @return
	 * @throws Exception
	 */
	protected String crawl(String url) throws Exception {
		try {
			CloseableHttpClient httpClient = createSSLClientDefault(url);
			 CloseableHttpResponse httpResponse = httpClient.execute(new NovelSpiderHttpGet(url));
			String result = EntityUtils.toString(httpResponse.getEntity(), NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url)).get("charset"));
			 httpClient.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			
		}
	}
	
	/**
	 * 简书的是https 需要绕过证书 
	 * @return
	 */
	public static CloseableHttpClient createSSLClientDefault(String url){
	   if(!url.contains("https")){
		  return HttpClients.createDefault();
	   }
        try {
            //SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 在JSSE中，证书信任管理器类就是实现了接口X509TrustManager的类。我们可以自己实现该接口，让它信任我们指定的证书。
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            //信任所有
        	
                X509TrustManager x509mgr = new X509TrustManager() {

                    //　　该方法检查客户端的证书，若不信任该证书则抛出异常
                    public void checkClientTrusted(X509Certificate[] xcs, String string) {
                    }
                    // 　　该方法检查服务端的证书，若不信任该证书则抛出异常
                    public void checkServerTrusted(X509Certificate[] xcs, String string) {
                    }
                    // 　返回受信任的X509证书数组。
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[] { x509mgr }, null);
                ////创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            //  HttpsURLConnection对象就可以正常连接HTTPS了，无论其证书是否经权威机构的验证，只要实现了接口X509TrustManager的类MyX509TrustManager信任该证书。
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();


        } catch (KeyManagementException e) {

            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

        // 创建默认的httpClient实例.
        return  HttpClients.createDefault();

    }


}
