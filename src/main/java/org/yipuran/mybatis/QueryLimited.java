package org.yipuran.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * クエリー実行のみを約束するアノテーション.
 * <PRE>
 * IBatisDao を継承してメソッドに@QueryLimited を付与する
 *
 * SqlSession を使う場合は、IBatisDao の getSqlSession() を使う。
 * 任意のSQLMapper インスタンスを取得する場合は、IBatisDao の
 * public &lt;T&gt; T getMapper(Class&lt;T&gt; t) を使う。T は、interface
 * </PRE>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface QueryLimited{
}
