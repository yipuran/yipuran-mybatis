package org.yipuran.mybatis;

import com.google.inject.Guice;
import com.google.inject.Module;

/**
 * OpenDao 生成インターフェース.
 */
public interface OpenDaoFactory {
	/**
	 * OpenDao 継承クラス生成.
	 * IBatisModuleでバインド定義されて生成する
	 * @param cls
	 * @param modules IBatisModule 以外に追加したい Module定義
	 * @return
	 */
	public static <T extends OpenDao> T create(Class<T> cls, Module...modules) {
		if (modules==null || modules.length==0) {
			return Guice.createInjector(new IBatisModule()).getInstance(cls);
		}
		return Guice.createInjector(new IBatisModule()).createChildInjector(modules).getInstance(cls);
	}
}
