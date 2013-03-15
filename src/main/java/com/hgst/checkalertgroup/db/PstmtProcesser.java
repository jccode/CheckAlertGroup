package com.hgst.checkalertgroup.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PstmtProcesser {

	public void setParamters(PreparedStatement pstmt) throws SQLException;
}
