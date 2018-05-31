package org.yipuran.mybatis;

import java.io.IOException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.yipuran.mybatis.spi.SpiConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.MapBinder;
/**
 * 複数ＤＢ接続用、SqlSessionFactory または、トランザクション インターセプタをバインド.
 */
public class MultiIBatisModule extends AbstractModule{
	private SpiConfiguration spiConfiguration;
	String configXmlName;
	/**
	 * コンストラクタ.
	 */
	public MultiIBatisModule(){
		this.configXmlName = "Configuration.xml";
		this.spiConfiguration = SpiConfiguration.getInstance();
	}
	/* (非 Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure(){
		binder().bind(String.class).annotatedWith(DefaultEnviroment.class).toInstance(getSpiConfiguration().getDefaultEnviroment());
		Injector injector = Guice.createInjector(new AbstractModule(){
			@Override
			protected void configure(){
				MapBinder<String, SqlSessionFactory> factoryBinder = MapBinder.newMapBinder(binder(), String.class, SqlSessionFactory.class, ControleSession.class);
				try{
					for(String name : getSpiConfiguration().enviromentNames()){
						factoryBinder.addBinding(name).toInstance(new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(MultiIBatisModule.this.configXmlName), name));
					}
				}catch(IOException e){
					throw new RuntimeException("Configration SqlSessionFactory bind Error : " + e.getMessage(), e);
				}
			}
		});
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transaction.class), injector.getInstance(MxTransactionExecutor.class));
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(QueryLimited.class), injector.getInstance(MxQueryLimitedExecutor.class));
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(BatchTransaction.class), injector.getInstance(BatchMxTransactionExecutor.class));
	}
	/**
	 * SpiConfiguration取得.
	 * @return SpiConfiguration
	 */
	protected SpiConfiguration getSpiConfiguration(){
		return this.spiConfiguration;
	}
}
