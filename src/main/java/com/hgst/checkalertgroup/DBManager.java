package com.hgst.checkalertgroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

	private String url;
	private String driver;
	private String user;
	private String password;
	private Connection conn;
	
	
	public DBManager(String url, String driver, String user, String password) throws ClassNotFoundException {
		this.url = url;
		this.driver = driver;
		this.user = user;
		this.password = password;
		init();
	}

	private void init() throws ClassNotFoundException {
		Class.forName(driver);
	}
	
	public Connection getConnection() throws SQLException {
		if(conn == null) {
			conn = DriverManager.getConnection(url, user, password);
		}
		return conn;
	}
}
