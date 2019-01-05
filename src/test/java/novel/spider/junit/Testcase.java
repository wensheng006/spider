package novel.spider.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import novel.spider.NovelSiteEnum;
import novel.spider.configuration.Configuration;
import novel.spider.entitys.Chapter;
import novel.spider.impl.BxwxChapterSpider;
import novel.spider.impl.DefaultChapterDetailSpider;
import novel.spider.impl.DefaultChapterSpider;
import novel.spider.impl.NovelDownload;
import novel.spider.interfaces.IChapterDetailSpider;
import novel.spider.interfaces.IChapterSpider;
import novel.spider.interfaces.INovelDownload;
import novel.spider.util.NovelSpiderUtil;

public class Testcase {
	
	
	/**
	 * ����ҳ���ܷ�����ͨ
	 * @throws Exception
	 */
	@Test
	public void testHttpOpen() throws Exception {
		String url  = "https://www.biquke.com/bq/3/3714/3038841.html";
		System.out.println(NovelSpiderUtil.testcrawl(url));
	}

	

	/**
	 * Ĭ����, �õ�����ҳ��
	 */
	
	@Test
	public void testGetChapterDetail1() {
		String url  = "https://www.biquke.com/bq/3/3714/3038841.html";
		IChapterDetailSpider spider = new DefaultChapterDetailSpider();
		System.out.println(spider.getChapterDetail(url).getContent() );
	}

	/**
	 * �õ������½��б�
	 * @throws Exception
	 */
	@Test
	public void testGetChapters1() throws Exception {
		String url  = "https://www.biquke.com/bq/3/3714/";
		IChapterSpider spider = new DefaultChapterSpider();
		List<Chapter>  chapters  = spider.getsChapter(url);
		for (Chapter chapter : chapters) {
			System.out.println(chapter);
		}
	}
	
	
	


	/**
	 *  ����һ��С˵
	 */
	@Test
	public void testDownload1()  {
		INovelDownload download = new NovelDownload(); 
		Configuration config = new Configuration();
		String path ="D:/1_1";
		String url= "https://www.biquke.com/bq/43/43961/";  
		config.setPath(path);
		config.setSize(40);
		config.setTryTimes(3);
		System.out.println("���غ��ˣ��ļ������ڣ�" + 
		download.download(url, config) + "����Ͻ�ȥ�����ɣ�");
	}
	

	 /**
	  * ��������С˵
	 * @throws InterruptedException 
	  */
	@Test
	public void batchDownload1() throws InterruptedException  {
		INovelDownload download = new NovelDownload(); 
		Configuration config = new Configuration();
		String path ="D:/1_1";
		config.setPath(path);
		config.setSize(20);
		int booknum = 20;
		int bookbase =3724;
		for (int i = bookbase; i < bookbase+booknum; i++) {
			String url= "https://www.biquke.com/bq/3/"+i+"/";  
			System.out.println("���غ��ˣ��ļ������ڣ�" + 
					download.download(url, config) + "����Ͻ�ȥ�����ɣ�");
			Thread.sleep(1000);
		}

	}
	
	
	@Test
	public void DelDirfile()  {
		String path ="D:/1/biquke.com";
        NovelSpiderUtil.deleteFile(path);

	}
	
	
	
	
	
	
	

	
	

	@Test
	public void testGetSiteContext() {
		System.out.println(NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl("http://www.23wx.com/html/42/42377/")));
	}
	



	
	@Test
	public void testDownload()  {
		INovelDownload download = new NovelDownload(); 
		Configuration config = new Configuration();
		config.setPath("D:/1");
		config.setSize(100);
		config.setTryTimes(3);
		System.out.println("���غ��ˣ��ļ������ڣ�" + 
		download.download("https://www.xs.la/296_296174/", config) + "����Ͻ�ȥ�����ɣ�");
	}
	

	
	/**
	 * �����Ƿ��ܹ���ȷ���õ�������ѧ���½��б�
	 * @throws Exception
	 */
	@Test
	public void testGetsChapter3() throws Exception {
		IChapterSpider spider = new BxwxChapterSpider();
		List<Chapter>  chapters  = spider.getsChapter("http://www.qiuwu.net/html/267/267205/");
		for (Chapter chapter : chapters) {
			System.out.println(chapter);
		}
	}
	
	

	
	@Test
	public void testMultiFileMerge() {
		NovelSpiderUtil.multiFileMerge("D:/1/xs.la/ԡѪ����/", "D:/1/xs.la/ԡѪ����.txt", true);
	}
	
	/**
	 * �õ��౾�鼮
	 */
	@Test
	public void getbook( )  {
		INovelDownload download = new NovelDownload(); 
		Configuration config = new Configuration();
		config.setPath("D:/1");
		config.setSize(50);
		config.setTryTimes(10);
	   for (int i = 130; i <200; i++) {
		String bookIndex = "267" +String.valueOf( i);
	     System.out.println("���غ��ˣ��ļ������ڣ�" + 
					download.download("http://www.qiuwu.net/html/267/"+bookIndex+"/", config) + "����Ͻ�ȥ�����ɣ�");
	   }
	}
	
	/**
	 * ɾ����վ�¶�����ļ���
	 */
	@Test
	public void deleteBookDir( )  {
		NovelSpiderUtil.deleteFile("D:/1/qiuwu.net");

	}
	
	
	
}
