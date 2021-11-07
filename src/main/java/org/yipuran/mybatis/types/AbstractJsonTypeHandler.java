package org.yipuran.mybatis.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Abstract JSONタイプハンドラ.
 * <PRE>
 * Jackson JSON prosessor による JSON タイプハンドラ抽象クラスで継承して使用する。
 * 継承先で com.fasterxml.jackson.databind.module.SimpleModule を返す抽象メソッドで
 * 任意のJSONシリアライザまたは、JSONデシリアライザを設定することが可能である。
 * </PRE>
 * @since 4.7
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public abstract class AbstractJsonTypeHandler<T> extends BaseTypeHandler<T> {
	private ObjectMapper mapper;
	private Class<T> clazz;
	public abstract SimpleModule getModule();
	/**
	 * コンストラクタ
	 * @param clazz JSONマッピング対象のクラス
	 */
	public AbstractJsonTypeHandler(Class<T> clazz) {
		if (clazz==null) throw new IllegalArgumentException("Type argument cannot be null");
		this.clazz = clazz;
		SimpleModule module = getModule();
		if (module==null) {
			mapper = new ObjectMapper();
		}else{
			mapper = new ObjectMapper().registerModule(module);
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
		try{
			ps.setString(i, mapper.writeValueAsString(parameter));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return this.toObject(rs.getString(columnName), clazz);
	}
	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return this.toObject(rs.getString(columnIndex), clazz);
	}
	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return this.toObject(cs.getString(columnIndex), clazz);
	}
	@SuppressWarnings("unchecked")
	private T toObject(String content, Class<?> clazz) {
		if (content != null && !content.isEmpty()) {
			try{
				return (T)mapper.readValue(content, clazz);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}else{
			return null;
		}
	}
}
