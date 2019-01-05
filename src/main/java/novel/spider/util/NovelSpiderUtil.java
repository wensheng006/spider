package novel.spider.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import novel.spider.NovelSiteEnum;

public final class NovelSpiderUtil {
	private static final Map<NovelSiteEnum, Map<String, String>> CONTEXT_MAP = new HashMap<NovelSiteEnum, Map<String, String>>();
	static {
		init();
	}
	private NovelSpiderUtil() {}
	
	@SuppressWarnings("unchecked")
	private static void init() {
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(new File("conf/Spider-Rule.xml"));
			Element root = doc.getRootElement();
			List<Element> sites = root.elements("site");
			for (Element site : sites) {
				List<Element> subs = site.elements();
				Map<String, String> subMap = new HashMap<String, String>();
				for (Element sub : subs) {
					String name = sub.getName();
					String text = sub.getTextTrim();
					subMap.put(name, text);
				}
				CONTEXT_MAP.put(NovelSiteEnum.getEnumByUrl(subMap.get("url")), subMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �õ���Ӧ��վ�Ľ�������
	 */
	public static Map<String, String> getContext(NovelSiteEnum novelSiteEnum) {
		return CONTEXT_MAP.get(novelSiteEnum);
	}
	
	/**
	 * ����ļ��ϲ�Ϊһ���ļ����ϲ����򣺰��ļ����ָ�����
	 * @param path ����Ŀ¼���ø�Ŀ¼�µ������ı��ļ����ᱻ�ϲ��� mergeToFile
	 * @param mergeToFile ���ϲ����ı��ļ��������������Ϊnull,�ϲ�����ļ�������path/merge.txt
	 */
	public static void multiFileMerge(String path, String mergeToFile, boolean deleteThisFile) {
		mergeToFile = mergeToFile == null ? path + "/merge.txt" : mergeToFile;
		File[] files = new File(path).listFiles(new FilenameFilter() {
			@Override 
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				int o1Index = Integer.parseInt(o1.getName().split("\\-")[0]);
				int o2Index = Integer.parseInt(o2.getName().split("\\-")[0]);
				if (o1Index > o2Index) {
					return 1;
				} else if (o1Index == o2Index){
					return 0;
				} else {
					return -1;
				}
			}
		});
		PrintWriter out = null;
		try {
			out = new PrintWriter(new File(mergeToFile), "UTF-8");
			for (File file : files) {
				BufferedReader bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));	
				String line = null;
				while ((line = bufr.readLine()) != null) {
					out.println(line);
				}
				bufr.close();
				if (deleteThisFile) {
					file.delete();
				}
			}

		} catch (IOException e1) {
			throw new RuntimeException(e1);
		} finally {
			out.close();
		}
	}
	
/*	
	//�ݹ�ɾ���ļ���  
	   private void deleteFile(File file) {  
	    if (file.exists()) {//�ж��ļ��Ƿ����  
	     if (file.isFile()) {//�ж��Ƿ����ļ�  
	      file.delete();//ɾ���ļ�   
	     } else if (file.isDirectory()) {//�����������һ��Ŀ¼  
	      File[] files = file.listFiles();//����Ŀ¼�����е��ļ� files[];  
	      for (int i = 0;i < files.length;i ++) {//����Ŀ¼�����е��ļ�  
	       this.deleteFile(files[i]);//��ÿ���ļ�������������е���  
	      }  
	      file.delete();//ɾ���ļ���  
	     }  
	    } else {  
	     System.out.println("��ɾ�����ļ�������");  
	    }  
	   }  */
	
	/**
	 * ɾ�����ļ��� �µ������ļ���	// �ݹ�ɾ��  
	 * @param  filepath
	 */
	   public static void deleteFile(String filepath) { 
		   int w = 0;
		   w = w++;
		   System.out.println("��" + w+ "�β�ѯ");
		   File delfile  = new File(filepath);
		   if(delfile.isFile()){
			   delfile.delete();
			   System.out.println( delfile.getName() + "��ɾ��"); 
		   }
		 
		 File[] dirs = new File(filepath).listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return  dir.isDirectory();
				}
			});
		 
	    if (  dirs ==null  ) {//�ж��ļ��Ƿ����  
	    	 System.out.println("��ɾ�����ļ�������");  
	    	 return;
	      }	
	   
	    for (int n = 0; n < dirs.length; n++) {
		    if (dirs[n].isDirectory()) {//�������һ��Ŀ¼  
			      File[] files = dirs[n].listFiles();//����Ŀ¼�����е��ļ� files[];  
			      for (int i = 0;i < files.length;i ++) {//����Ŀ¼�����е��ļ�  
			         deleteFile(files[i].getAbsolutePath());
			      }  
			        dirs[n].delete();//ɾ���ļ���  
				    System.out.println( dirs[n].getName()+ "��ɾ��");  
			     }  

			   }
		}

	   
	   
		/**
		 * ץȡָ��С˵��վ������
		 * @param url
		 * @return
		 * @throws Exception
		 */
	   public static String testcrawl(String url) throws Exception {
			try {
				CloseableHttpClient httpClient = createSSLClientDefault();
				 CloseableHttpResponse httpResponse = httpClient.execute(new NovelSpiderHttpGet(url));
				String result = EntityUtils.toString(httpResponse.getEntity(), NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url)).get("charset"));
				// httpClient.close();
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
	   public static  CloseableHttpClient createSSLClientDefault(){

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
