package org.yipuran.mybatis;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * IBatisDaoPlus  IBatisDao を継承したselect拡張追加.
 * <PRE>
 * selectBounds は、SQLMap で実行する select クエリが返す行数が膨大な場合に、
 * SQLを複問い合わせを記述せず、offet,limit で 隠蔽された mybatis ResultHandler の使用結果
 * として List を返す。
 * </PRE>
 */
public abstract class IBatisDaoPlus extends IBatisDao{
	@Inject private SqlSessionFactory factory;
	/**
	 * RowBoundsクエリ.
	 * @param <T> クエリ抽出対象ObjectのGeneric
	 * @param sqlid SQL id
	 * @param offset offset
	 * @param limit LIMIT値
	 * @return List<T>
	 */
	@QueryLimited
   public <T> List<T> selectBounds(String sqlid, int offset, int limit){
		final List<T> list = new ArrayList<T>();
		try{
			getSqlSession().select(sqlid, null, new RowBounds(offset, limit), new ResultHandler(){
				@SuppressWarnings("unchecked")
				@Override
				public void handleResult(ResultContext con){
					list.add((T)con.getResultObject());
				}
			});
		}catch(Exception e){
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.warn("## IBatisDaoPlus.selectBounds(sqlid,offset,limit) : " + e.getMessage(), e);
			SqlSession session = factory.openSession();
			session.select(sqlid, null, new RowBounds(offset, limit), new ResultHandler(){
				@SuppressWarnings("unchecked")
				@Override
				public void handleResult(ResultContext con){
					list.add((T)con.getResultObject());
				}
			});
			session.close();
		}
		return list;
   }
	/**
	 * parameter指定、RowBoundsクエリ.
	 * sqlidが指すSQLMap のselect に、抽出条件のパラメータを指定する。
	 * @param <T> クエリ抽出対象ObjectのGeneric
	 * @param sqlid sqlid SQL id
	 * @param param 抽出条件のパラメータ
	 * @param offset offset
	 * @param limit LIMIT値
	 * @return List<T>
	 */
	@QueryLimited
	public <T> List<T> selectBounds(String sqlid, Object param, int offset, int limit){
		final List<T> list = new ArrayList<T>();
		try{
			getSqlSession().select(sqlid, param, new RowBounds(offset, limit), new ResultHandler(){
				@SuppressWarnings("unchecked")
				@Override
				public void handleResult(ResultContext con){
					list.add((T)con.getResultObject());
				}
			});
		}catch(Exception e){
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.warn("## IBatisDaoPlus.selectBounds(sqlid,offset,param,limit) : " + e.getMessage(), e);
			SqlSession session = factory.openSession();
			session.select(sqlid, param, new RowBounds(offset, limit), new ResultHandler(){
				@SuppressWarnings("unchecked")
				@Override
				public void handleResult(ResultContext con){
					list.add((T)con.getResultObject());
				}
			});
			session.close();
		}
		return list;
	}
}
