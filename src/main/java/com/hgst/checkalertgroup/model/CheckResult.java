package com.hgst.checkalertgroup.model;

import java.util.List;

public class CheckResult {

	private String groupName;
	private String message;
	private List<String> invalidParameters;
	private ResultType type;
	
	public CheckResult() {
		super();
	}

	public CheckResult(String groupName, ResultType type, String message) {
		this.groupName = groupName;
		this.message = message;
		this.type = type;
	}
	
	public CheckResult(String groupName, ResultType type, String message,
			List<String> invalidParameters) {
		this(groupName, type, message);
		this.invalidParameters = invalidParameters;
	}

	public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getInvalidParameters() {
		return invalidParameters;
	}

	public void setInvalidParameters(List<String> invalidParameters) {
		this.invalidParameters = invalidParameters;
	}

	@Override
	public String toString() {
		return this.groupName + "\t" + this.type + "\t" + this.invalidParameters + "\t" + this.message;
	}
}
