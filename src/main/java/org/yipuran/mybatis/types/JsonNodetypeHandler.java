package org.yipuran.mybatis.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson JsonNode typeHandler.
 * @since Ver4.8
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonNodetypeHandler extends BaseTypeHandler<JsonNode>{
	private ObjectMapper mapper;
	private Class<JsonNode> clazz;
	/**
	 * コンストラクタ.
	 * @param clazz Class JsonNode
	 */
	public JsonNodetypeHandler(Class<JsonNode> clazz) {
		if (clazz==null) throw new IllegalArgumentException("Type argument cannot be null");
		this.clazz = clazz;
		mapper = new ObjectMapper();
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, JsonNode parameter, JdbcType jdbcType) throws SQLException {
		try{
			ps.setString(i, mapper.writeValueAsString(parameter));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	@Override
	public JsonNode getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return this.toObject(rs.getString(columnName), clazz);
	}
	@Override
	public JsonNode getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return this.toObject(rs.getString(columnIndex), clazz);
	}
	@Override
	public JsonNode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return this.toObject(cs.getString(columnIndex), clazz);
	}
	private JsonNode toObject(String content, Class<?> clazz) {
		if (content != null && !content.isEmpty()) {
			try{
				return mapper.readTree(content);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}else{
			return null;
		}
	}
}
