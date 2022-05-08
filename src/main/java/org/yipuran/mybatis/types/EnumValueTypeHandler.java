package org.yipuran.mybatis.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.yipuran.util.EnumBase;

/**
 * コード値内包 enum タイプハンドラ.
 * <pre>
 * コード値参照方法を org.yipuran.util.EnumBase インターフェース実装で提供している enum のタイプハンドラ
 *
 * public enum Color implements EnumBase&lt;Color&gt; {
 *   :
 * }
 *
 * &lt;typeHandlers&gt;
 *    &lt;typeHandler handler="org.yipuran.mybatis.types.EnumValueTypeHandler" javaType="sample.Color"/&gt;
 * &lt;/typeHandlers&gt;
 * もしくは、
 *
 * package sample.types;
 * public class ColorEnumTypeHandler extends EnumValueTypeHandler&lt;Color&gt;{
 *   public ColorEnumTypeHandler() {
 *      super(Color.class);
 *   }
 * }
 *
 *  &lt;typeHandlers&gt;
 *       &lt;package name="sample.types"/&gt;
 *  &lt;/typeHandlers&gt;
 * </pre>
 * @param <E>
 */
public class EnumValueTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E>{
	private Class<EnumBase<E>> cls;

	public EnumValueTypeHandler() {
	}
	/**
	 * コンストラクタ.
	 * @param cls enumのEnumBase実装クラス
	 */
	@SuppressWarnings("unchecked")
	public EnumValueTypeHandler(Class<? extends EnumBase<E>> cls) {
		this.cls = (Class<EnumBase<E>>)cls;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException{
		if (parameter==null) {
			ps.setObject(i, null);
		}else{
			ps.setObject(i, ((EnumBase<E>)parameter).getValue());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException{
		try {
			Object o = rs.getObject(columnName);
			return (E) Arrays.stream(cls.getEnumConstants()).filter(e->e.getValue().equals(o)).findAny().orElse(null);
		}catch(SQLException e) {
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		try {
			Object o = rs.getObject(columnIndex);
			return (E) Arrays.stream(cls.getEnumConstants()).filter(e->e.getValue().equals(o)).findAny().orElse(null);
		}catch(SQLException e) {
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		try {
			Object o = cs.getObject(columnIndex);
			return (E) Arrays.stream(cls.getEnumConstants()).filter(e->e.getValue().equals(o)).findAny().orElse(null);
		}catch(SQLException e) {
			throw e;
		}
	}
}
