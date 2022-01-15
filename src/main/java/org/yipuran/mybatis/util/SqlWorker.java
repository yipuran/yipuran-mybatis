package org.yipuran.mybatis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

/**
 * 簡易 SqlSession worker.
 * <pre>
 * （例）
 *
 * UnpooledDataSource dataSource = new UnpooledDataSource();
 * dataSource.setDriver("com.mysql.cj.jdbc.Driver");
 * dataSource.setUrl("jdbc:mysql://localhost:3306/sampleDB");
 * dataSource.setUsername("root");
 * dataSource.setPassword("password");
 *
 * SqlWorker worker = new SqlWorker(dataSource, "sql/", SampleMapper.class, ItemMapper.class);
 * try(SqlSession session = worker.getSqlSession()){
 *    // TODO
 * }
 * </pre>
 * @since 4.9
 */
public class SqlWorker {
	private SqlSessionFactory factory;

	/**
	 * コンストラクタ.
	 * @param dataSource DataSource
	 * @param path Mappper XML が存在するパス
	 * @param mapperClasses 登録対象 SQL mapper Class
	 */
    public SqlWorker(DataSource dataSource, String path, Class<?>... mapperClasses) {
       Environment environment = new Environment("deployment", new JdbcTransactionFactory(), dataSource);
       Configuration config = new Configuration(environment);
       // snake Case → camel Case
       config.setMapUnderscoreToCamelCase(true);
       for(Class<?> cls: mapperClasses){
          config.addMapper(cls);
       }
       File file = new File(Thread.currentThread().getContextClassLoader().getResource("./"+path).getPath());
       List<File> list = parse(file, new ArrayList<>(), f->f.getAbsolutePath().endsWith(".xml"));
       list.stream().forEach(f->{
       try(InputStream input = new FileInputStream(f)){
              new XMLMapperBuilder(input, config, null, config.getSqlFragments()).parse();
          }catch(Exception e){
          }
       });
       factory = new SqlSessionFactoryBuilder().build(config);
    }
    private List<File> parse(File file, List<File> list, Predicate<File> p){
        if (p.test(file)) list.add(file);
        if (file.isDirectory()){
            for(File f:file.listFiles()){
                parse(f, list, p);
            }
        }
        return list;
    }
    /**
     * 接続⇒SqlSession取得.
     * @return SqlSession
     */
    public SqlSession getSqlSession(){
        return factory.openSession();
    }
    /**
     * 接続⇒SqlSession Consumer実行
     * @param consumer SqlSession Consumer
     */
    public void execution(Consumer<SqlSession> consumer) {
        SqlSession session = factory.openSession();
        try{
            consumer.accept(session);
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
        	session.close();
        }
    }
    /**
     * 接続⇒SqlSession 例外捕捉つき Consumer実行
     * @param consumer SqlSession Consumer
     * @param error  BiConsumer<SqlSession, Throwable>
     */
    public void execution(Consumer<SqlSession> consumer, BiConsumer<SqlSession, Throwable> error) {
        SqlSession session = factory.openSession();
        try{
            consumer.accept(session);
        }catch(Exception e){
            error.accept(session, e);
            throw new RuntimeException(e);
        }finally{
            session.close();
        }
    }
    /**
     * 接続⇒SqlSession トランザクション実行
     * @param consumer SqlSession Consumer
     */
    public void transaction(Consumer<SqlSession> consumer) {
        SqlSession session = factory.openSession();
        try{
            consumer.accept(session);
            session.commit();
        }catch(Exception e){
            session.rollback();
            throw new RuntimeException(e);
        }finally{
        	session.close();
        }
    }
    /**
     * 接続⇒SqlSession 例外捕捉つき トランザクション実行
     * @param consumer SqlSession Consumer
     * @param error BiConsumer<SqlSession, Throwable>
     */
    public void transaction(Consumer<SqlSession> consumer, BiConsumer<SqlSession, Throwable> error) {
        SqlSession session = factory.openSession();
        try{
            consumer.accept(session);
            session.commit();
        }catch(Exception e){
            session.rollback();
            error.accept(session, e);
            throw new RuntimeException(e);
        }finally{
        	session.close();
        }
    }
    /**
     * 接続⇒SqlSession Function<SqlSession, R>実行
     * @param function Function<SqlSession, R>
     * @return R
     */
    public <R> R getObject(Function<SqlSession, R> function) {
        try(SqlSession session = factory.openSession()){
            return function.apply(session);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
