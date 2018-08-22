package novel.spider.configuration;

import java.io.Serializable;

/**
 * @author LiuKeFeng
 * @date   2016��10��12��
 */
public class Configuration implements Serializable {
	private static final long serialVersionUID = 427742653911093147L;
	/**
	 * ÿ���߳�Ĭ�Ͽ������ص�����½�����
	 */
	public static final int DEFAULT_SIZE = 100;
	

	/**
	 * ÿ���߳�����ÿһ�������������Դ���
	 */
	public static final int DEFAULT_TRY_TIMES = 3;
	/**
	 * ���غ���ļ�����Ļ���ַ
	 * d:/opt/bxwx.org/��������/1-2.txt
	 * ....
	 */
	private String path;
	/**
	 * ÿ���߳��ܹ����ص�����½�����
	 */
	private int size;
	private int tryTimes;
	public Configuration() {
		this.size = DEFAULT_SIZE;
		this.tryTimes = DEFAULT_TRY_TIMES;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTryTimes() {
		return tryTimes;
	}
	public void setTryTimes(int tryTimes) {
		this.tryTimes = tryTimes;
	}
	
}
