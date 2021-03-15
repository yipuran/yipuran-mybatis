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
 * コード値参照方法を org.yipuran.util.EnumBase インターフェース実装で提供している enum のタイプハンドラ
 * @param <E>
 */
public class EnumValueTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E>{
	private Class<EnumBase<E>> cls;

	/**
	 * コンストラクタ.
	 * @param cls enumクラス
	 */
	public EnumValueTypeHandler(Class<EnumBase<E>> cls) {
		this.cls = cls;
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
			Object o = rs.getInt(columnName);
			return (E) Arrays.stream(cls.getEnumConstants()).filter(e->e.getValue().equals(o)).findAny().orElse(null);
		}catch(SQLException e) {
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		try {
			Object o = rs.getInt(columnIndex);
			return (E) Arrays.stream(cls.getEnumConstants()).filter(e->e.getValue().equals(o)).findAny().orElse(null);
		}catch(SQLException e) {
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		try {
			Object o = cs.getInt(columnIndex);
			return (E) Arrays.stream(cls.getEnumConstants()).filter(e->e.getValue().equals(o)).findAny().orElse(null);
		}catch(SQLException e) {
			throw e;
		}
	}
}
