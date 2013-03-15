package com.hgst.checkalertgroup.util;

public class CLIBar {

	/**
	 * display a cli process bar
	 * @param ratio [0,1]
	 */
	public static void bar(double ratio) {
		int iRatio = (int)(Math.floor(ratio * 100));
		char[] symbol = new char[]{'-','\\','|','/'};
		// 50 '=' represent 100%, to avoid the process bar too long. iRatio/2
		System.out.printf("%c [%-51s ] %d%%\r", symbol[iRatio%symbol.length], repeat('=', iRatio/2)+">", iRatio);
	}
	
	/**
	 * repeat char c
	 * @param c
	 * @param len
	 * @return
	 */
	private static String repeat(char c, int len) {
		char[] cs = new char[len];
		for(int i = 0; i < len; i++) {
			cs[i] = c;
		}
		return new String(cs);
	}
}
