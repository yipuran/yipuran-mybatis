package org.yipuran.mybatis.util;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.yipuran.function.ThrowableConsumer;
import org.yipuran.function.ThrowableFunction;

/**
 * SQLProcess.
 * <PRE>
 * Datasource でＤＢ接続からSQL実行まで、config XML設定ファイル無しで処理するためのクラスです。
 * コンストラクタまたは、UnpooledDataSource の setter でＤＢ接続を準備します。
 * コンストラクタによる準備は、コンストラクタで、JDBC driver , 接続URL , 接続ユーザ , 接続パスワードを指定します。
 * コンストラクタで生成する場合は、非プールの Datasource
 *      org.apache.ibatis.datasource.unpooled.UnpooledDataSource
 * が準備されます。
 * 他のDatasourceで準備する場合は、デフォルトコンストラクタ new SQLProcess() で生成して setDatasource を実行する必要があります。
 * SQL実行メソッドは、Interface class による SQLMapper の指定と、DB接続済の SqlSession をラムダ式で実行する為の
 * Throwable な Consumer で SQL実行 あるいは、Throwable な Function で SQL実行結果の受け取りを行います。
 * SQLMap を XMLで用意することを想定していません。
 * SQL アノテーションを interface class で記述して用意することを想定しています。
 * （使用例）
 *     public interface SimpleMapper{
 *         ＠Select("SELECT * FROM users WHERE age > #{value}")
 *         public List&lt;User&gt; getList(int over);
 *     }
 *
 *
 *     SQLProcess proc = new SQLProcess("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/mydb", "uranus", "password");
 *     proc.accept(SimpleMapper.class, s-&gt;{
 *         List&lt;User&gt; list =  s.getMapper(SimpleMapper.class).getList(30);
 *         // for  age 30 over
 *     });
 *
 *     List&lt;User&gt; list = proc.apply(SimpleMapper.class, s-&gt;s.getMapper(SimpleMapper.class).getList(30));
 *
 *
 *    SQLProcess 準備に、デフォルトコンストラクタを使用して setDatasource を使用するのであれば、
 *    org.yipuran.util.GenericBuilder 等を使用して、UnpooledDataSource を生成して指定します。
 *
 *    UnpooledDataSource source = GenericBuilder.of(UnpooledDataSource::new)
 *    .with(UnpooledDataSource::setDriver, "com.mysql.jdbc.Driver")
 *    .with(UnpooledDataSource::setUrl, "jdbc:mysql://127.0.0.1:3308/mydb")
 *    .with(UnpooledDataSource::setUsername, "uranus")
 *    .with(UnpooledDataSource::setPassword, "password")
 *    .build();
 *    SQLProcess proc = new SQLProcess();
 *    proc.setDatasource(source);
 *
 * </PRE>
 * @since 4.2
 */
public class SQLProcess{
	private DataSource datasource;
	/**
	 * コンストラクタ.
	 */
	public SQLProcess(){}
	/**
	 * コンストラクタ.
	 * @param driver JDBCドライバ
	 * @param url 接続URL
	 * @param user 接続ユーザ名
	 * @param password 接続パスワード
	 */
	public SQLProcess(String driver, String url, String user, String password){
		UnpooledDataSource d = new UnpooledDataSource();
		d.setDriver(driver);
		d.setUrl(url);
		d.setUsername(user);
		d.setPassword(password);
		d.setAutoCommit(false);
		datasource = d;
	}
	/**
	 * 接続 Datasouceを設定.
	 * @param datasource UnpooledDataSource
	 */
	public void setDatasource(DataSource datasource){
		this.datasource = datasource;
	}

	/**
	 * 非トランザクションSQL実行.
	 * SqlSession の commit 実行はしない
	 * @param mapper SQLMapper の interface class
	 * @param c Throwable な SqlSession Consumer
	 */
	public void accept(Class<?> mapper, ThrowableConsumer<SqlSession> c){
		Environment environment = new Environment("deployment", new JdbcTransactionFactory(), datasource);
		Configuration config = new Configuration(environment);
		config.addMapper(mapper);
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);
		try(SqlSession session = factory.openSession()){
			c.accept(session);
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	/**
	 * SQL実行（自動コミット）.
	 * 最後に、SqlSession の commit 実行してくれる。
	 * @param mapper SQLMapper の interface class
	 * @param c Throwable な SqlSession Consumer
	 */
	@SuppressWarnings("resource")
	public void acceptUpdate(Class<?> mapper, ThrowableConsumer<SqlSession> c){
		Environment environment = new Environment("deployment", new JdbcTransactionFactory(), datasource);
		Configuration config = new Configuration(environment);
		config.addMapper(mapper);
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);
		SqlSession session = null;
		try{
			session = factory.openSession();
			c.accept(session);
			session.commit();
		}catch(Exception ex){
			if (session != null) session.rollback();
			throw new RuntimeException(ex);
		}
	}
	/**
	 * 非トランザクションSQL実行結果受信.
	 * SqlSession の commit 実行はしない
	 * @param mapper SQLMapper の interface class
	 * @param c Throwable な SqlSession Function
	 * @return R Functionで受け散る返却値
	 */
	public <R> R apply(Class<?> mapper, ThrowableFunction<SqlSession, R> c){
		Environment environment = new Environment("deployment", new JdbcTransactionFactory(), datasource);
		Configuration config = new Configuration(environment);
		config.addMapper(mapper);
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);
		try(SqlSession session = factory.openSession()){
			return c.apply(session);
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	/**
	 * SQL実行結果受信（自動コミット）.
	 * 最後に、SqlSession の commit 実行してくれる。
	 * @param mapper SQLMapper の interface class
	 * @param c Throwable な SqlSession Function
	 * @return R Functionで受け散る返却値
	 */
	@SuppressWarnings("resource")
	public <R> R applyUpdate(Class<?> mapper, ThrowableFunction<SqlSession, R> c){
		Environment environment = new Environment("deployment", new JdbcTransactionFactory(), datasource);
		Configuration config = new Configuration(environment);
		config.addMapper(mapper);
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);
		SqlSession session = null;
		try{
			session = factory.openSession();
			R r = c.apply(session);
			session.commit();
			return r;
		}catch(Exception ex){
			if (session != null) session.rollback();
			throw new RuntimeException(ex);
		}
	}
}
