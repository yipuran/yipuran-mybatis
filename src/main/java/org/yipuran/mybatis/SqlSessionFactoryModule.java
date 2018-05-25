package org.yipuran.mybatis;

import javax.inject.Provider;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.inject.AbstractModule;
/**
 * SqlSessionFactoryのみをバインド定義.
 * コンフィグファイルは、Configuration.xml 固定
 */
public final class SqlSessionFactoryModule extends AbstractModule implements Provider<SqlSessionFactory>{
	final String configXmlPath;
	/**
	 * デフォルト コンストラクタ.
	 */
	public SqlSessionFactoryModule(){
		this.configXmlPath = "Configuration.xml";
	}
	/**
	 * Configuration ファイルパス指定のコンストラクタ.
	 * @param configXmlPath Configuration ファイルパス
	 */
	public SqlSessionFactoryModule(String configXmlPath){
		this.configXmlPath = configXmlPath;
	}
	/*
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure(){
		binder().bind(SqlSessionFactory.class).toProvider(this.getClass());
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
	 * Configuration ファイルパス取得.
	 * @return Configuration ファイルパス
	 */
	protected String getConfigXmlPath(){
		return this.configXmlPath;
	}
}
