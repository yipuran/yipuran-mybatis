package org.yipuran.mybatis;

import java.io.Serializable;
/**
 * ORDER BY 句 生成ソートキー.
 * <PRE>
 * OREDER BY 句にするフィールド名とORDER方法、ASC / DESC をコンストラクタで指定する.
 * AbstractOrderByConditon で foreach を構成するための Order By 句を表現する
 *    &lt;foreach collection="orderList" item="orderby" open="ORDER BY" close="" separator=","&gt;
 *        ${orderby.field} ${orderby.type}
 *    &lt;/foreach&gt;
 * Collection&ltSortKey&gt; としてparameterType の属性になるようにする
 * </PRE>
 */
public class SortKey implements Serializable{
	private static final long serialVersionUID = 1L;
	/** OREDER BY 句にするフィールド名. */
	public String field;

	/** ASC / DESC type. */
	public String type;

	/**
	 * コンストラクタ.
	 * @param field OREDER BY 句にするフィールド名
	 * @param type ASC / DESC type
	 */
	public SortKey(String field, String type){
		this.field = field;
		this.type = type;
	}
}
