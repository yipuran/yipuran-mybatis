package org.yipuran.mybatis;

import java.io.IOException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.guice.XMLMyBatisModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

/**
 * DataBaseModule.
 * mybatis-guice 使用では、リソースに mybatis-config.xml を用意して、IBatisModule の代わりに DataBaseModuleを使用する。
 * @since 4.11
 */
public class DataBaseModule extends XMLMyBatisModule{

	@Override
	protected void initialize() {
		Injector injector = Guice.createInjector(new AbstractModule(){
			@Override
			protected void configure(){
			}
			@Provides
			protected SqlSessionFactory providedBatchSqlSession() throws IOException{
				return new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
			}
		});
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transaction.class), injector.getInstance(TransactionExecutor.class));
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(QueryLimited.class), injector.getInstance(QueryLimitedExecutor.class));
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(BatchTransaction.class), injector.getInstance(BatchTransactionExecutor.class));
	}

}
