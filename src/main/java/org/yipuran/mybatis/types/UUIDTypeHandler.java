package org.yipuran.mybatis.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
/**
 * UUIDTypeHandler. UUIDタイプハンドラ
 *
 * configuration XML add
 *
 * <typeHandlers>
 *    <typeHandler handler="org.yipuran.mybatis.types.UUIDTypeHandler" javaType="java.util.UUID"/>
 * </typeHandlers>
 *
 */
public class UUIDTypeHandler extends BaseTypeHandler<UUID>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
		ps.setObject(i, parameter);
	}

	@Override
	public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getObject(columnName, UUID.class);
	}

	@Override
	public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getObject(columnIndex, UUID.class);
	}

	@Override
	public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getObject(columnIndex, UUID.class);
	}

}
