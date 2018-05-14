package com.andy.gomoku.entity;

import com.andy.gomoku.utils.GameConf;

public class UsrGameInfo extends BaseEntity{

	private static final long serialVersionUID = 1L;

	public UsrGameInfo() {
	}
	
	public UsrGameInfo(Long id) {
		super(id);
	}
	
	private Long uid;
	
	private Long coin;
	
//	private Integer score;
	
	private Integer titleSort;
	private Integer winCount;
	private Integer loseCount;
	private Integer cid;
	
	private String title;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getCoin() {
		return coin;
	}

	public void setCoin(Long coin) {
		this.coin = coin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTitleSort() {
		return titleSort;
	}

	public void setTitleSort(Integer titleSort) {
		this.titleSort = titleSort;
		if(titleSort != null){
			setTitle(GameConf.getTitle(titleSort).getTitle());
		}
	}

	public int addCoin(int coin){
		if(this.coin == null){
			this.coin = 0L;
		}
		Long old = this.coin;
		this.coin += coin;
		if(this.coin < 0)this.coin = 0L;
		int titleSor = GameConf.getTitleSort(this.coin.intValue());
		if(this.titleSort == null || this.titleSort != titleSor){
			this.setTitleSort(titleSor);
		}
		return (int) (this.coin - old);
	}

	public Integer getWinCount() {
		return winCount == null?0:winCount;
	}

	public void setWinCount(Integer winCount) {
		this.winCount = winCount;
	}

	public Integer getLoseCount() {
		return loseCount==null?0:loseCount;
	}

	public void setLoseCount(Integer loseCount) {
		this.loseCount = loseCount;
	}

	public static String table() {
		return "usr_game_info";
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}
	
	
}
