package com.andy.gomoku.entity;

public class UsrGameLog extends BaseEntity{

	private static final long serialVersionUID = 1L;

	public UsrGameLog() {
	}
	
	public UsrGameLog(Long id) {
		super(id);
	}
	
	public UsrGameLog(UsrGameInfo gameInfo) {
		this.setCoin(gameInfo.getCoin());
		this.setUid(gameInfo.getUid());
	}

	private Long uid;
	
	private Long coin;
	
	private Integer addCoin;
	
	private Integer result;
	
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


	public static String table() {
		return "usr_game_log";
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getAddCoin() {
		return addCoin;
	}

	public void setAddCoin(Integer addCoin) {
		this.addCoin = addCoin;
	}
	
	
}
