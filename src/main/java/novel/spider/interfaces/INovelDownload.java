package novel.spider.interfaces;

import novel.spider.configuration.Configuration;

public interface INovelDownload {
	/**
	 * ����˵�����ص�D:/novel/biquge.tw/��������/��������.txt
	 * @param url ���url��ָĳ��С˵���½��б�ҳ��
	 * @return
	 */
	public String download(String url, Configuration config);
	public String download(String url, Configuration config, int lovenum);
}
