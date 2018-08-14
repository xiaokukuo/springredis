/**
 * Copyright 2010-2018 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.mytest.datasource;

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

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
