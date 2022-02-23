package org.yipuran.mybatis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;

/**
 * Configuration Tool
 * @since 4.10
 */
public final class ConfigurationTool {
	private ConfigurationTool(){}
	/**
	 * 現在のリソース上に存在する XMLファイルのリスト取得
	 * @return List File
	 */
	public static List<File> findXML(){
		File file = new File(Thread.currentThread().getContextClassLoader().getResource("./").getPath());
		return searchMapFiles(file, new ArrayList<>(), f->f.getName().endsWith(".xml"));

	}

	private static List<File> findXML(String path){
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
		return searchMapFiles(file, new ArrayList<>(), f->f.getName().endsWith(".xml"));
	}
	private static List<File> searchMapFiles(File file, List<File> list, Predicate<File> p){
		if (p.test(file)) list.add(file);
		if (file.isDirectory()){
			for(File f:file.listFiles()){
				searchMapFiles(f, list, p);
			}
		}
		return list;
	}


    /**
     * Configuration に全てのMapper XMLを読み込ませる。
     * @param config Configuration
     * @return parseできたXMLファイル のリスト
     */
    public static List<File> mapperScan(Configuration config) {
    	return findXML("./").stream().map(f->{
			try(InputStream input = new FileInputStream(f)){
				new XMLMapperBuilder(input, config, f.getAbsolutePath(), config.getSqlFragments()).parse();
				return f;
			}catch(Exception e){
				return null;
			}
		}).filter(e->e != null).collect(Collectors.toList());
	}
    /**
     * 指定Pathは以下の全てのMapper XML を Configuration に読み込ませる。
     * @param config Configuration
     * @return parseできたXMLファイル のリスト
     */
    public static List<File> mapperScan(Configuration config, String path) {
    	return findXML(path).stream().map(f->{
			try(InputStream input = new FileInputStream(f)){
				new XMLMapperBuilder(input, config, f.getAbsolutePath(), config.getSqlFragments()).parse();
				return f;
			}catch(Exception e){
				return null;
			}
		}).filter(e->e != null).collect(Collectors.toList());
    }
    /**
     * Configuration に読み込まれた Mapper XML の List<File>を取得する
     * @param config Configuration
     * @return  List<File> g
     */
    public static List<File> getSQLMapXmlList(Configuration config){
        return config.getMappedStatements().stream().map(m->m.getResource().toString())
        		.filter(e->e.endsWith(".xml")).distinct().map(p->new File(p)).collect(Collectors.toList());
    }
}
