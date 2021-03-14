package org.yipuran.mybatis;

import org.apache.ibatis.session.SqlSession;
/**
 * SqlSession 実行の抽象クラス.
 * <PRE>
 * サブクラスのメソッドで @QueryLimited または、@Transaction を付与したメソッドの中で
 * getSqlSession() を使う。
 *
 * 任意のSQLMapper インスタンスを取得する場合は、
 *   public &lt;T&gt; T getMapper(Class&lt;T&gt; t) を使う。T は、Java interface
 *
 * SqlSession が実行するメソッドは、
 * sqlmap.xml &lt;mapper&gt; の namespace属性と SQL の ID を結合させる
 * メソッドが用意されている→protected String bindId の結果を使うことが奨励される
 * </PRE>
 */
public abstract class IBatisDao{
	@ControleSession private SqlSession sqlSession;

	/**
	 * SqlSession取得.
	 * @return SqlSession
	 */
	public SqlSession getSqlSession(){
		return this.sqlSession;
	}
	/**
	 * Mapper取得.
	 * @param <T> Mapper Generic
	 * @param t Mapper class
	 * @return Mapper class インスタンス
	 */
	public <T> T getMapper(Class<T> t){
		return this.sqlSession.getMapper(t);
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
