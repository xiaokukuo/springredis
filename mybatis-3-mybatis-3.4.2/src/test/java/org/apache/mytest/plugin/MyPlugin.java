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
package org.apache.mytest.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class})
})
public class MyPlugin implements Interceptor {

    Properties props = null;


    /**
     * 代替拦截对象方法的内容
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.err.println("before…………");
        Object obj = invocation.proceed();
        System.err.println("after…………");

        return obj;
    }

    @Override
    public Object plugin(Object target) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub

    }

}
