package com.idk.coin.bybit.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.idk.coin.CoinConfig;
import com.idk.coin.db.DBCPDataSource;


public class BybitDBManager {
	public static final Logger LOG = Logger.getLogger(BybitDBManager.class);
	
	public static BybitDBManager DBM;
	
	public static BybitDBManager getInstance(){
		if(DBM == null){
			DBM = new BybitDBManager();
		}
		return DBM;
	}
	public BybitDBManager(){
		if(isLoaded)
			try {
				load();
			} catch (NamingException e) {
				e.printStackTrace();
				isLoaded =false;
			}
	}
	public static boolean isLoaded = false;
	public static DBCPDataSource pool = null;
	public static String DB_NAME = "bybit";
	public static String url	 = "jdbc:mysql://localhost:3306/bybit";
    public static String userid = "root";
    public static String userpw = "80idwook";
	public static void load() throws NamingException{
			    isLoaded = true;
			    String ip = System.getProperty("BYBIT_DB_IP");
			    url = url.replace("localhost", ip);
			    pool = new DBCPDataSource(url, userid, userpw);
			   if (pool == null)
			      throw new NamingException("Unknown DataSource 'jdbc/"+DB_NAME+"'");
	 }
	
	public static List selectList(String sql) throws Exception{
		 return queryForList(sql);
	}
	public static Object[][] select(String sql) throws Exception{
		return executeQuery(sql);
	}
	public static int update(String sql) throws Exception{
		return executeUpdate(sql);
	}
	public static int insert(String sql) throws Exception{
		return executeUpdate(sql);
	}
	public static Connection getConnection(){
			try {
				if(!isLoaded) load();
				Connection conn = null;
				conn = pool.getConnection();
				return conn;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		
		
	}
	public static Object[][] executeQuery(String sql) throws Exception{
		if(!isLoaded) load();
		Connection conn = null;
		Statement stmt = null;
		Object[][] result = null;
		try {
			//DB�� ����
			conn = pool.getConnection();
			// Statement ��ü ����
			 stmt = conn.createStatement();
			// SQL����
			LOG.info(sql);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			// �ݺ�ó��
			int row =0;
			if(rs.last()){
				row = rs.getRow();
				rs.beforeFirst();
			}
			int column = meta.getColumnCount();
			
			LOG.debug("Result R:"+row+",C:"+column);
			
			if(column == 0) return null;
			if(row == 0) {
				result = new Object[1][1];
				result[0][0] = 0;
				return result;
			}else {
				result = new Object[row][column];
			}
			
			int count = 0;
			
			while(rs.next()){
				for(int i=0; i<column; i++){
					result[count][i] = (String)rs.getString(i+1);
				}
				count++;
			 }
			return result;
		}finally{
			try{
			conn.close();
			}catch(Exception e){}
		}
	}
	public static int executeUpdate(String sql) throws Exception{
		if(!isLoaded) load();
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = pool.getConnection();
			 stmt = conn.createStatement();
			 System.out.println(sql);
			return stmt.executeUpdate(sql);
		}finally{
			try{
			conn.close();
			}catch(Exception e){}
		} 
	}
	public static int insertAndIndex(String sql) throws Exception{
		if(!isLoaded) load();
		Connection conn = null;
		Statement stmt = null;
		
		LOG.info(sql);
		try {
			conn = pool.getConnection();
			 stmt = conn.createStatement();
			 int result = stmt.executeUpdate(sql);
			 if(result > 0) {
				 ResultSet rs  = stmt.executeQuery("SELECT LAST_INSERT_ID()");
				 if(rs.next()) result =  rs.getInt(1);
			 }
			 return result;
		}finally{
			try{
			conn.close();
			}catch(Exception e){}
		} 
	}
	/**
	 * 
	 * @param sql
	 * @return NULL or List
	 * @throws Exception
	 */
	public static List<HashMap> queryForList(String sql)throws Exception{
		return queryForList(sql,-1,-1);
	}
	/**
	 * 
	 * @param sql
	 * @param startIndex
	 * @param length
	 * @return NULL or List
	 * @throws Exception 
	 */
	public static List<HashMap> queryForList(String sql,int startIndex,int length)throws Exception{
		if(!isLoaded) load();
		Connection conn = null;
		Statement stmt = null;
		List<HashMap> list = null;
		try {
			conn = pool.getConnection();
			stmt = conn.createStatement();
		/*	if(startIndex >= 0 && length >0){
				sql += " LIMIT "+startIndex+","+length;
			}*/
			
			//System.out.println(sql);
			//LOG.info(sql);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			// �ݺ�ó��
			int row =0;
			if(rs.last()){
				row = rs.getRow();
				rs.beforeFirst();
			}
			int col = meta.getColumnCount();
			
			LOG.info("Result row:"+row+",col:"+col);
			
			/*if(row == 0 || col == 0) return null;*/
			if(col == 0) return null;
			String[] columnNames = new String[col];
			int[] columnTypes	= new int[col];
			String[] columnTypeNames = new String[col];
			for(int i=0; i<col; i++){
				columnNames[i] = meta.getColumnName(i+1);
				columnTypes[i] = meta.getColumnType(i+1);
				columnTypeNames[i] = meta.getColumnTypeName(i+1);
			}
			
			list = new ArrayList<HashMap>();
			if(row == 0) return list;
			int count = 0;
			while(rs.next()){
				HashMap resultMap = new HashMap();
				for(int i=0; i<col; i++){
					String colName = columnNames[i];
					int colType = columnTypes[i];
					String colTypeName = columnTypeNames[i];
					//91 data , 93 datetime
					String colData = null;
					/*if(colType == 91(date)) colData = rs.getDate(colName).toString();
					else colData = rs.getString(colName);*/
					colData = rs.getString(i+1);
					
					
					if(colData == null) colData = "";
					 //LOG.info(colName +":"+colData);
					resultMap.put(colName, colData);
				}
				list.add(resultMap);
			 }
		}finally{
			try{
			conn.close();
			}catch(Exception e){}
			return list;
		}
	}
	public HashMap	selectHashMap(String sql)throws Exception {
		return queryForObjectHashMap(sql);
	}
	public static Object queryForObject(String sql)throws Exception{
		if(sql.toUpperCase().indexOf("COUNT(*)") > -1){
			return queryCountForObject(sql);
		}else{
			return queryForObjectHashMap(sql);
		}
	}
	private static Integer queryCountForObject(String sql)throws Exception{
		HashMap resultMap = queryForObjectHashMap(sql);
		if(resultMap == null) return 0;
		Iterator it = resultMap.keySet().iterator();
		String countKey = null;
		while(it.hasNext()){
			String key = (String)it.next();
			if(key.startsWith("count")){
				countKey = key;
				break;
			}
		}
		int countValue = 0;
		if(countKey != null){
			String value = (String)resultMap.get(countKey);
			try{
				countValue = Integer.parseInt(value);
			}catch(Exception e){
			}
		}
		return countValue;
	}
	private static HashMap queryForObjectHashMap(String sql)throws Exception{
		if(!isLoaded) load();
		Connection conn = null;
		Statement stmt = null;
		HashMap resultMap = new HashMap();
		try {
			//LOG.info(sql);
			conn = pool.getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			// �ݺ�ó��
			int row =0;
			if(rs.last()){
				row = rs.getRow();
				rs.beforeFirst();
			}
			int col = meta.getColumnCount();
			
			//LOG.debug("Result row:"+row+",col:"+col);
			
			if(row == 0 || col == 0) return resultMap;
			String[] columnNames = new String[col];
			
			for(int i=0; i<meta.getColumnCount(); i++){
				columnNames[i] = meta.getColumnName(i+1);
			}
			if(rs.next()){
				for(int i=0; i<col; i++){
					resultMap.put(columnNames[i], (String)rs.getString(i+1));
				}
			 }
			
		}finally{
			try{
			conn.close();
			}catch(Exception e){}
			return resultMap;
		}
	}
	public static String[] getColumns(String sql) throws NamingException{
		if(!isLoaded) load();
		Connection conn = null;
		Statement stmt = null;
		String[] columnNames = null;
		try {
			//LOG.info(sql);
			conn = pool.getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int row =0;
			if(rs.last()){
				row = rs.getRow();
				rs.beforeFirst();
			}
			int col = meta.getColumnCount();
			
			//LOG.debug("Result row:"+row+",col:"+col);
			
			columnNames = new String[col];
			
			for(int i=0; i<meta.getColumnCount(); i++){
				columnNames[i] = meta.getColumnName(i+1);
			}
			 
			
		}finally{
			try{
			conn.close();
			}catch(Exception e){}
			return columnNames;
		}
	}
	public static String getType(int type){
		String value = "";
		switch(type){
			case Types.INTEGER 		: value = "INT"; 			break;
			case Types.VARCHAR 		: value = "VARCHAR"; 		break;
			case Types.DATE 		: value = "DATE"; 			break;
			case Types.TIMESTAMP 	: value = "DATETIME"; 		break;
			case Types.TINYINT 		: value = "TINYINT"; 		break;
			case Types.LONGVARCHAR 	: value = "TEXT";			break;
			case Types.TIME 		: value = "TIME";	 		break;
			default 				: value = String.valueOf(type); break;
		}
		return value;
	}
}
