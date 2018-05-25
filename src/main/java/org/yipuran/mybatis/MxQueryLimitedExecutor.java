package org.yipuran.mybatis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
/**
 * 複数ＤＢクエリー実行限定 インターセプター.
 */
final class MxQueryLimitedExecutor implements MethodInterceptor{
	@Inject @ControleSession private Map<String, SqlSessionFactory> factorymap;

	/* (非 Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation m) throws Throwable{
		Object rtn = null;
		Map<String, SqlSession> sessionmap = new HashMap<String, SqlSession>();
		try{
			for(String key : this.factorymap.keySet()){
				sessionmap.put(key, this.factorymap.get(key).openSession(false));
			}
			Field setField = this.getAnotatedField(ControleSession.class, m.getMethod().getDeclaringClass());
			setField.setAccessible(true);
			setField.set(m.getThis(), sessionmap);
			rtn = m.proceed();
		}catch(Exception e){
			throw e;
		}finally{
			for(SqlSession session : sessionmap.values()){
				session.close();
		   }
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
		throw new RuntimeException("must be MultiIBatisDao extends");
	}
}
