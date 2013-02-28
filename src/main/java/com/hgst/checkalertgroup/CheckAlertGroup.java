package com.hgst.checkalertgroup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CheckAlertGroup implements Runnable {

	private String url;
	private String driver;
	private String user;
	private String password;
	private DBManager dbm;
	
	public CheckAlertGroup(String url, String driver, String user, String password) throws ClassNotFoundException {
		super();
		this.url = url;
		this.driver = driver;
		this.user = user;
		this.password = password;
		dbm = new DBManager(url, driver, user, password);
	}

	@Override
	public void run() {
		System.out.println("doCheck!");
		System.out.println("------------ >>>>>>");
		test();
		List<String> groupNames = getAllGroupNames();
	}
	
	List<String> getAllGroupNames() {
		List<String> groupNames = new ArrayList<String>();
		
		String sql = "select count(*) from 1000funs.shop";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int ret = -100;
		try {
			conn = dbm.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ret = rs.getInt(1);
			}
			System.out.println("1000funs.shop的记录总数为: " + ret);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
		return groupNames;
	}
	
	void test() {
		System.out.println("----------------");
		System.out.println("url:\t" + url);
		System.out.println("user:\t" + user);
		System.out.println("password:\t" + password);
		System.out.println("driver:\t" + driver);
		System.out.println("----------------");
	}
}
