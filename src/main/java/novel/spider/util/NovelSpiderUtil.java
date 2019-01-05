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
	 * 拿到对应网站的解析规则
	 */
	public static Map<String, String> getContext(NovelSiteEnum novelSiteEnum) {
		return CONTEXT_MAP.get(novelSiteEnum);
	}
	
	/**
	 * 多个文件合并为一个文件，合并规则：按文件名分割排序
	 * @param path 基础目录，该根目录下的所有文本文件都会被合并到 mergeToFile
	 * @param mergeToFile 被合并的文本文件，这个参数可以为null,合并后的文件保存在path/merge.txt
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
	//递归删除文件夹  
	   private void deleteFile(File file) {  
	    if (file.exists()) {//判断文件是否存在  
	     if (file.isFile()) {//判断是否是文件  
	      file.delete();//删除文件   
	     } else if (file.isDirectory()) {//否则如果它是一个目录  
	      File[] files = file.listFiles();//声明目录下所有的文件 files[];  
	      for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件  
	       this.deleteFile(files[i]);//把每个文件用这个方法进行迭代  
	      }  
	      file.delete();//删除文件夹  
	     }  
	    } else {  
	     System.out.println("所删除的文件不存在");  
	    }  
	   }  */
	
	/**
	 * 删除此文件夹 下的所有文件夹	// 递归删除  
	 * @param  filepath
	 */
	   public static void deleteFile(String filepath) { 
		   int w = 0;
		   w = w++;
		   System.out.println("第" + w+ "次查询");
		   File delfile  = new File(filepath);
		   if(delfile.isFile()){
			   delfile.delete();
			   System.out.println( delfile.getName() + "已删除"); 
		   }
		 
		 File[] dirs = new File(filepath).listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return  dir.isDirectory();
				}
			});
		 
	    if (  dirs ==null  ) {//判断文件是否存在  
	    	 System.out.println("所删除的文件不存在");  
	    	 return;
	      }	
	   
	    for (int n = 0; n < dirs.length; n++) {
		    if (dirs[n].isDirectory()) {//如果它是一个目录  
			      File[] files = dirs[n].listFiles();//声明目录下所有的文件 files[];  
			      for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件  
			         deleteFile(files[i].getAbsolutePath());
			      }  
			        dirs[n].delete();//删除文件夹  
				    System.out.println( dirs[n].getName()+ "已删除");  
			     }  

			   }
		}

	   
	   
		/**
		 * 抓取指定小说网站的内容
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
		 * 简书的是https 需要绕过证书 
		 * @return
		 */
	   public static  CloseableHttpClient createSSLClientDefault(){

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
