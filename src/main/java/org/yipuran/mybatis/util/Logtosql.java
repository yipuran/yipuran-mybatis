package org.yipuran.mybatis.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.yipuran.regex.RegExpress;

/**
 * JDBCログ結果から SQL文生成.
 * <PRE>
 * org.apache.ibatis.logging.jdbc.BaseJdbcLogger が出力するログ（文字列）を解析して SQL文を生成する。
 * BaseJdbcLogger がログメッセージとして出力するのは、
 * 　　"==>  Preparing: "　　の後に続くSQL本文と、
 * 　　"==> Parameters: "　　の後に続くSQLパラメータである。
 * この２つの文字列テキストから、 SQL文を生成する。
 *
 * "==>  Preparing: "を先頭に持つ文字列テキスト
 *  と、
 * "==> Parameters: "を先頭に持つ文字列テキスト
 *  を指定することで
 * ＳＱＬを生成するメソッドと、
 * ＤＢ種類によって ＳＱＬ文法が異なる日付時刻のＳＱＬ生成は、Function&lt;String, String&gt; の setter が用意されている。
 *
 * "==> Parameters: " として取得したログ、例えば、
 * 　　　　　　　　　"==> Parameters: 2019-10-17(Date) , 2019-10-17 13:27:41"
 * に対して、
 * 　　　　// for MySQL
 * 　　　　　setDateformat(e-&gt;"STR_TO_DATE('"+e+"','%Y-%m-%d')")
 * 　　　　　setTimestampformat(e-&gt;"STR_TO_DATE('"+e+"','%Y-%m-%d %H:%i:%s')")
 * 　　　　// for Oracle
 * 　　　　　etDateformat(e-&gt;"STR_TO_DATE('"+e+"','YYYY-MM-DD')")
 * 　　　　　setTimestampformat(e-&gt;"TO_TIMESTAMP('"+e+"','YYYY-MM-DD HH24:MI:SS')")
 * のようにラムダ式で指定できる。
 *
 * </PRE>
 * @since 4.2
 */
public final class Logtosql{
	private Function<String, String> dateformat;
	private Function<String, String> timestampformat;
	private Function<String, String> timeformat;
	/**
	 * Date型 SQLフォーマットの設定.
	 * @param dateformat Date型SQL生成 Function
	 */
	public void setDateformat(Function<String, String> dateformat){
		this.dateformat = dateformat;
	}
	/**
	 * Timestamp型 SQLフォーマットの設定.
	 * @param timestampformat Timestamp型SQL生成 Function
	 */
	public void setTimestampformat(Function<String, String> timestampformat){
		this.timestampformat = timestampformat;
	}
	/**
	 * Time型 SQLフォーマットの設定.
	 * @param timeformat Time型SQL生成 Function
	 */
	public void setTimeformat(Function<String, String> timeformat){
		this.timeformat = timeformat;
	}

	/**
	 * Date型 設定済インスタンス生成
	 * @param dateformat Date型SQL生成 Function
	 * @return Logtosql
	 */
	public static Logtosql of(Function<String, String> dateformat){
		Logtosql logtosql = new Logtosql();
		logtosql.setDateformat(dateformat);
		return logtosql;
	}
	/**
	 * Date型＆Timestamp型 設定済インスタンス生成
	 * @param dateformat Date型SQL生成 Function
	 * @param timestampformat Timestamp型SQL生成 Function
	 * @return Logtosql
	 */
	public static Logtosql of(Function<String, String> dateformat, Function<String, String> timestampformat){
		Logtosql logtosql = new Logtosql();
		logtosql.setDateformat(dateformat);
		logtosql.setTimestampformat(timestampformat);
		return logtosql;
	}
	/**
	 * SQL生成.
	 * @param text "==>  Preparing: " で始まるＳＱＬ文（ログ）
	 * @param parametersText "==> Parameters: " で始まるパラメータ（ログ）
	 * @return SQL文
	 */
	public String get(String text, String parametersText){
		String param = parametersText.replaceFirst("==> Parameters: ", "");
		Pattern tptn = Pattern.compile("\\(.+\\)$");
		List<String> plist = Arrays.stream(param.split(",")).map(e->e.trim()).filter(e->tptn.matcher(e).find() || e.equals("null"))
		.map(e->{
			Matcher m = tptn.matcher(e);
			if (m.find()){
				String s = m.group().replaceFirst("\\(", "").replaceFirst("\\)", "");
				if (s.equals("String")){
					return "'" + e.substring(0, m.start()) + "'";
				}else if(s.equals("Integer")){
					return e.substring(0, m.start());
				}else if(s.equals("Long")){
					return e.substring(0, m.start());
				}else if(s.equals("Double")){
					return e.substring(0, m.start());
				}else if(s.equals("Float")){
					return e.substring(0, m.start());
				}else if(s.equals("BigDecimal")){
					return e.substring(0, m.start());
				}else if(s.equals("Date")){
					return dateformat.apply(e.substring(0, m.start()));
				}else if(s.equals("Timestamp")){
					return timestampformat.apply(e.substring(0, m.start()));
				}else if(s.equals("Time")){
					return timeformat.apply(e.substring(0, m.start()));
				}else{
					throw new RuntimeException("unknown type : " + e);
				}
			}else if(e.equals("null")){
				return "null";
			}
			return "";
		}).collect(Collectors.toList());
		return RegExpress.replace("\\?", text.replaceFirst("==>  Preparing: ", ""), (e, i)->plist.get(i));
	}
}
