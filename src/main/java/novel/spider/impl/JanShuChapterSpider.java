package novel.spider.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import novel.spider.NovelSiteEnum;
import novel.spider.entitys.Chapter;
import novel.spider.entitys.ChapterUser;
import novel.spider.util.NovelSpiderUtil;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JanShuChapterSpider extends AbstractChapterSpider {

	
	
	public List<Chapter> getsChapter(String url) {
		return getsChapter( url,  100 );
	}
	
	public List<Chapter> getsChapter(String url, int lovenum) {
		return getsChapter( url,  lovenum, 10 );
	}
	
	
	public List<Chapter> getsChapter(String url, int lovenum, int pages) {
		try {
			String result = crawl(url);
			Document doc = Jsoup.parse(result);
			doc.setBaseUri(url);
			Elements as =null;
			if(lovenum==0){
				as=	 doc.select(NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url)).get("chapter-list-selector"));
			}else{
				as = getLoveEls(url, lovenum, pages);
				if(as.size()==0){
					return null;
				}
			}
			List<Chapter> chapters = new ArrayList<Chapter>();
			for (Element a : as) {
				Chapter chapter = new Chapter();
				chapter.setTitle(a.text());
				chapter.setUrl(a.absUrl("href"));
/*				//getUserlaberindex
				chapter.setFingnum(getUserlaberindex(url, 0));
				chapter.setFersnum(getUserlaberindex(url, 1));
				chapter.setSharenum(getUserlaberindex(url, 2));
				chapter.setLovessnum(getUserlaberindex(url, 4));*/
				chapters.add(chapter);
			}
			return chapters;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Elements getLoveEl (String url, int lovenum) {
		try {
			Elements LoveEl = new Elements(1) ;
	
			String result = crawl(url);
			Document doc = Jsoup.parse(result);
			doc.setBaseUri(url);
			Map<String, String> Ruel =   NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url));
			Elements ConEls = doc.select(Ruel.get("chapter-list-content")); 
			for (Element con : ConEls) {
				Element loveel  = 	con.select(Ruel.get("likes-count-selector")).get(0);
				if(StringUtils.isNumeric(loveel.text()) ){
				int lovValue= Integer.valueOf( loveel.text());
		        if(lovValue > lovenum){
		        	LoveEl.add (  con.select(Ruel.get("chapter-list-selector")).first() ); 
		        }
				}
			}
			 return LoveEl;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 *  index 0 关注
	 *        1 粉丝
	 *        2文章数
	 * @param url
	 * @param index
	 * @return
	 */
	public int getUserlaberindex (String url, int index) {
		int arrownum = 0;
		try {
			String result = crawl(url);
			Document doc = Jsoup.parse(result);
			Map<String, String> Ruel =   NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url));
			String num=  doc.select(".meta-block a p").get(index).text();
			arrownum = Integer.valueOf(num);   // User 下面总的文章数
			arrownum = (int) Math.ceil((double) arrownum/10);   // 每次加载10页
		} catch (Exception e) {
			// TODO: handle exception
		}
		return arrownum;
	}
	
	
	/**
	 *  
	 * @param index
	 * @return
	 */
	public ChapterUser getUserInfo (String usercode) {
		String url = "https://www.jianshu.com/u/"+usercode;
		ChapterUser user= new ChapterUser();
		try {
			String result = crawl(url);
			Document doc = Jsoup.parse(result);
			Map<String, String> ruls = NovelSpiderUtil.getContext(NovelSiteEnum.getEnumByUrl(url));
			Elements as = doc.select(ruls.get("book-name"));
			String username =  as.text();
			if(StringUtils.isEmpty(username)){
				throw new RuntimeException(url+"_用户名不能为空!");
			}
			user.setUsercode(usercode);
			user.setUsername(username);
			Elements userel=	doc.select(ruls.get("userinfo-selector"));
			user.setFingnum(Integer.valueOf( userel.get(0).text()));
			user.setFersnum(Integer.valueOf( userel.get(1).text()));
			user.setSharenum(Integer.valueOf( userel.get(2).text()));
			user.setLovessnum(Integer.valueOf( userel.get(4).text()));


		} catch (Exception e) {
			throw new RuntimeException("得到用户信息出错!");
		}
		return user;
	}
	
	
	public Elements getLoveEls (String url, int lovenum, int pages) {
		 url = url+"?order_by=top&page=";  
		 Elements LoveEls = new Elements(1) ;
		 int arrownum = getUserlaberindex(url,2) ;
			if(arrownum <pages){
				pages = arrownum;   // 总页数
			}
		for (int i = 1; i < arrownum; i++) {
			String turl = url+i;
			LoveEls.addAll ( getLoveEl(turl,lovenum));
		}
		return LoveEls;
		
	}
	
	
	
	
	/**
	 * 给我个人URl,返回 用户list code.
	 * HashSet<code>
	 * @param url
	 * @return
	 */
	public HashSet<String> getSingePageAuthorList(String url) {
		HashSet<String> auths = new HashSet<String>();
		
		try {
			String result = crawl(url);
			Document doc = Jsoup.parse(result);
			doc.setBaseUri(url);
			Elements userlist = doc.select(".user-list li a[class=name]");
			for (Element user : userlist) {
				String usercode2 = user.attr("href");
				String usercode =  usercode2.split("/")[2];
				auths.add(usercode);
			}
		     return auths;
		 } catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到所有关注的用户的code
	 */
	public  HashSet<String> getAuthorList(String firstCode, String followtype) {	
		HashSet<String> auths = new HashSet<String>();
		String url = "https://www.jianshu.com/users/"+firstCode+"/"+followtype;
		String append = "?page=";  	
		int laberindex = 0;
		if(followtype.equals("followers")){
			laberindex = 1;
		}
		//int fingNum = 1; //getUserlaberindex(url, laberindex);
		int fingNum =getUserlaberindex(url, laberindex);
		try {
			for (int i = 1; i <=fingNum; i++) {
			    String	newUrl = url+append+i;
			    auths.addAll (getSingePageAuthorList(newUrl));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return auths;
	}
	
	

	
	
	/**
	 * 得到所有用户的code
	 */
	public  HashSet<String>  getAllAuthorList(String firstCode, String followtype, int size) {
	   HashSet<String> hashSet = new HashSet<String>();
		ArrayList<String> tem = new ArrayList<String>();
		tem.add(firstCode);
        for (int i = 0; i < size  ; i++) {
        	
        	 HashSet<String> hashSet1 = getAllAuthorList(tem.get(i), followtype);
        	 tem.addAll(hashSet1 );
        	if(i%20==0){
            	tem  = (ArrayList<String>) removeDuplicate(tem);   // array 去重
        	}
        	size = size>tem.size() ?tem.size():size;
		}
        hashSet.addAll(tem);
		return hashSet;
	}
	
	
	/**
	 * 得到所有用户的code
	 */
	public HashSet<String> getAllAuthorList(String firstCode, String followtype) {
		HashSet<String> allauths = new HashSet<String>();
		allauths.addAll( getAuthorList(firstCode, followtype));
		return allauths;
	}
	
	/**
     * 集合去重
     * 
     * @param list
     * @return
     */
    private <T> List<T> removeDuplicate(List<T> list) {
        Set<T> set = new HashSet<T>();
        List<T> newList = new ArrayList<T>();
        for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
            T element = (T) iter.next();
            if (set.add(element))
                newList.add(element);
        }
        return newList;
    }

}
