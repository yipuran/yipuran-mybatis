package org.yipuran.mybatis.types;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * LocalDateTimeArrayTypeHandler. for LocalDateTime[] ArrayTypeHandler
 * <PRE>
 * resultMap result
 *
 * &lt;result column="ary" property="ary" typeHandler="org.yipuran.mybatis.types.LocalDateTimeArrayTypeHandler"
 *            jdbcType="TIMESTAMP" javaType="java.time.LocalDateTime" /&gt;
 *
 * OR
 *
 * &lt;typeHandler handler="org.yipuran.mybatis.types.LocalDateTimeArrayTypeHandler" jdbcType="ARRAY" /&gt;
 *
 * OR
 *
 * &lt;typeHandlers&gt;
 *   &lt;package name="org.yipuran.mybatis.types"/&gt;
 * &lt;/typeHandlers&gt;
 * </PRE>
 */
public class LocalDateTimeArrayTypeHandler extends BaseTypeHandler<LocalDateTime[]>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime[] parameter, JdbcType jdbcType) throws SQLException{
		@SuppressWarnings("resource")
		Connection conn = ps.getConnection();
		LocalDateTime[] po = new LocalDateTime[parameter.length];
		int x = 0;
		for(LocalDateTime p:parameter){
			po[x] = p;
			x++;
		}
		Array array = conn.createArrayOf("timestamp", po);
		ps.setArray(i, array);
	}
	@Override
	public LocalDateTime[] getNullableResult(ResultSet rs, String columnName) throws SQLException{
		return getArray(rs.getArray(columnName));
	}

	@Override
	public LocalDateTime[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		return getArray(rs.getArray(columnIndex));
	}

	@Override
	public LocalDateTime[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		return getArray(cs.getArray(columnIndex));
	}
	private LocalDateTime[] getArray(Array array){
		if (array==null){
			return null;
		}
		try{
			Timestamp[] ary = (Timestamp[])array.getArray();
			LocalDateTime[] rtn = new LocalDateTime[ary.length];
			int i = 0;
			for(Timestamp t:ary){
				rtn[i] = t.toLocalDateTime();
				i++;
			}
			return rtn;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
