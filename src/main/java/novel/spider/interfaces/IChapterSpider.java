package novel.spider.interfaces;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jsoup.select.Elements;

import novel.spider.entitys.Chapter;

public interface IChapterSpider {
	/**
	 * ������һ��������url�����Ǿ͸��㷵�����е��½��б�
	 * @param url
	 * @return
	 */
	
	
	public List<Chapter> getsChapter(String url);
	public String  getTitle(String url);
	public  HashSet<String> getAllAuthorList(String firstCode,String followtype) ;
}
