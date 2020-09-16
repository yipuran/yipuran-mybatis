package org.yipuran.mybatis;

import java.io.IOException;

import javax.inject.Provider;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
/**
 * SqlSessionFactory または、トランザクション インターセプタをバインド.
 * <PRE>
 * コンストラクタで、configuration のXMLファイル名を指定する。
 * 引数なしコンストラクタを使用した場合は、Configuration.xml が指定されたものとして解釈される。
 * </PRE>
 */
public class IBatisModule extends AbstractModule implements Provider<SqlSessionFactory>{
	final String configXmlPath;
	/**
	 * default コンストラクタ.
	 */
	public IBatisModule(){
		this.configXmlPath = "Configuration.xml";
	}
	/**
	 * コンストラクタ.
	 * @param configXmlPath configurationファイルpath
	 */
	public IBatisModule(String configXmlPath){
		this.configXmlPath = configXmlPath;
	}
	/*
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure(){
		Injector injector = Guice.createInjector(new AbstractModule(){
			@Override
			protected void configure(){
			}
			@Provides
			protected SqlSessionFactory providedBatchSqlSession() throws IOException{
				return new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(getConfigXmlPath()));
			}
		});
		binder().bind(SqlSessionFactory.class).toProvider(this.getClass());
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transaction.class), injector.getInstance(TransactionExecutor.class));
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(QueryLimited.class), injector.getInstance(QueryLimitedExecutor.class));
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(BatchTransaction.class), injector.getInstance(BatchTransactionExecutor.class));
	}
	/*
	 * @see javax.inject.Provider#get()
	 */
	@Override
	public SqlSessionFactory get(){
		try{
			return new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(getConfigXmlPath()));
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	/**
	 * get configuration XML file path.
	 * @return configuration XML file path
	 */
	protected String getConfigXmlPath(){
		return this.configXmlPath;
	}
}
