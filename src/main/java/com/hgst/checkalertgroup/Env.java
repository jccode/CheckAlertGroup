package com.hgst.checkalertgroup;

import java.util.Properties;

public class Env {
	
	public static final String KEY_DB_URL = "db.url";
	public static final String KEY_DB_USER = "db.user";
	public static final String KEY_DB_PASSWORD = "db.password";
	public static final String KEY_DB_DRIVER = "db.driver";
	public static final String KEY_FILE_OUTPUT = "file.output";
	public static final String KEY_FILE_FORMAT = "file.format";
	public static final String KEY_TB_ACT = "tb.act";
	public static final String KEY_TB_RULEPARAMTABLE = "tb.ruleparamtable";
	
	private static Env env = new Env();
	private static Properties p;

	private Env() {
		super();
	}
	
	public static Env init(Properties p) {
		Env.p = p;
		return getInstance();
	}
	
	public static Env getInstance() {
		return env;
	}
	
	public String get(String key) {
		return p.getProperty(key);
	}
	
	public String get(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}
}
