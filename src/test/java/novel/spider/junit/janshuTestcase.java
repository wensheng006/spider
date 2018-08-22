package novel.spider.junit;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import novel.spider.NovelSiteEnum;
import novel.spider.configuration.Configuration;
import novel.spider.entitys.Chapter;
import novel.spider.entitys.ChapterUser;
import novel.spider.impl.BxwxChapterSpider;
import novel.spider.impl.DefaultChapterDetailSpider;
import novel.spider.impl.DefaultChapterSpider;
import novel.spider.impl.JanShuChapterSpider;
import novel.spider.impl.JanShuNovelDownload;
import novel.spider.impl.NovelDownload;
import novel.spider.interfaces.IChapterDetailSpider;
import novel.spider.interfaces.IChapterSpider;
import novel.spider.interfaces.INovelDownload;
import novel.spider.util.NovelSpiderUtil;

public class janshuTestcase {
	
	/**
	 * ���Եõ����������ҳ��
	 */
	@Test
	public void testGetChapterDetail() {
		IChapterDetailSpider spider = new DefaultChapterDetailSpider();
		System.out.println(spider.getChapterDetail("https://www.jianshu.com/p/5b37403f6ba6").getContent());
	}
	

	/**
	 * �õ�ĳ���û���info
	 * @throws Exception
	 */
	@Test
	public void testGetUserInfo() throws Exception {
		JanShuChapterSpider spider = new JanShuChapterSpider();
		ChapterUser user  = spider.getUserInfo("deeea9e09cbc");
		System.out.println(user);
	}
	
	
	/**
	 * �õ�ĳ���û��������������
	 * @throws Exception
	 */
	@Test
	public void testGetUserChapters() throws Exception {
		String url= "https://www.jianshu.com/u/deeea9e09cbc?order_by=top";
		JanShuChapterSpider spider = new JanShuChapterSpider();
		List<Chapter>  chapters  = spider.getsChapter(url,1000);
		for (Chapter chapter : chapters) {
			System.out.println(chapter);
		}
	}
	
	
	
	
	
	/**
	 * �ò��Է���������ȡ��������վ���½��б�
	 * @throws Exception
	 */
	@Test
	public void testGetsChapter2() throws Exception {
		HashSet<String> authorHashSet = new HashSet<String>();
        JanShuChapterSpider jausers = new JanShuChapterSpider();
        authorHashSet =  jausers.getAllAuthorList("7f0b8ce7890a", "following",2);
        System.out.println(authorHashSet.size());
	}
	
	
	/**
	 * ��������һ���û���������� ϲ��������100 ��
	 * @throws Exception
	 */
	@Test
	public void testDownSingeUser() throws Exception {
		Configuration config = new Configuration();
		String path ="D:/1_1";
		config.setPath(path);
		config.setSize(40);
		
        JanShuChapterSpider jausers = new JanShuChapterSpider();
        JanShuNovelDownload jDownload = new JanShuNovelDownload();
        String usercode ="deeea9e09cbc";
        ChapterUser user = jausers.getUserInfo(usercode);
     	String url = "https://www.jianshu.com/u/"+usercode;
	    jDownload.download(url, config, 100);
        System.out.println(user.getUsername()+"�����������!");
	}
	
	
	
	/**
	 * �������ؼ������ߵ�����.
	 * @throws Exception
	 */
	@Test
	public void betchDownJanshu() throws Exception {
		Configuration config = new Configuration();
		String path ="D:/1_1";
		config.setPath(path);
		config.setSize(40);
		String firstcode = "19dd8f5482bb";
		String spidertype ="following";
		HashSet<String> authorHashSet = new HashSet<String>();
        JanShuChapterSpider jausers =  new JanShuChapterSpider();
        JanShuNovelDownload jDownload = new JanShuNovelDownload();
        authorHashSet =  jausers.getAllAuthorList(firstcode, spidertype,10);
        System.out.println(authorHashSet.size());
        int downnum= 0;
	    for (String usercode : authorHashSet) {
	        ChapterUser user = jausers.getUserInfo(usercode);
	        if(user.getSharenum()<100){
	        	continue;  // ������С��100�Ĳ�������!
	        }
	        downnum++;
	    	String url = "https://www.jianshu.com/u/"+usercode;
	    	jDownload.download(url, config, 100);
	    	 System.out.println(user.getUsername()+"�����������!");
	   }
        
         System.out.println(downnum+"�����غ���");
	}

	
	/**
	 * ɾ����վ�¶�����ļ���
	 */
	@Test
	public void deleteBookDir( )  {
		NovelSpiderUtil.deleteFile("D:/1_1/jianshu.com/");

	}	
	
	/**
	 * �����ϲ��ļ�
	 */
	@Test
	public void  testmultDir2( )  {
		String path = "D:/1_1/jianshu.com/";
		File file  = new File(path);
		String[] files =  file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return   !name.endsWith(".txt")&& dir.isDirectory();
			}
		});
		for (int i = 0; i < files.length; i++) {
			String string = files[i];
			System.out.println(string);
			NovelSpiderUtil.multiFileMerge(path+string, path+string+".txt", false);
		}
	}
	
}
