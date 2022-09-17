package com.idk.coin.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class DBCPDataSource {
	 private static BasicDataSource ds = new BasicDataSource();
	    
	    static {
	        ds.setUrl("jdbc:mysql://localhost:3306/upbit");
	        ds.setUsername("root");
	        ds.setPassword("80idwook");
	        ds.setMinIdle(5);
	        ds.setMaxIdle(10);
	        ds.setMaxOpenPreparedStatements(100);
	    }
	    
	    public static Connection getConnection() throws SQLException {
	        return ds.getConnection();
	    }
	    String cs = "jdbc:mysql://localhost:3306/upbit";
        String user = "root";
        String password = "80idwook";
	    public DBCPDataSource(){ 
	    	
	    }
	   
}
