java 多线程爬虫    jdk 1.7+ 

思路是给一个数据的主页面,下载一本小说.
 

novel.spider.junit.Testcase 测试类



1  以笔趣阁为例
/**
	 * 测试页面能否连接通
	 * @throws Exception
	 */
	@Test
	public void testHttpOpen() throws Exception {
		String url  = "https://www.biquke.com/bq/3/3714/3038841.html";
		System.out.println(NovelSpiderUtil.testcrawl(url));
	}
	
	
	/**
	 * 默认类, 得到详情页面
	 */
	
	@Test
	public void testGetChapterDetail1() {
		String url  = "https://www.biquke.com/bq/3/3714/3038841.html";
		IChapterDetailSpider spider = new DefaultChapterDetailSpider();
		System.out.println(spider.getChapterDetail(url).getContent() );
	}
	
	
	
	/**
	 * 得到所有章节列表
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
	 *  下载一本小说
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
		System.out.println("下载好了，文件保存在：" + 
		download.download(url, config) + "这里，赶紧去看看吧！");
	}
	