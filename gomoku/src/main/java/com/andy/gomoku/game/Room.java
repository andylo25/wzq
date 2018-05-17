package com.andy.gomoku.game;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomUtils;

import com.google.common.collect.Lists;

public class Room implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 计数器
	 */
	public static AtomicInteger counter = new AtomicInteger();
	
	private int id = counter.incrementAndGet();
	
	private List<GameUser> users = Lists.newCopyOnWriteArrayList();
	
	private int firstIndex = -1;
	
	private GomokuGame game ;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<GameUser> getUsers() {
		return users;
	}

	public void setUsers(List<GameUser> users) {
		this.users = users;
	}

	public void addUser(GameUser user) {
		this.users.add(user);
		user.setRoom(this);
	}
	
	public void logout(GameUser user){
		this.game = null;
		this.users.remove(user);
		this.firstIndex = -1;
		GameUser other = this.getOther(user);
		if(other != null && other.isReady()){
			other.toggleReady(null);
		}
		user.setRoom(null);
		user.setGame(null);
	}

	public GameUser ready(GameUser gameUser) {
		boolean haveRob = false;
		if(users.size() < 2) return null;
		for(GameUser user:users){
			if(!user.isReady())return null;
			if(user.isRobo()){
				haveRob = true;
			}
		}
		if(firstIndex == -1){
			firstIndex = RandomUtils.nextInt(0, users.size());
		}
		// 创建游戏
		game = new GomokuGame(haveRob,firstIndex);
		for(GameUser user:users){
			user.setGame(game);
		}
		
		return users.get(firstIndex);
	}

	public int getIndx(GameUser gameUser) {
		if(users.get(0) == gameUser){
			return 0;
		}
		return 1;
	}

	public GameUser getOther(GameUser user) {
		if(users.get(0) == user){
			if(users.size() > 1){
				return users.get(1);
			}
			return null;
		}
		return users.get(0);
	}
	
	public void gameOver(){
		this.firstIndex = 1-firstIndex;
		this.game = null;
	}
	
}
