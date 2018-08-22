package novel.spider.entitys;

public class ChapterUser {
  
	private String usercode; 
	
	private String username; 
	/**
	 * 关注数
	 */
	private int fingnum; 
	
	@Override
	public String toString() {
		return "ChapterUser [usercode=" + usercode + ", username=" + username
				+ ", fingnum=" + fingnum + ", fersnum=" + fersnum
				+ ", sharenum=" + sharenum + ", wordsnum=" + wordsnum
				+ ", lovessnum=" + lovessnum + "]";
	}
	/**
	 * 粉丝数
	 */
	private int fersnum;
	/**
	 * 文章数
	 */
	private int  sharenum;
	/**
	 * 字数
	 */
	private int wordsnum;
	/**
	 * 喜欢数
	 */
	private int lovessnum;

	public int getFingnum() {
		return fingnum;
	}
	/**
	 * index 0
	 * @param fingnum
	 */
	public void setFingnum(int fingnum) {
		this.fingnum = fingnum;
	}

	public int getFersnum() {
		return fersnum;
	}
	/**
	 * index 1
	 * @return
	 */
	public void setFersnum(int fersnum) {
		this.fersnum = fersnum;
	}
	public int getSharenum() {
		return sharenum;
	}
	/**
	 * 2
	 * @param sharenum
	 */
	public void setSharenum(int sharenum) {
		this.sharenum = sharenum;
	}
	public int getWordsnum() {
		return wordsnum;
	}
	/**
	 * 3
	 * @param wordsnum
	 */
	public void setWordsnum(int wordsnum) {
		this.wordsnum = wordsnum;
	}
	public int getLovessnum() {
		return lovessnum;
	}
	/**
	 * index 4
	 * @param lovessnum
	 */
	public void setLovessnum(int lovessnum) {
		this.lovessnum = lovessnum;
	} 
	
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fersnum;
		result = prime * result + fingnum;
		result = prime * result + lovessnum;
		result = prime * result + sharenum;
		result = prime * result
				+ ((usercode == null) ? 0 : usercode.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		result = prime * result + wordsnum;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChapterUser other = (ChapterUser) obj;
		if (fersnum != other.fersnum)
			return false;
		if (fingnum != other.fingnum)
			return false;
		if (lovessnum != other.lovessnum)
			return false;
		if (sharenum != other.sharenum)
			return false;
		if (usercode == null) {
			if (other.usercode != null)
				return false;
		} else if (!usercode.equals(other.usercode))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (wordsnum != other.wordsnum)
			return false;
		return true;
	}
}
