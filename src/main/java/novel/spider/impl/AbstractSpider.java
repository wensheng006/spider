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
	 * ץȡָ��С˵��վ������
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
	 * �������https ��Ҫ�ƹ�֤�� 
	 * @return
	 */
	public static CloseableHttpClient createSSLClientDefault(String url){
	   if(!url.contains("https")){
		  return HttpClients.createDefault();
	   }
        try {
            //SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // ��JSSE�У�֤�����ι����������ʵ���˽ӿ�X509TrustManager���ࡣ���ǿ����Լ�ʵ�ָýӿڣ�������������ָ����֤�顣
            // ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
            //��������
        	
                X509TrustManager x509mgr = new X509TrustManager() {

                    //�����÷������ͻ��˵�֤�飬�������θ�֤�����׳��쳣
                    public void checkClientTrusted(X509Certificate[] xcs, String string) {
                    }
                    // �����÷���������˵�֤�飬�������θ�֤�����׳��쳣
                    public void checkServerTrusted(X509Certificate[] xcs, String string) {
                    }
                    // �����������ε�X509֤�����顣
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[] { x509mgr }, null);
                ////����HttpsURLConnection���󣬲�������SSLSocketFactory����
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            //  HttpsURLConnection����Ϳ�����������HTTPS�ˣ�������֤���Ƿ�Ȩ����������֤��ֻҪʵ���˽ӿ�X509TrustManager����MyX509TrustManager���θ�֤�顣
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();


        } catch (KeyManagementException e) {

            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

        // ����Ĭ�ϵ�httpClientʵ��.
        return  HttpClients.createDefault();

    }


}
