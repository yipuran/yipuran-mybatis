package org.yipuran.mybatis.types;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * LocalDateArrayTypeHandler. for LocalDate[] ArrayTypeHandler
 * <PRE>
 * resultMap result
 *
 * &lt;result column="ary" property="ary" typeHandler="org.yipuran.mybatis.types.LocalDateArrayTypeHandler"
 *            jdbcType="DATE" javaType="java.time.LocalDate" /&gt;
 *
 * OR
 *
 * &lt;typeHandler handler="org.yipuran.mybatis.types.LocalDateArrayTypeHandler" jdbcType="ARRAY" /&gt;
 *
 * OR
 *
 * &lt;typeHandlers&gt;
 *   &lt;package name="org.yipuran.mybatis.types"/&gt;
 * &lt;/typeHandlers&gt;
 * </PRE>
 */
public class LocalDateArrayTypeHandler extends BaseTypeHandler<LocalDate[]>{
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LocalDate[] parameter, JdbcType jdbcType) throws SQLException{
		@SuppressWarnings("resource")
		Connection conn = ps.getConnection();
		LocalDate[] po = new LocalDate[parameter.length];
		int x = 0;
		for(LocalDate p:parameter){
			po[x] = p;
			x++;
		}
		Array array = conn.createArrayOf("date", po);
		ps.setArray(i, array);
	}
	@Override
	public LocalDate[] getNullableResult(ResultSet rs, String columnName) throws SQLException{
		return getArray(rs.getArray(columnName));
	}
	@Override
	public LocalDate[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		return getArray(rs.getArray(columnIndex));
	}
	@Override
	public LocalDate[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		return getArray(cs.getArray(columnIndex));
	}
	private LocalDate[] getArray(Array array){
		if (array==null){
			return null;
		}
		try{
			Date[] ary = (Date[])array.getArray();
			LocalDate[] rtn = new LocalDate[ary.length];
			int i = 0;
			for(Date t:ary){
				rtn[i] = t.toLocalDate();
				i++;
			}
			return rtn;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
