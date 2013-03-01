package com.hgst.checkalertgroup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hgst.checkalertgroup.db.DBManager;
import com.hgst.checkalertgroup.db.QueryProcesser;
import com.hgst.checkalertgroup.io.CheckResultWriter;


public class CheckAlertGroup implements Runnable {

	private String url;
	private String driver;
	private String user;
	private String password;
	private DBManager dbm;
	
	private Env env;
	private CheckResultWriter writer;
	
	private List<String> groupNames;
	private List<String> withoutAreaRule = new ArrayList<String>();
	private List<String> areaSameResult = new ArrayList<String>() ;
	private List<String> areaDiffResult = new ArrayList<String>();
	
	public CheckAlertGroup(Env env, CheckResultWriter writer) throws ClassNotFoundException {
		this.env = env;
		this.writer = writer;
		this.url = this.env.get(Env.KEY_DB_URL);
		this.driver = this.env.get(Env.KEY_DB_DRIVER);
		this.user = this.env.get(Env.KEY_DB_USER);
		this.password = this.env.get(Env.KEY_DB_PASSWORD);
		dbm = new DBManager(url, driver, user, password);
	}

	@Override
	public void run() {
		connectionInfo();
		groupNames = getAllGroupNames();
		
		System.out.println("processing...");
		
		check(groupNames);
		writer.write(withoutAreaRule, areaSameResult, areaDiffResult);
		
		System.out.println("finish.");
	}
	
	List<String> getAllGroupNames() {
		String sql = "SELECT DISTINCT GROUP" + 
					"  FROM (SELECT OBJECTNAME, PARAMETERVALUE, LEFT(OBJECTNAME, LENGTH(OBJECTNAME) - 9) AS GROUP" +
					"          FROM RULEDB.RULEPARAMTABLE" +
					"          WHERE OBJECTNAME LIKE '%SeqInputGroupsOOC%' AND PARAMETERNAME = 'Name'" +
					"          ORDER BY OBJECTNAME) AS T";
		List<String> groupNames = dbm.executeQuery(sql, new QueryProcesser<List<String>>() {
			@Override
			public List<String> process(Statement stmt, ResultSet rs) throws SQLException {
				List<String> groupNames = new ArrayList<String>();
				while(rs.next()) {
					groupNames.add(rs.getString("GROUP"));
				}
				return groupNames;
			}
		});
		return groupNames;
	}
	
	void check(List<String> groupNames) {
		Connection conn = null; 
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dbm.getConnection();
			stmt = conn.createStatement();
			for(String groupName : groupNames) {
				
				// get parameter values
				String sql = getParameterValueSQL(groupName);
				List<String> paramValues = new ArrayList<String>();
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					paramValues.add(rs.getString("PARAMETERVALUE"));
				}
				
				// check if has area value
				if(areaRuleNotExist(paramValues)) {
					withoutAreaRule.add(groupName);
					continue;
				}
				
				// check if has the same result
				String checkSQL = getCheckSQL(paramValues);
				rs = stmt.executeQuery(checkSQL);
				while(rs.next()) {
					if(rs.getInt(1) == 1) { // same result whether the areaRule exist or not
						areaSameResult.add(groupName);
					} else { // different result
						areaDiffResult.add(groupName);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(conn, stmt, rs);
		}
	}
	
	private String getCheckSQL(List<String> rules) {
		String sqlMain = "SELECT COUNT(0) FROM ", 
				sqlFrom = "SELECT COUNT(0) AS PART FROM EVENTS.HMNY_ACT WHERE ";
		String allWhere = "", partWhere = "";
		for(String rule : rules) {
			allWhere += " AND EVENTGROUPS LIKE '%" + rule + "%'";
			if(!containArea(rule)) {
				partWhere += " AND EVENTGROUPS LIKE '%" + rule + "%'";
			}
		}
		if(allWhere.length() > 5) {
			allWhere = allWhere.substring(4); // delete the first "AND"
		}
		if(partWhere.length() > 5) {
			partWhere = partWhere.substring(4); // delete the first "AND"
		}
		String sql = sqlMain + "(" + (sqlFrom + allWhere) + " UNION " + (sqlFrom + partWhere) + ")";
		return sql;
	}

	private String getParameterValueSQL(String groupName) {
		String sql = "SELECT PARAMETERVALUE" +
					"  FROM RULEDB.RULEPARAMTABLE" +
					"  WHERE PARAMETERNAME = 'Name'" +
					"    AND OBJECTNAME LIKE '"+groupName+"%'";
		return sql;
	}
	
	private boolean areaRuleNotExist(List<String> rules) {
		if (rules.size() > 0) {
			for (String rule : rules) {
				if (containArea(rule))
					return false;
			}
		}
		return true;
	}
	
	private boolean containArea(String ruleName) {
		return ruleName.indexOf("/area/") > 0;
	}
	
	void connectionInfo() {
		System.out.println("----------------");
		System.out.println("url:\t" + url);
		System.out.println("user:\t" + user);
//		System.out.println("password:\t" + password);
		System.out.println("driver:\t" + driver);
		System.out.println("----------------");
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

}
