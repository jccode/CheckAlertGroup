package com.hgst.checkalertgroup.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.hgst.checkalertgroup.Env;

public class CheckResultWriter {

	public static final String TMPL_SYMBOL_NO_AREA = "${noarea}";
	public static final String TMPL_SYMBOL_AREA_INVALID = "${areaInvalid}";
	public static final String TMPL_SYMBOL_AREA_VALID = "${areaValid}";
	
	private Env env;
	private File output;
	private String template;

	public CheckResultWriter(Env env) throws IOException {
		this.env = env;
		output = TextFile.forceNewFile(this.env.get(Env.KEY_FILE_OUTPUT));
		template = env.get(Env.KEY_FILE_FORMAT);
	}
	
	public void write(List<String> withoutAreaRule, List<String> areaSameResult, List<String> areaDiffResult) {
		String out = template.replace(TMPL_SYMBOL_NO_AREA, wrapper(withoutAreaRule))
				.replace(TMPL_SYMBOL_AREA_INVALID, wrapper(areaSameResult))
				.replace(TMPL_SYMBOL_AREA_VALID, wrapper(areaDiffResult));
		try {
			TextFile.write(output, out);
		} catch (IOException e) {
			System.out.println("输出最终结果失败. 异常:");
			e.printStackTrace();
		}
	}
	
	private String wrapper(List<String> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("共 " + list.size() + " 条\n");
		sb.append("------------------------------------\n");
		sb.append(listToString(list));
		return sb.toString();
	}
	
	private String listToString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for(String s : list) {
			sb.append(s).append("\n");
		}
		return sb.toString();
	}
}
