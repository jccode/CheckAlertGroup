package com.hgst.checkalertgroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public <T> T executeQuery(String sql, QueryProcesser<T> queryProcesser) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		T t = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			t = queryProcesser.process(stmt, rs);
			return t;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, rs);
		}
		return t;
	}
	
	/**
	 * execute update
	 * 
	 * @param sql
	 * @return int -1:exception, others are normal
	 */
	public int executeUpdate(String sql) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int ret = -1;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			ret = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, rs);
		}
		return ret;
	}
	
	public void close(Connection conn, Statement stmt, ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
