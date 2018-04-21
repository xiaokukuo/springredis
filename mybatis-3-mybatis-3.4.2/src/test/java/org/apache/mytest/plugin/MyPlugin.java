package org.apache.mytest.plugin;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

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
