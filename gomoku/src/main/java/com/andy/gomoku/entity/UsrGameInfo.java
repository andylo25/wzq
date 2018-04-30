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
	
	private Integer score;
	
	private Integer titleSort;
	private Integer winCount;
	private Integer loseCount;
	
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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
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

	public void addScore(int score){
		this.score += score;
		int titleSor = GameConf.getTitleSort(score);
		if(this.titleSort == null || this.titleSort != titleSor){
			this.setTitleSort(titleSor);
		}
	}

	public Integer getWinCount() {
		return winCount;
	}

	public void setWinCount(Integer winCount) {
		this.winCount = winCount;
	}

	public Integer getLoseCount() {
		return loseCount;
	}

	public void setLoseCount(Integer loseCount) {
		this.loseCount = loseCount;
	}

	public static String table() {
		return "usr_game_info";
	}
	
	
}
