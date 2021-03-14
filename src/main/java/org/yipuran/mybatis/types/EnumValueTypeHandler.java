package org.yipuran.mybatis.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	@SuppressWarnings("unchecked")
	public EnumValueTypeHandler(Class<E> cls) {
		this.cls = (Class<EnumBase<E>>) cls;
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
	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException{
		return EnumBase.parseCode(cls, rs.getInt(columnName)).orElse(null);
	}
	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		return EnumBase.parseCode(cls, rs.getInt(columnIndex)).orElse(null);
	}
	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		return EnumBase.parseCode(cls, cs.getInt(columnIndex)).orElse(null);
	}
}
