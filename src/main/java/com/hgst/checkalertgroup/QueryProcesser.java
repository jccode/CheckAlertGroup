package com.hgst.checkalertgroup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface QueryProcesser<T> {

	public T process(Statement stmt, ResultSet rs) throws SQLException;

}
