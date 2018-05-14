package com.andy.gomoku.game;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;

import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrUser;
import com.andy.gomoku.utils.GoConstant;
import com.google.common.collect.Maps;

public class GameUser implements Serializable{

	private static final long serialVersionUID = 1L;

	private int status;
	private UsrUser user;
	private UsrGameInfo gameInfo;
	private Long id;
	private Room room;
	private GomokuGame game;
	
	private long startMatch;
	private long lastMov;
	private long allMovTime;
	private long startPeace;
	
	public GameUser() {
	}
	
	public GameUser(UsrUser user) {
		this.setUser(user);
		this.id = user.getId();
		
		if(id >= GoConstant.ROBOT_ID_START){
			user.setRole("1");
			this.gameInfo = new UsrGameInfo(user.getId()+1);
			gameInfo.setUid(user.getId());
			gameInfo.setCoin(RandomUtils.nextLong(100, 1000));
			gameInfo.addCoin(0);
			gameInfo.setLoseCount(RandomUtils.nextInt(100, 1000));
			gameInfo.setWinCount(RandomUtils.nextInt(100, 1000));
		}
		
	}
	
	public String getUserName() {
		return user.getUserName();
	}

	public String getNickName() {
		return user.getNickName();
	}

	public UsrUser getUser() {
		return user;
	}

	public void setUser(UsrUser user) {
		this.user = user;
	}

	public UsrGameInfo getGameInfo() {
		return gameInfo;
	}

	public void setGameInfo(UsrGameInfo gameInfo) {
		this.gameInfo = gameInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRoom(Room room) {
		this.room = room;
		status = GoConstant.USER_STATUS_0;
	}

	public Room getRoom() {
		return room;
	}

	public Map<String, Object> getUserInfo(boolean isSelf) {
		Map<String, Object> resu = Maps.newHashMap();
		resu.put("userId", id);
		resu.put("nickName", getNickName());
		resu.put("userName", getUserName());
		resu.put("icon", user.getIcon());
		if(isSelf){
			resu.put("cids", user.getTheme());
		}
		resu.put("cid", gameInfo.getCid());
		resu.put("coin", gameInfo.getCoin());
		resu.put("title", gameInfo.getTitle());
		return resu;
	}

	public void toggleReady(Integer chid) {
		if(status == GoConstant.USER_STATUS_0){
			status = GoConstant.USER_STATUS_1;
			if(chid != null){
				this.gameInfo.setCid(chid);
			}
		}else{
			status = GoConstant.USER_STATUS_0;
		}
	}
	
	public boolean isReady(){
		return status == GoConstant.USER_STATUS_1;
	}
	
	
	public void match(boolean isMatch){
		if(isMatch){
			status = GoConstant.USER_STATUS_2;
			startMatch = System.currentTimeMillis();
		}else{
			status = GoConstant.USER_STATUS_0;
		}
	}
	public boolean isMatching(){
		return status == GoConstant.USER_STATUS_2;
	}
	public boolean isMatchOutTime(int timeOut){
		return System.currentTimeMillis() - startMatch >= timeOut;
	}

	public GomokuGame getGame() {
		return game;
	}

	public void setGame(GomokuGame game) {
		this.game = game;
		lastMov = System.currentTimeMillis();
	}

	public boolean isRobo() {
		return "1".equals(user.getRole());
	}

	public void gameOver() {
		
		this.game = null;
		this.status = GoConstant.USER_STATUS_0;
		lastMov = 0;
		allMovTime = 0;
		
		startPeace = 0;
		
	}

	public void move(Long lastM) {
		lastMov = System.currentTimeMillis();
		allMovTime += lastMov - lastM;
	}
	public Long getLastMov() {
		return lastMov;
	}

	public Long getAllMovTime() {
		return allMovTime;
	}

	public void peace() {
		startPeace = System.currentTimeMillis();
	}
	public boolean isOutPeace(){
//		return System.currentTimeMillis() - startPeace > 10*1000;
		return startPeace == 0;
	}

	public void rejuct() {
		startPeace = 0;
	}
	
	public Integer getCid(){
		return gameInfo.getCid();
	}
}
