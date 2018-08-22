package novel.spider.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import novel.spider.NovelSiteEnum;
import novel.spider.entitys.Chapter;
import novel.spider.interfaces.IChapterSpider;
import novel.spider.util.NovelSpiderUtil;

/**
 * 抓取任意网站的章节列表
 * @author LiuKeFeng
 * @date   2016年10月11日
 */
public abstract class AbstractChapterSpider extends AbstractSpider implements IChapterSpider {
	@Override
	public List<Chapter> getsChapter(String url) {
		try {
			String result = crawl(url);
			Document doc = Jsoup.parse(result);
			doc.setBaseUri(url);
			Elements as =null;
			as=	 doc.select(NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url)).get("chapter-list-selector"));
			List<Chapter> chapters = new ArrayList<Chapter>();
			for (Element a : as) {
				Chapter chapter = new Chapter();
				chapter.setTitle(a.text());
				chapter.setUrl(a.absUrl("href"));
				chapters.add(chapter);
			}
			return chapters;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getTitle (String url) {
		try {
			String result = crawl(url);
			Document doc = Jsoup.parse(result);
			doc.setBaseUri(url);
			Elements as = doc.select(NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url)).get("book-name"));
			if(StringUtils.isEmpty( as.text())){
				throw new RuntimeException(url+"_书名不能为空!");
			}
			return as.text();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public HashSet<String> getAllAuthorList(String firstCode, String followtype) {
		// TODO Auto-generated method stub
		return null;
	}
}
