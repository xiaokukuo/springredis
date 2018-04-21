package org.apache.mytest.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.mytest.MyBaseDataTest;
import org.junit.Test;

public class SqlRunnerTest extends MyBaseDataTest {

	public static final String JDBC_ = "org/apache/mytest/jdbc/jdbc.properties";
	
	public static final String CREATED_ = "org/apache/mytest/jdbc/created.sql";

	@Test
	public void shouldSelectOne() throws Exception {
		DataSource ds = createUnpooledDataSource(JDBC_);
		runScript(ds, CREATED_);
		Connection connection = ds.getConnection();
		SqlRunner exec = new SqlRunner(connection);
		connection.close();
	}

}
