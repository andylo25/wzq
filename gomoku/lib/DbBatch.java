package com.bmm.platform.common.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.bmm.platform.common.constant.SGConstants;
import com.bmm.platform.common.dao.BaseDao;
import com.bmm.platform.common.dao.ScoreDao;
import com.bmm.platform.common.dao.UserDao;
import com.bmm.platform.common.entity.BaseEntity;
import com.bmm.platform.common.entity.User;
import com.bmm.platform.common.utils.FileUtil;
import com.bmm.platform.common.utils.SpringBeanUtil;
import com.bmm.platform.common.utils.UtilDatetime;
import com.bmm.platform.sserver.GameServer;
import com.bmm.platform.system.service.TaxService;

/**
 * 批量更新数据库
 * @author Administrator
 *
 */
public class DbBatch {

	private static Logger logger = Logger.getLogger(DbBatch.class);
	
	private static List<DbBatch> dbBatchs = new ArrayList<DbBatch>();
	
	private List<BaseEntity> modelSaveList = new CopyOnWriteArrayList<BaseEntity>();
	private List<BaseEntity> modelUpdateList = new CopyOnWriteArrayList<BaseEntity>();
	private ScoreDao scoreDao;
	private UserDao userDao;
	private TaxService taxService;
	
	private ScoreDao getScoreDao(){
		if(scoreDao == null){
			scoreDao = SpringBeanUtil.getBean(ScoreDao.class);
		}
		return scoreDao;
	}
	private UserDao getUserDao(){
		if(userDao == null){
			userDao = SpringBeanUtil.getBean(UserDao.class);
		}
		return userDao;
	}
	private TaxService getTaxService(){
		if(taxService == null){
			taxService = SpringBeanUtil.getBean(TaxService.class);
		}
		return taxService;
	}
	
	public static void addScores(BaseDao baseDao,BaseEntity m){
//		EntityItem item = new EntityItem(null,m);
		DbBatch batch = dbBatchs.get(m.getId()%dbBatchs.size());
		if(batch.thread.isAlive()){
			synchronized (batch.modelSaveList) {
				batch.modelSaveList.add(m);
			}
		}else{
			batch.stop();
			batch.start();
		}
	}
	
	public static void updateUsers(BaseDao baseDao,BaseEntity m){
//		EntityItem item = new EntityItem(null,m);
		int userId = m.getId()>0?m.getId():1;
		DbBatch batch = dbBatchs.get(userId%dbBatchs.size());
		if(batch.thread.isAlive()){
			synchronized (batch.modelUpdateList) {
				if(!batch.modelUpdateList.contains(m)){
					batch.modelUpdateList.add(m);
				}
			}
		}else{
			batch.stop();
			batch.start();
		}
	}
	
	private DbBatch() {}
	
	private void saveBatchScores(){
		if(!modelSaveList.isEmpty()){
			List<BaseEntity> modelListT = new ArrayList<BaseEntity>();
			synchronized (modelSaveList) {
				modelListT.addAll(modelSaveList);
				modelSaveList.clear();
			}
//			for(EntityItem en:modelListT){
//				en.baseDao.add(en.baseEntity);
//			}
			try {
				getScoreDao().insertBatch(modelListT);
			} catch (Exception e) {
				logger.error("",e);
				saveScores(modelListT);
			}
			logger.error("保存数据："+modelListT.size());
		}
		
	}
	private void saveScores(List<BaseEntity> modelListT) {
		StringBuilder sb = new StringBuilder();
		for(BaseEntity sc:modelListT){
			sb.append(sc).append("|");
		}
		try {
			FileUtil.saveToFile("/backdata/Scores/"+UtilDatetime.getDatetimeFromText(new Date()), sb.toString().getBytes());
		} catch (IOException e1) {
			logger.error("没有数据文件夹："+sb.toString(),e1);
		}
	}
	
	private void updateBatchUsers(){
		if(!modelUpdateList.isEmpty()){
			List<BaseEntity> modelListT = new ArrayList<BaseEntity>();
			synchronized (modelUpdateList) {
				modelListT.addAll(modelUpdateList);
				modelUpdateList.clear();
			}
			try {
				getUserDao().batchUpdate(modelListT);
			} catch (Exception e) {
				logger.error("",e);
				saveUserData(modelListT);
			}
			logger.error("更新数据："+modelListT.size());
		}
	}
	private void saveUserData(List<BaseEntity> modelListT) {
		StringBuilder sb = new StringBuilder();
		for(BaseEntity user:modelListT){
			if(user instanceof User){
				User userT = (User)user;
				sb.append(userT.getId()).append(":").append(userT.getCoin()).append("|");
			}
		}
		try {
			FileUtil.saveToFile("/backdata/Users/"+UtilDatetime.getDatetimeFromText(new Date()), sb.toString().getBytes());
		} catch (IOException e1) {
			logger.error("没有数据文件夹："+sb.toString(),e1);
		}
	}

	private boolean run;
	private int errorTimes = 0;//错误次数
	Thread thread = null;
	public boolean start() {
		run = true;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(run){
//					logger.error("执行数据落地线程");
//					Connection conn = getConnection();
//					if(conn == null)continue;
//					try {
//						conn.setAutoCommit(false);
//					} catch (SQLException e1) {
//						logger.error(e1.getMessage(),e1);
//						errorTimes++;
//					}
					try {
						saveBatchScores();
						updateBatchUsers();
						getTaxService().updateSysTax();
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					} finally{
//						try {
//							conn.commit();
//							conn.setAutoCommit(true);
//						} catch (SQLException e) {
//							logger.error(e.getMessage(),e);
//							errorTimes++;
//						}
//						try {
//							conn.close();
//						} catch (SQLException e) {
//							logger.error(e.getMessage(),e);
//						}
					}
					try {
						if(errorTimes > 500){
							GameServer.SERVER_STATE = SGConstants.SERVER_STATE_ERROR;//服务器异常
							logger.error("服务器操作数据异常，请检查重启");
							GameServer.stop();
						}
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(),e);
					}
				}
			}
		});
		thread.setName("DbBatch");
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
		saveScores(modelSaveList);
		saveUserData(modelUpdateList);
		thread = null;
		return true;
	}
	
	public static void stopBatch(){
		logger.error("开始停止数据落地线程");
		for(DbBatch dbBatch:dbBatchs){
			dbBatch.stop();
		}
	}
	
	public static class EntityItem {
		public EntityItem(BaseDao baseDao, BaseEntity m) {
//			this.baseDao = baseDao;
			this.baseEntity = m;
		}
//		BaseDao baseDao;
		BaseEntity baseEntity;
		
		public int hashCode(){
			if(baseEntity == null){
				return super.hashCode();
			}
			return baseEntity.hashCode();
		}
		
		public boolean equals(Object item){
			if(baseEntity == null || item == null || !(item instanceof EntityItem)){
				return false;
			}
			EntityItem it = (EntityItem) item;
			return baseEntity.equals(it.baseEntity);
		}
	}

	/**
	 * 启动线程
	 * @param num
	 */
	public static void start(int num) {
		for(int i=0;i<num;i++){
			DbBatch dbBatch = new DbBatch();
			dbBatch.start();
			dbBatchs.add(dbBatch);
		}
	}
	
	
}
