package com.hgst.checkalertgroup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hgst.checkalertgroup.io.CheckResultWriter;

/**
 * Check Alert Group App
 * 
 */
public class App {
	
	public static void main(String[] args) throws Exception {
		App app = new App();
		app.loadConfig();
	}
	
	/**
	 * load properties config file.<br>
	 * if the config file in current path exist, will override the defalut config file in classpath.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void loadConfig() throws IOException, ClassNotFoundException {
		
		Properties p = new Properties();
		
		// load the default from classpath first
		InputStream defaultConfig = this.getClass().getResourceAsStream("/db.properties");
		p.load(defaultConfig);
		
		// try to load from the current directory, if the config file exist.
		try {
			InputStream externalConfig = new FileInputStream("db.properties");
			p.load(externalConfig);
		} catch (FileNotFoundException e) {
		}
		
		Env env = Env.init(p);
		CheckResultWriter writer = new CheckResultWriter(env);
		CheckAlertGroup check = new CheckAlertGroup(env, writer);
		
		new Thread(check).start();
	}
}
