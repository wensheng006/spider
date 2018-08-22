package novel.spider.util;

import novel.spider.NovelSiteEnum;

import novel.spider.impl.DefaultChapterDetailSpider;
import novel.spider.interfaces.IChapterDetailSpider;

public final class ChapterDetailSpiderFactory {
	private ChapterDetailSpiderFactory() {}
	/**
	 * ͨ��������url�õ�ʵ����IChapterDetailSpider�ľ���ʵ����
	 * @param url
	 * @return
	 */
	public static IChapterDetailSpider getChapterDetailSpider(String url) {
		IChapterDetailSpider spider = null;
		NovelSiteEnum novelSiteEnum = NovelSiteEnum.getEnumByUrl(url);
		switch (novelSiteEnum) {
/*		case DingDianXiaoShuo :
		case BiQuGe :
		case BiQuGe_2 :
		case KanShuZhong :
		case FengWu :
		case Bxwx :
			spider = new DefaultChapterDetailSpider();
			break;*/
		default:
			spider = new DefaultChapterDetailSpider();
		break;
		}
		return spider;
	}
}
