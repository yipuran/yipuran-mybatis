package org.yipuran.mybatis;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

/**
 * JBatisModule.
 * <pre>
 * mybatis-guice 使用では、リソースに mybatis-config.xml を用意して、IBatisModule の代わりに JBatisModule継承を使用する。
 * mybatis-config.xml で、任意のプロパティキー置換を実行できるように、
 * 　　public abstract Properties getProperty();
 * の実装で返す。
 *
 * 例） mybatis-config.xml
 *     property name="username" value="${USERNAME}
 *     の時、 "USERNAME" をセットした Properties をgetProperty() で返却する,
 * </pre>
 * @since 4.12
 */
public abstract class JBatisModule extends AbstractModule{
	public abstract Properties getConfigProperty();

	@Override
	protected void configure() {
		Injector injector = Guice.createInjector(new CoreBatisModule() {
			@Override
			public Properties getProperty() {
				return getConfigProperty();
			}
		});
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transaction.class), injector.getInstance(TransactionExecutor.class));
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(QueryLimited.class), injector.getInstance(QueryLimitedExecutor.class));
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(BatchTransaction.class), injector.getInstance(BatchTransactionExecutor.class));
	}

}
