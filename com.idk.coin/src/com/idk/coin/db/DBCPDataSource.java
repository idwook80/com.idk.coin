package com.idk.coin.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class DBCPDataSource {
	 private BasicDataSource ds;
	    
	    public Connection getConnection() throws SQLException {
	        return ds.getConnection();
	    }
	    public DBCPDataSource(String url, String userid, String userpw){ 
	    	ds = new BasicDataSource();
	    	ds.setUrl(url);
	        ds.setUsername(userid);
	        ds.setPassword(userpw);
	        ds.setMinIdle(5);
	        ds.setMaxIdle(10);
	        ds.setMaxOpenPreparedStatements(100);
	    }
	   
}
