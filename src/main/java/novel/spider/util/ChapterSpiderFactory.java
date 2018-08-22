package novel.spider.util;

import novel.spider.NovelSiteEnum;
import novel.spider.impl.BxwxChapterSpider;
import novel.spider.impl.DefaultChapterSpider;
import novel.spider.interfaces.IChapterSpider;

public final class ChapterSpiderFactory {
	private ChapterSpiderFactory() {}
	
	/**
	 * ͨ��������url������һ��ʵ����IChapterSpider�ӿڵ�ʵ����
	 * @param url
	 * @return
	 */
	public static IChapterSpider getChapterSpider(String url) {
		NovelSiteEnum novelSiteEnum = NovelSiteEnum.getEnumByUrl(url);
		IChapterSpider chapterSpider = null;
		switch (novelSiteEnum) {
		case Bxwx :
			chapterSpider = new BxwxChapterSpider(); break;
		case DingDianXiaoShuo:
		case BiQuGe:
		case BiQuGe_2:
		case KanShuZhong :
		case FengWu :
		chapterSpider = new DefaultChapterSpider(); break;
		 default :chapterSpider = new DefaultChapterSpider(); break;
		}
		return chapterSpider;
	}
}
