package com.hgst.checkalertgroup.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.hgst.checkalertgroup.Env;
import com.hgst.checkalertgroup.model.CheckResult;

public class CheckResultWriter {

	public static final String TMPL_SYMBOL_NO_AREA = "${noarea}";
	public static final String TMPL_SYMBOL_AREA_INVALID = "${areaInvalid}";
	public static final String TMPL_SYMBOL_AREA_VALID = "${areaValid}";
	
	private Env env;
	private File output;
	private String template = "不存在area rule的group:\n${noarea}\n\n" +
			"存在area但不影响最终结果的group(无效的area条件):\n${areaInvalid}\n\n" +
			"存在area且影响最终结果的group(有效的area条件):\n${areaValid}";

	public CheckResultWriter(Env env) throws IOException {
		this.env = env;
		output = TextFile.forceNewFile(this.env.get(Env.KEY_FILE_OUTPUT));
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
	
	public void write(List<CheckResult> results) {
		try {
			String out = "GroupName\tType\tInvalidParameters\tMessage\n"+listToString(results);
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
	
	private String listToString(List<? extends Object> list) {
		StringBuilder sb = new StringBuilder();
		for(Object o : list) {
			sb.append(o.toString()).append("\n");
		}
		return sb.toString();
	}
}
