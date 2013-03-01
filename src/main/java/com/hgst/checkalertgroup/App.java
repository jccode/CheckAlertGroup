package com.hgst.checkalertgroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hgst.checkalertgroup.io.CheckResultWriter;

/**
 * Hello world!
 * 
 */
public class App {
	
	public static void main(String[] args) throws Exception {
		App app = new App();
		app.loadConfig();
	}
	
	void loadConfig() throws IOException, ClassNotFoundException {
		InputStream is = this.getClass().getResourceAsStream("/db.properties");
		Properties p = new Properties();
		p.load(is);
		Env env = Env.init(p);
		CheckResultWriter writer = new CheckResultWriter(env);
		CheckAlertGroup check = new CheckAlertGroup(env, writer);
		
		new Thread(check).start();
	}
}
