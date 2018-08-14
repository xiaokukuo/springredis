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
package org.apache.ibatis.submitted.xml_external_ref;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class SameIdTest {

    @Test
    public void testCrossReferenceXmlConfig() throws Exception {
        testCrossReference(getSqlSessionFactoryXmlConfig());
    }

    @Test
    public void testCrossReferenceJavaConfig() throws Exception {
        testCrossReference(getSqlSessionFactoryJavaConfig());
    }

    private void testCrossReference(SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            SameIdPersonMapper personMapper = sqlSession.getMapper(SameIdPersonMapper.class);
            Person person = personMapper.select(1);
            assertEquals((Integer) 1, person.getId());
            assertEquals(2, person.getPets().size());
            assertEquals((Integer) 2, person.getPets().get(1).getId());

            Pet pet = personMapper.selectPet(1);
            assertEquals(Integer.valueOf(1), pet.getId());

            SameIdPetMapper petMapper = sqlSession.getMapper(SameIdPetMapper.class);
            Pet pet2 = petMapper.select(3);
            assertEquals((Integer) 3, pet2.getId());
            assertEquals((Integer) 2, pet2.getOwner().getId());
        } finally {
            sqlSession.close();
        }
    }

    private SqlSessionFactory getSqlSessionFactoryXmlConfig() throws Exception {
        Reader configReader = Resources
                .getResourceAsReader("org/apache/ibatis/submitted/xml_external_ref/SameIdMapperConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configReader);
        configReader.close();

        Connection conn = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource().getConnection();
        initDb(conn);

        return sqlSessionFactory;
    }

    private SqlSessionFactory getSqlSessionFactoryJavaConfig() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:xmlextref", "sa", "");
        initDb(c);

        Configuration configuration = new Configuration();
        Environment environment = new Environment("development", new JdbcTransactionFactory(), new UnpooledDataSource(
                "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:xmlextref", null));
        configuration.setEnvironment(environment);

        configuration.addMapper(SameIdPersonMapper.class);
        configuration.addMapper(SameIdPetMapper.class);

        return new SqlSessionFactoryBuilder().build(configuration);
    }

    private static void initDb(Connection conn) throws IOException, SQLException {
        try {
            Reader scriptReader = Resources.getResourceAsReader("org/apache/ibatis/submitted/xml_external_ref/CreateDB.sql");
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setLogWriter(null);
            runner.setErrorLogWriter(null);
            runner.runScript(scriptReader);
            conn.commit();
            scriptReader.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
