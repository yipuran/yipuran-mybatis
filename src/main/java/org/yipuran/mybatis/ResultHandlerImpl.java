package org.yipuran.mybatis;

import java.util.function.Consumer;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * Generic ResultHandler実装.
 * <PRE>
 * SqlSession.select(String statement, ResultHandler handler) の ResultHandler インタフェースの実装
 * ResultHandler の void handleResult(ResultContext&lt;? extends T&gt; resultContext) 実装インナークラス記述のかわりに、
 * ResultContext#getResultObject() で取得済の結果を Consumer&lt;T&gt; 処理記述で実装するようにする。
 *
 * （ResultHandler インナークラスの記述）
 * getSqlSession().select(bindId(MyMapper.class, "listA"), new ResultHandler&lt;Alpha&gt;(){
 *    ＠Override
 *    public void handleResult(ResultContext&lt;? extends Alpha&gt; resultContext){
 *       Alpha a = resultContext.getResultObject();
 *       //
 *    }
 * });
 *
 * （ResultHandlerImplの記述）
 * getSqlSession().select(bindId(MyMapper.class, "listA"), ResultHandlerImpl.of(Alpha.class, t->{
 *     // t = Alpha
 * });
 *
 * または、
 *
 * getSqlSession().select(bindId(MyMapper.class, "listA"), new ResultHandlerImpl&lt;Alpha&gt;(t->{
 *     // t = Alpha
 * });
 *
 * </PRE>
 * @since 4.3
 */
public class ResultHandlerImpl<T> implements ResultHandler<T>{
	private Consumer<T> consumer;

	public ResultHandlerImpl(Consumer<T> consumer){
		this.consumer = consumer;
	}

	public static <T> ResultHandlerImpl<T> of(Class<T> cls, Consumer<T> consumer){
		return new ResultHandlerImpl<>(consumer);
	}

	@Override
	public void handleResult(ResultContext<? extends T> resultContext) {
		consumer.accept(resultContext.getResultObject());
	}
}
