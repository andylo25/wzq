package com.andy.gomoku.dao;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

import com.andy.gomoku.entity.BaseEntity;
import com.andy.gomoku.entity.UsrGameInfo;
import com.andy.gomoku.entity.UsrGameLog;
import com.andy.gomoku.utils.JsonUtils;
import com.google.common.collect.Lists;

/**
 * 批量更新数据库
 * @author Administrator
 *
 */
public class DbBatch{

	static Logger logger = Logger.getLogger(DbBatch.class);
	
	private static List<DbBatch> dbBatchs = Lists.newArrayList();
	
	private List<UsrGameLog> modelSaveList = Lists.newCopyOnWriteArrayList();
	private List<UsrGameInfo> modelUpdateList = Lists.newCopyOnWriteArrayList();
	
	public static void upUsrUser(UsrGameInfo m){
		DbBatch batch = dbBatchs.get(Math.abs(m.hashCode())%dbBatchs.size());
		if(batch.thread.isAlive()){
			if(!batch.modelUpdateList.contains(m)){
				batch.modelUpdateList.add(m);
			}
		}else{
			batch.stop();
			batch.start();
		}
	}
	
	public static void sv(UsrGameLog m){
		DbBatch batch = dbBatchs.get(Math.abs(m.hashCode())%dbBatchs.size());
		if(batch.thread.isAlive()){
			batch.modelSaveList.add(m);
		}else{
			batch.stop();
			batch.start();
		}
	}
	
	private DbBatch(int i) {start();this.index = i;}
	
	void saveBatch(){
		if(!modelSaveList.isEmpty()){
			List<UsrGameLog> modelListT = Lists.newArrayList();
			synchronized (modelSaveList) {
				modelListT.addAll(modelSaveList);
				modelSaveList.clear();
			}
			try {
				DaoUtils.batchSave(modelListT);
			} catch (Exception e) {
				logger.error("",e);
				saveException(modelListT,"saving");
			}
			logger.error("保存数据："+modelListT.size());
		}
		
	}
	
	void updateBatch(){
		if(!modelUpdateList.isEmpty()){
			List<UsrGameInfo> modelListT = Lists.newArrayList();
			synchronized (modelUpdateList) {
				modelListT.addAll(modelUpdateList);
				modelUpdateList.clear();
			}
			try {
				DaoUtils.updateBatch("coin,titleSort,winCount,loseCount,title",modelListT);
			} catch (Exception e) {
				logger.error("",e);
				saveException(modelListT,"updating");
			}
			logger.error("更新数据："+modelListT.size());
		}
	}
	
	private void saveException(List<? extends BaseEntity> modelListT, String path) {
		if(modelListT.isEmpty())return;
		StringBuilder sb = new StringBuilder();
		for(BaseEntity mod:modelListT){
			sb.append(JsonUtils.object2JsonString(mod)).append("|@|");
		}
		try {
			FileUtils.writeStringToFile(new File("backdata/"+path+"/"+DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")), sb.toString());
		} catch (IOException e1) {
			logger.error("没有数据文件夹："+sb.toString(),e1);
		}
	}

	private int index;
	boolean run;
	private Thread thread;
	private boolean start() {
		run = true;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(run){
					try {
						saveBatch();
						updateBatch();
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(),e);
					}
				}
			}
		});
		thread.setName("DbBatch"+index);
		thread.setDaemon(true);
		thread.start();
		return true;
	}

	public boolean stop() {
		run = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		saveException(modelSaveList,"saving");
		saveException(modelUpdateList,"updating");
		thread = null;
		return true;
	}
	
	public static void stopBatch(){
		logger.error("开始停止数据落地线程");
		for(DbBatch dbBatch:dbBatchs){
			dbBatch.stop();
		}
	}
	
	/**
	 * 启动线程
	 * @param num
	 */
	public static void start(int num) {
		for(int i=0;i<num;i++){
			dbBatchs.add(new DbBatch(i));
		}
	}
	
	
	
	
}
