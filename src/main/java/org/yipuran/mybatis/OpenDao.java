package org.yipuran.mybatis;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * SqlSessionFactory インジェクトDAO 抽象クラス.
 * <PRE>
 * インジェクトされる SqlSessionFactory を持つ抽象クラス
 * </PRE>
 */
public abstract class OpenDao implements OpenDaoFactory{
	@Inject private SqlSessionFactory factory;

	/**
	 * SqlSessionFactory 参照.
	 * @return
	 */
	public SqlSessionFactory getFactory() {
		return factory;
	}
}
