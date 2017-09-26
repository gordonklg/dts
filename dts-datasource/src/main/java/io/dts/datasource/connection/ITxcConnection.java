package io.dts.datasource.connection;

import java.sql.Connection;
import java.sql.SQLException;

import io.dts.datasource.core.IDtsDataSource;
import io.dts.parser.model.TxcRuntimeContext;

public interface ITxcConnection extends Connection {
	/**
	 * 获取不带事务的数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	Connection getRawConnection() throws SQLException;

	/**
	 * 获取TxcDataSource
	 * 
	 * @return
	 * @throws SQLException
	 */
	IDtsDataSource getDataSource() throws SQLException;

	/**
	 * 获取上下文
	 * @return
	 */
	TxcRuntimeContext getTxcContext();

}
