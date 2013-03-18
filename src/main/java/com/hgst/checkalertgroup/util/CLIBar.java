package com.hgst.checkalertgroup.util;

/**
 * A cli processbar. <br>
 * <pre>
 * Usage(sample code): 
 *	
 * before the bussiness process, announce and instance the CLIBar, then start it.	
 * <code>
 *	CLIBar bar = new CLIBar();
 *	bar.start();
 * </code>
 * in the process code, update the CLIBar's state by 
 * <code>
 * 	bar.setRate( (double)i / length-1 );
 * </code>
 * 
 * </pre>
 * @author jcchen
 *
 */
public class CLIBar extends Thread {

	private int sleepTime = 200;
	private int count = 0;
	private double rate = 0.0;

	public CLIBar() {}
	
	/**
	 * display processbar
	 * 
	 * @param ratio
	 *            [0,1]
	 */
	void bar(double ratio) {
		int iRatio = (int) (Math.floor(ratio * 100));
		char[] symbol = new char[] { '-', '\\', '|', '/' };
		// to avoid the processbar so long, each 2% output one '='. that's why iRatio/2
		System.out.printf("%c [%-51s ] %d%%\r", 
				symbol[count++ % symbol.length],
				repeat('=', iRatio / 2) + ">", iRatio);
	}

	/**
	 * repeat char c
	 * 
	 * @param c
	 * @param len
	 * @return
	 */
	private String repeat(char c, int len) {
		char[] cs = new char[len];
		for (int i = 0; i < len; i++) {
			cs[i] = c;
		}
		return new String(cs);
	}

	@Override
	public void run() {
		try {
			while (true) {
				bar(rate);
				if ((int) rate == 1) {
					break;
				}
				Thread.sleep(sleepTime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
}
