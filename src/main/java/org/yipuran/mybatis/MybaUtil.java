package org.yipuran.mybatis;

/**
 * mybatis利用ユーティリティ.
 */
public final class MybaUtil{
	/** private constructor. */
	private MybaUtil(){}
	
	/**
	 * Mapperクラス名＋SQL ID 生成.
	 * IBatisDaoの bindId と同じ処理
	 * @param cls Mapperクラス
	 * @param id SQL ID
	 * @return 生成したbinID文字列
	 */
	public static String bindId(Class<?> cls, String id){
		return cls.getName() + "." + id;
	}
}
