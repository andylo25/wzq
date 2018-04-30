package com.andy.gomoku.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;

import com.andy.gomoku.base.PageVO;
import com.andy.gomoku.entity.BaseEntity;
import com.andy.gomoku.exception.GoSeviceException;
import com.andy.gomoku.utils.EntityUtils;
import com.andy.gomoku.utils.ReflectUtil;
import com.andy.gomoku.utils.SpringContextHolder;
import com.google.common.collect.Lists;

public class DaoUtils {

	static DataSource dataSource = SpringContextHolder.getBean(DataSource.class);
	
	static GmkBeanProcessor convert = new GmkBeanProcessor();
	static BasicRowProcessor rowProcessor = new BasicRowProcessor(convert);

	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	
	private static final String keywordEscape = "`";

	/**
	 * 根据ID查询实体
	 * 
	 * @param id
	 * @param clasz
	 * @return
	 */
	public static <T> T get(Serializable id, Class<T> clasz) {
		QueryRunner run = new QueryRunner(dataSource);
		ResultSetHandler<T> h = new BeanHandler<T>(clasz,rowProcessor);
		try {
			T p = run.query("SELECT * FROM " + toTable(clasz.getSimpleName()) + " WHERE id=?", h, id);
			return p;
		} catch (SQLException e) {
			throw new GoSeviceException(e);
		}
	}

	public static String toTable(String entity) {
		String[] strs = StringUtils.splitByCharacterTypeCamelCase(entity);
		return StringUtils.join(strs, "_");
	}

	/**
	 * 根据条件查询列表
	 * 
	 * @param clasz
	 * @param conds
	 * @return
	 */
	public static <T> List<T> getList(Class<T> clasz, Where... conds) {
		return getList(clasz, null, null, null, null, conds);
	}
	public static <T> T getOne(Class<T> clasz, Where... conds) {
		List<T> list = getList(clasz, null, null, null, null, conds);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public static <T> List<T> getList(Class<T> clasz, String sort, String limit, Where... conds) {
		return getList(clasz, sort, limit, null, null, conds);
	}
	public static <T> T getOne(Class<T> clasz, String sort, String limit, Where... conds) {
		List<T> list = getList(clasz, sort, limit, null, null, conds);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public static List<Map<String, Object>> getListMap(String table,String field,Integer limit, Where... conds) {
		QueryRunner run = new QueryRunner(dataSource);
		Object[] wh = buildWhere(conds);
		Object[] vas = (Object[]) wh[1];
		if(field == null){
			field = "*";
		}
		String limits = "";
		if(limit != null){
			limits = " LIMIT 0," + limit;
		}
		try {
			List<Map<String, Object>> list = run.query("SELECT " + field + " FROM " + table + wh[0] + limits, new MapListHandler(),vas);
			return list;
		} catch (SQLException e) {
			throw new GoSeviceException(e);
		}
	}
	
	public static PageVO getPageForMap(String table,String field,int page,Integer epage, Where... conds) {
		QueryRunner run = new QueryRunner(dataSource);
		PageVO pageVO = new PageVO();
		pageVO.setPage(page);
		pageVO.setEpage(epage);
		Object[] wh = buildWhere(conds);
		Object[] vas = (Object[]) wh[1];
		if(field == null){
			field = "*";
		}
		String limits = " LIMIT "+(page-1) * epage+","+(page * epage);
		try {
			List<Map<String, Object>> list = run.query("SELECT " + field + " FROM " + table + wh[0] + limits, new MapListHandler(), vas);
			Map<String, Object> count = run.query("SELECT count(1) as count FROM " + table + wh[0], new MapHandler(), vas);
			pageVO.setItems(list);
			pageVO.setTotal_items(MapUtils.getInteger(count, "count"));
			return pageVO;
		} catch (SQLException e) {
			throw new GoSeviceException(e);
		}
	}
	
	private static Object[] buildWhere(Where... conds) {
		String where = "";
		StringBuilder sb = new StringBuilder();
		Object[] vas = null;
		if (conds.length > 0) {
			vas = new Object[conds.length];
			int c =0;
			for (int i = 0; i < conds.length; i++) {
				Where nv = conds[i];
				sb.append(" AND ").append(keywordEscape).append(nv.getName()).append(keywordEscape).append(" ").append(nv.getCondition());
				if(nv.getCondition().equals(Condition.IN) || nv.getCondition().equals(Condition.NOT_IN)) {
					sb.append(" (").append(nv.getValue()).append(") ");
				}else {
					c++;
					sb.append(" ? ");
					vas[i] = nv.getValue();
				}
				vas[i] = nv.getValue();
			}
			vas = Arrays.copyOf(vas, c);
			where = " WHERE 1=1" + sb.toString();
		}
		return new Object[]{where,vas};
	}
	
	public static Map getOne(String field, String group, Where... conds) {
		List<Map> list = getList(Map.class, null, null, field, group, conds);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public static <T> List<T> getListSql(Class<T> clasz, String sql, Object... conds) {
		QueryRunner run = new QueryRunner(dataSource);
		ResultSetHandler<List<T>> h = new BeanListHandler<T>(clasz);
		try {
			List<T> list = run.query(sql, h, conds);
			return list;
		} catch (SQLException e) {
			throw new GoSeviceException(e);
		}
	}
	
	public static List<Map<String, Object>> getListSql(String sql, Object... conds) {
		QueryRunner run = new QueryRunner(dataSource);
		try {
			List<Map<String, Object>> list = run.query(sql, new MapListHandler(), conds);
			return list;
		} catch (SQLException e) {
			throw new GoSeviceException(e);
		}
	}

	public static <T> T getOneSql(Class<T> clasz, String sql, Object... conds) {
		List<T> list = getListSql(clasz, sql, conds);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据条件查询排序列表
	 * 
	 * @param clasz
	 * @param group
	 * @param sort
	 * @param limit
	 * @param conds
	 * @return
	 */
	public static <T> List<T> getList(Class<T> clasz, String sort, String limit, String field, String group,
			Where... conds) {
		QueryRunner run = new QueryRunner(dataSource);
		
		ResultSetHandler<List<T>> h = new BeanListHandler<T>(clasz,rowProcessor);
		Object[] wh = buildWhere(conds);
		Object[] vas = (Object[]) wh[1];
		if(field == null){
			field = "*";
		}
		String limits = "";
		if(limit != null){
			if(limit.indexOf(",") > 0){
				limits = " LIMIT " + limit;
			}else{
				limits = " LIMIT 0," + limit;
			}
		}
		try {
			List<T> list= run.query(
						"SELECT " + field + " FROM " + toTable(clasz.getSimpleName()) + wh[0]
								+ (group == null ? "" : " GROUP BY " + group)
								+ (sort == null ? "" : " ORDER BY " + sort) + limits, h, vas);
			return list;
		} catch (SQLException e) {
			throw new GoSeviceException(e);
		}
	}

	/**
	 * 插入实体
	 * 
	 * @param entity
	 * @return
	 */
	public static int insert(Object entity) {
		QueryRunner run = new QueryRunner(dataSource);
		try {
			List<NameValue> fields = null;
			Serializable id = null;
			if (entity instanceof BaseEntity) {
				fields = EntityUtils.getNameValues((BaseEntity) entity, false,true);
			} else if (entity instanceof Map) {
				fields = Lists.newArrayList();
				for (Entry<String, Object> fc : ((Map<String, Object>) entity).entrySet()) {
					Object value = fc.getValue();
					if (value != null) {
						String column = toTable(fc.getKey());
						fields.add(new NameValue(column, value));
					}
				}
			} else {
				return 0;
			}
			String table = toTable(entity.getClass().getSimpleName());
			StringBuilder sb = new StringBuilder();
			Object[] vas = new Object[fields.size()];
			for (int i = 0; i < fields.size(); i++) {
				NameValue nv = fields.get(i);
				sb.append(",").append(keywordEscape).append(nv.getName()).append(keywordEscape);
				vas[i] = nv.getValue();
			}
			Object[] insert = run.insert("INSERT INTO " + table + " (" + sb.substring(1) + ") VALUES ("
					+ StringUtils.repeat("?", ",", fields.size()) + ")",new ArrayHandler(), vas);
			Long idd = ((Long) insert[0]);
			if (entity instanceof BaseEntity) {
				((BaseEntity) entity).setId(idd);
			}else{
				((Map<String, Object>) entity).put("id", idd);
			}
			return 1;
		} catch (SQLException sqle) {
			throw new GoSeviceException(sqle);
		}
	}

	/**
	 * 更新实体
	 * 
	 * @param entity
	 * @return
	 */
	public static int update(Object entity) {
		QueryRunner run = new QueryRunner(dataSource);
		List<NameValue> fields = null;
		Serializable id = null;
		if (entity instanceof BaseEntity) {
			fields = EntityUtils.getNameValues((BaseEntity) entity, false,false);
			id = ((BaseEntity) entity).getId();
		} else if (entity instanceof Map) {
			id = (Serializable) ((Map) entity).get("id");
			fields = Lists.newArrayList();
			for (Entry<String, Object> fc : ((Map<String, Object>) entity).entrySet()) {
				if ("id".equals(fc.getKey()))
					continue;
				Object value = fc.getValue();
				if (value != null) {
					String column = toTable(fc.getKey());
					fields.add(new NameValue(column, value));
				}
			}
		} else {
			return 0;
		}
		String table = toTable(entity.getClass().getSimpleName());
		StringBuilder sb = new StringBuilder();
		Object[] vas = new Object[fields.size() + 1];
		for (int i = 0; i < fields.size(); i++) {
			NameValue nv = fields.get(i);
			sb.append(",").append(keywordEscape).append(nv.getName()).append(keywordEscape).append("=?");
			vas[i] = nv.getValue();
		}
		vas[fields.size()] = id;
		try {
			int updates = run.update("UPDATE " + table + " SET " + sb.substring(1) + " WHERE id=?", vas);
			return updates;
		} catch (SQLException sqle) {
			throw new GoSeviceException(sqle);
		}
	}
	
	
	/**
	 * 批量更新
	 * @param entity
	 * @return
	 */
	public static int[] updateBatch(String field, List<BaseEntity> entitys) {
		if(field == null || entitys == null || entitys.isEmpty()) return null;
		QueryRunner run = new QueryRunner(dataSource);
		String[] fields = StringUtils.split(field,",");
		Object[][] params = new Object[entitys.size()][fields.length+1];
		StringBuilder sql = new StringBuilder("UPDATE ");
		String table = toTable(entitys.get(0).getClass().getSimpleName());
		sql.append(table).append(" SET ");
		for(String fie:fields){
			sql.append(",").append(keywordEscape).append(fie).append(keywordEscape).append("=?");
		}
		sql.append(" WHERE id=?");
		
		for(int i=0;i<entitys.size();i++){
			for (int j = 0; j < fields.length; j++) {
				params[i][j] = ReflectUtil.getFieldValue(entitys.get(i), fields[j]);
			}
			params[i][fields.length] = entitys.get(i).getId();
		}
		
		try {
			int[] updates = run.batch(sql.substring(1), params);
			return updates;
		} catch (SQLException sqle) {
			throw new GoSeviceException(sqle);
		}
	}

	/**
	 * 更新数据
	 * 
	 * @param sql
	 * @param conds
	 * @return
	 */
	public static int update(String sql, Object... conds) {
		QueryRunner run = new QueryRunner(dataSource);
		try {
			int updates = run.update(sql, conds);
			return updates;
		} catch (SQLException sqle) {
			throw new GoSeviceException(sqle);
		}
	}

	/**
	 * @Method: startTransaction
	 * @Description: 开启事务
	 */
	public static void startTransaction() {
		try {
			Connection conn = threadLocal.get();
			if (conn == null) {
				conn = dataSource.getConnection();
				threadLocal.set(conn);
			}
			// 开启事务
			conn.setAutoCommit(false);
		} catch (Exception e) {
			throw new GoSeviceException(e);
		}
	}

	/**
	 * @Method: commit
	 * @Description:提交事务
	 */
	public static void commit() {
		try {
			// 从当前线程中获取Connection
			Connection conn = threadLocal.get();
			if (conn != null) {
				// 提交事务
				conn.commit();
			}
		} catch (Exception e) {
			throw new GoSeviceException(e);
		}
	}

	/**
	 * @Method: close
	 * @Description:关闭数据库连接(注意，并不是真的关闭，而是把连接还给数据库连接池)
	 * @Anthor:
	 *
	 */
	public static void close() {
		try {
			// 从当前线程中获取Connection
			Connection conn = threadLocal.get();
			if (conn != null) {
				conn.close();
				// 解除当前线程上绑定conn
				threadLocal.remove();
			}
		} catch (Exception e) {
			throw new GoSeviceException(e);
		}
	}

}
