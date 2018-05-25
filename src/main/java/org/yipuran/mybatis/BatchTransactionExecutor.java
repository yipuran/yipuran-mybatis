package org.yipuran.mybatis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * BATCH処理用トランザクション インターセプター
 */
public class BatchTransactionExecutor implements MethodInterceptor{
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * コンストラクタ.
	 * @param sqlSessionFactory SqlSessionFactory
	 */
	@Inject
	public BatchTransactionExecutor(SqlSessionFactory sqlSessionFactory){
		this.sqlSessionFactory = sqlSessionFactory;
	}

	/* @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation m) throws Throwable {
		 Object rtn = null;
		 SqlSession session = null;
		 try{
			 session = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			 session.getConnection().setAutoCommit(false);
			 Field setField = this.getAnotatedField(ControleSession.class, m.getMethod().getDeclaringClass());
			 setField.setAccessible(true);
			 setField.set(m.getThis(), session);
			 rtn = m.proceed();
			 session.commit();
		 }catch(Exception e){
			 if (session != null){
				 session.rollback();
			 }
			 throw e;
		 }finally{
			 if (session != null) session.close();
		 }
		 return rtn;
	}
	/**
	 * getAnotatedField.
	 * @param a Annotation
	 * @param cls Class
	 * @return Field
	 */
	private Field getAnotatedField(Class<? extends Annotation> a, Class<?> cls){
		Field rtn = null;
		Class<?> tcls = cls;
		while(!tcls.equals(java.lang.Object.class)){
			Field[] fls = tcls.getDeclaredFields();
			for(int i = 0; i < fls.length; i++){
				Annotation[] as = fls[i].getAnnotations();
				if (as != null){
					for(int k = 0; k < as.length; k++){
						if (as[k].annotationType().equals(a)){
							rtn = fls[i];
							i = fls.length;
							break;
						}
					}
				}
			}
			if (rtn != null) return rtn;
			tcls = tcls.getSuperclass();
		}
		throw new RuntimeException("must be IBatisDao extends");
	}
}
