package com.hgst.checkalertgroup.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextFile {

	public static void write(File file, String text, boolean append) throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, append);
			writer.write(text);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(writer != null) {
				writer.close();
			}
		}
	}
	
	public static void write(File file, String text) throws IOException {
		write(file, text, false);
	}
	
	public static File forceNewFile(String name) throws IOException {
		File file = new File(name);
		if(!file.exists()) {
			File parentFile = file.getParentFile();
			if(parentFile != null) {
				parentFile.mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}
	
	public static void main(String[] args) {
		String name = "../c.txt";
		try {
			File file = forceNewFile(name);
			write(file, "我要写个文件工具类-----");
			System.out.println("over..");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
