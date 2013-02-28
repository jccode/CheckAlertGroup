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
//		List<String> groupNames = getAllGroupNames();
		getAllFoods();
	}
	
	// 手动建立连接
	List<String> getAllGroupNames() {
		List<String> groupNames = new ArrayList<String>();
		
		String sql = "select count(*) from 1000funs.food";
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
			System.out.println("1000funs.food的记录总数为: " + ret);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(conn, stmt, rs);
		}
		return groupNames;
	}
	
	// 使用模板
	List<String> getAllFoods() {
		String sql = "select * from 1000funs.food";
		List<String> foodNames = dbm.executeQuery(sql, new QueryProcesser<List<String>>() {

			@Override
			public List<String> process(Statement stmt, ResultSet rs) throws SQLException {
				List<String> foodNames = new ArrayList<String>();
				while(rs.next()) {
					foodNames.add(rs.getString("food_name"));
				}
				
				//这里可以使用stmt对象再做进一步的查询处理
				String sql2 = "select * FROM 1000funs.package_group";
				rs = stmt.executeQuery(sql2);
				while(rs.next()) {
					foodNames.add(rs.getString("group_name"));
				}
				
				return foodNames;
			}
			
		});
		System.out.println(foodNames);
		return foodNames;
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
