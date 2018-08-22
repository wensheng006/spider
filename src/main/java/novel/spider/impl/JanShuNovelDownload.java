package novel.spider.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import novel.spider.NovelSiteEnum;
import novel.spider.configuration.Configuration;
import novel.spider.entitys.Chapter;
import novel.spider.entitys.ChapterDetail;
import novel.spider.interfaces.IChapterDetailSpider;
import novel.spider.interfaces.IChapterSpider;
import novel.spider.interfaces.INovelDownload;
import novel.spider.util.ChapterDetailSpiderFactory;
import novel.spider.util.ChapterSpiderFactory;
import novel.spider.util.NovelSpiderUtil;

/**
 * ���ʵ�ֶ��߳�����������վ��С˵
	1.�õ�����վ��ĳ��С˵�������½ڣ��½��б�
	2.ͨ�����㣬����Щ�½ڷ����ָ���������̣߳�������ȥ������Ȼ�󱣴浽�ı��ļ���
	3.֪ͨ���̣߳�����Щ��ɢ��С�ļ��ϲ���һ�����ļ��������Щ��Ƭ��С�ļ�ɾ������
 *  Ӧ�� �и�������, ��һ��chapters, bookname, Configuration ��, 
 *  ǰ����߼���������Զ���
 */
public class JanShuNovelDownload implements INovelDownload {

	@Override
	public String download(String url, Configuration config, int lovenum) {
		JanShuChapterSpider spider = new JanShuChapterSpider();
		List<Chapter> chapters = spider.getsChapter(url, lovenum);
		
		if(chapters==null || chapters.size() <15){
			return null;
		}
		String bookname   =  spider.getTitle(url);
		//ĳ���߳��������֮����ø������̣߳������غ���
		//���е��̶߳����غ��ˣ��ϲ�������
		int size = config.getSize();
		// 2010�� 100��
		// ��Ҫ21���߳�
		// һ��int / int ���ֻ����int
		// һ��double / double �����Ȼ��double
		//һ��double / int �����double
		//Math.ceil(double) 10 -> 10 10.5->11 10.1 ->11 -10 -> -10 -10.1 -> 10 -10.5 -> -10
		int maxThreadSize = (int)Math.ceil(chapters.size() * 1.0 / size);
		Map<String, List<Chapter>> downloadTaskAlloc = new HashMap<String, List<Chapter>>();
		for (int i = 0; i < maxThreadSize; i++) {
			// i = 0 0-99	-- > 100  0 100
			// i = 1 100-199
			// i = 2 200-299 
			// i = 3 300-399 
			// ...
			// i = 19 1900-1999
			// i = 20 2000-2052
			// �ܹ���2053��
			int fromIndex = i * config.getSize();
			int toIndex = i == maxThreadSize - 1 ? chapters.size() : i * config.getSize() + config.getSize();
			downloadTaskAlloc.put(fromIndex + "-" + toIndex, chapters.subList(fromIndex, toIndex));
		}
		ExecutorService service = Executors.newFixedThreadPool(maxThreadSize);
		Set<String> keySet = downloadTaskAlloc.keySet();
		List<Future<String>> tasks = new ArrayList<Future<String>>();
		
		//ͨ�������δ���Ϳ��Դ���ȱʧ��·��
		String savePathdir = config.getPath() + "/" + NovelSiteEnum.getEnumByUrl(url).getUrl()+"/";
		String savebookpath = savePathdir+bookname;
		new File(savebookpath).mkdirs();
		for (String key : keySet) {
			tasks.add(service.submit(new DownloadCallable(savebookpath+"/"+ key+"+"+bookname+".txt", downloadTaskAlloc.get(key), config.getTryTimes())));
		}
		service.shutdown();

		for (Future<String> future : tasks) {
			try {
						System.out.println(future.get() + ",������ɣ�");
			} catch ( Exception e) {
						e.printStackTrace();
			}
		}
		NovelSpiderUtil.multiFileMerge(savebookpath, savebookpath+".txt", false);
		return savePathdir + "/"+bookname+".txt";
	}
	
	public String download(String url, Configuration config) {
		return download(url,config,0);
	}
	

}

class DownloadCallable2 implements Callable<String> {
	private List<Chapter> chapters;
	private String path;
	private int tryTimes;
	public DownloadCallable2(String path, List<Chapter> chapters, int tryTimes) {
		this.path = path;
		this.chapters = chapters;
		this.tryTimes = tryTimes;
	}
	@Override
	public String call() throws Exception {
		try {
			PrintWriter out = new PrintWriter(new File(path), "UTF-8");
			for (Chapter chapter : chapters) {
				IChapterDetailSpider spider = ChapterDetailSpiderFactory.getChapterDetailSpider(chapter.getUrl());
				ChapterDetail detail = null;
				
				for (int i = 0; i < tryTimes; i++) {
					try {
						detail = spider.getChapterDetail(chapter.getUrl());
						out.println(detail.getTitle());
						out.println(detail.getContent());
						break;
					} catch (RuntimeException e) {
						System.err.println("���Ե�[" + (i + 1) + "/" + tryTimes + "]������ʧ���ˣ�");
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return path;
	}
	
}