package org.yipuran.mybatis;

import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
/**
 * 複数ＤＢ接続のSqlSession 実行の抽象クラス.
 * <PRE>
 * Configuration の Envioment名を指定して複数接続できる→ environment タグの id 属性
 * </PRE>
 */
public abstract class MultiIBatisDao{
	@ControleSession private Map<String, SqlSession> map;
	@Inject @DefaultEnviroment private String defaultEnviromentName;

	/**
	 * デフォルトのSqlSession取得.
	 * @return SqlSession
	 */
	public SqlSession getSqlSession(){
		return this.map.get(this.defaultEnviromentName);
	}
	/**
	 * 接続先指定SqlSession取得.
	 * @param enviromentName  environment タグの id 属性
	 * @return SqlSession
	 */
	public SqlSession getSqlSession(String enviromentName){
		return this.map.get(enviromentName);
	}
	/**
	 * デフォルトのMapper取得.
	 * @param <T> Mapper Generic
	 * @param t Mapper class
	 * @return Mapper class インスタンス
	 */
	public <T> T getMapper(Class<T> t){
		return this.map.get(this.defaultEnviromentName).getMapper(t);
	}
	/**
	 * 接続先指定のMapper取得.
	 * @param <T> Mapper Generic
	 * @param t Mapper class
	 * @param enviromentName  environment タグの id 属性
	 * @return Mapper class インスタンス
	 */
	public <T> T getMapper(Class<T> t, String enviromentName){
		return this.map.get(enviromentName).getMapper(t);
	}
	/**
	 * SQLMapper bind Id string 取得.
	 * @param cls Mapper class
	 * @param id bind Id
	 * @return bindId string
	 */
	protected String bindId(Class<?> cls, String id){
		return cls.getName() + "." + id;
	}
}
