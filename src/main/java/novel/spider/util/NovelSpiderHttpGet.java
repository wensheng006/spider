package novel.spider.util;

import java.net.URI;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

public class NovelSpiderHttpGet extends HttpGet {

	public NovelSpiderHttpGet() {
	}

	public NovelSpiderHttpGet(URI uri) {
		super(uri);
	}

	public NovelSpiderHttpGet(String uri) {
		super(uri);
		setDefaultConfig();
	}
	
	/**
	 * ����Ĭ�ϵ��������
	 */
	private void setDefaultConfig() {
		this.setConfig(RequestConfig.custom()
				.setSocketTimeout(20000)
				.setConnectTimeout(100000)	//�������ӷ������ĳ�ʱʱ��
				.setConnectionRequestTimeout(100000)	//���ôӷ�������ȡ���ݵĳ�ʱʱ��
				.build());
		this.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");	//��������ͷ
	}

}
