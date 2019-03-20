package org.yipuran.mybatis.types;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * StringArrayTypeHandler. String[] for ArrayTypeHandler
 * <PRE>
 * resultMap result
 *
 * &lt;result column="ary" property="ary" typeHandler="org.yipuran.mybatis.types.StringArrayTypeHandler" jdbcType="ARRAY" javaType="string" /&gt;
 *
 * OR
 *
 * &lt;typeHandler handler="org.yipuran.mybatis.types.StringArrayTypeHandler" jdbcType="ARRAY" /&gt;
 *
 * OR
 *
 * &lt;typeHandlers&gt;
 *   &lt;package name="org.yipuran.mybatis.types"/&gt;
 * &lt;/typeHandlers&gt;
 * </PRE>
 */
public class StringArrayTypeHandler extends BaseTypeHandler<String[]>{
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException{
		@SuppressWarnings("resource")
		Connection conn = ps.getConnection();
		String[] po = new String[parameter.length];
		int x = 0;
		for(String p:parameter){
			po[x] = p;
			x++;
		}
		Array array = conn.createArrayOf("string", po);
		ps.setArray(i, array);
	}
	@Override
	public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException{
		return getArray(rs.getArray(columnName));	}
	@Override
	public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
		return getArray(rs.getArray(columnIndex));
	}
	@Override
	public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
		return getArray(cs.getArray(columnIndex));
	}

	private String[] getArray(Array array){
		if (array==null){
			return null;
		}
		try{
			Object[] ary = (Object[]) array.getArray();
			String[] rtn = new String[ary.length];
			int i = 0;
			for(Object o:ary){
				rtn[i] = o==null ? null : o.toString();
				i++;
			}
			return rtn;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
