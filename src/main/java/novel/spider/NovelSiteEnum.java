package novel.spider;

/**
 * �Ѿ���֧�ֵ�С˵��վö��
 * @author LiuKeFeng
 * @date   2016��10��9��
 */
public enum NovelSiteEnum {
	DingDianXiaoShuo(1, "23wx.com"),
	BiQuGe(2, "biquke.com"),
	KanShuZhong(3, "kanshuzhong.com"),
	BiQuGe_2(5, "xs.la"),
	Bxwx(4, "bxwx8.org"),
	FengWu(6, "qiuwu.net"),
	NNSF(7, "kanunu8.com"),
	JANSHU(8, "jianshu.com");
	private int id;
	private String url;
	private NovelSiteEnum(int id, String url) {
		this.id = id;
		this.url = url;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public static NovelSiteEnum getEnumById(int id) {
		switch (id) {
		case 1 : return DingDianXiaoShuo;
		case 2 : return BiQuGe;
		default : throw new RuntimeException("id=" + id + "�ǲ���֧�ֵ�С˵��վ");
		}
	}
	public static NovelSiteEnum getEnumByUrl(String url) {
		for (NovelSiteEnum novelSiteEnum : values()) {
			if (url.contains(novelSiteEnum.url)) {
				return novelSiteEnum;
			}
		}
		throw new RuntimeException("url=" + url + "�ǲ���֧�ֵ�С˵��վ");
	}
}
