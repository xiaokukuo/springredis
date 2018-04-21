package org.apache.mytest.datasource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.sql.DataSource;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.BlobInputStreamTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PoolDataSource {

	private static final TypeHandler<InputStream> TYPE_HANDLER = new BlobInputStreamTypeHandler();

	private static SqlSessionFactory sqlSessionFactory;

	@Mock
	protected Blob blob;

	@Mock
	protected ResultSet rs;
	@Mock
	protected CallableStatement cs;
	@Mock
	protected ResultSetMetaData rsmd;

	@Mock
	protected PreparedStatement ps;

	@BeforeClass
	public static void setupSqlSessionFactory() throws Exception {
		DataSource dataSource = BaseDataTest.createUnpooledDataSource("org/apache/mytest/jdbc/jdbc.properties");
		BaseDataTest.runScript(dataSource, "org/apache/mytest/jdbc/created.sql");
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("Production", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(Mapper.class);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}

	@Test
	public void shouldSetParameter() throws Exception {
		InputStream in = new ByteArrayInputStream("Hello".getBytes());
		TYPE_HANDLER.setParameter(ps, 1, in, null);
		verify(ps).setBlob(1, in);
		System.out.println("as");
	}

	@Test
	public void shouldGetResultFromResultSetByName() throws Exception {
		InputStream in = new ByteArrayInputStream("Hello".getBytes());
		when(rs.getBlob("column")).thenReturn(blob);
		when(rs.wasNull()).thenReturn(false);
		when(blob.getBinaryStream()).thenReturn(in);
		assertThat(TYPE_HANDLER.getResult(rs, "column"), is(in));

	}

	static class BlobContent {
		private int id;
		private InputStream content;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public InputStream getContent() {
			return content;
		}

		public void setContent(InputStream content) {
			this.content = content;
		}
	}

}
