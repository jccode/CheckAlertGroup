package com.hgst.checkalertgroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Hello world!
 * 
 */
public class App {
	
	public static final String KEY_DB_URL = "db.url";
	public static final String KEY_DB_USER = "db.user";
	public static final String KEY_DB_PASSWORD = "db.password";
	public static final String KEY_DB_DRIVER = "db.driver";
	
	public static void main(String[] args) throws Exception {
		App app = new App();
		app.loadConfig();
	}
	
	void loadConfig() throws IOException, ClassNotFoundException {
		InputStream is = this.getClass().getResourceAsStream("/db.properties");
		Properties p = new Properties();
		p.load(is);
		String url = p.getProperty(KEY_DB_URL);
		String driver = p.getProperty(KEY_DB_DRIVER);
		String user = p.getProperty(KEY_DB_USER);
		String password = p.getProperty(KEY_DB_PASSWORD);
		CheckAlertGroup check = new CheckAlertGroup(url, driver, user, password);
		
		new Thread(check).start();
	}
}
