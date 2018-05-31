package org.yipuran.mybatis.spi;

import java.util.ArrayList;
import java.util.List;

import org.yipuran.xml.AbstractXmlHandler;
import org.yipuran.xml.XmlParser;
/**
 * Configuration.xml spi.
 * for get enviromtent name
 */
public final class SpiConfiguration{
	private static SpiConfiguration inst;
	private ConfigurationParser parser;

	/**
	 * private constructor.
	 */
	private SpiConfiguration(){
		XmlParser<ConfigurationParser> xmlparser = AbstractXmlHandler.createParser("Configuration", new ConfigHandler());
		try{
			this.parser = xmlparser.parse();
		}catch(Exception e){
			throw new RuntimeException("Configuration.xml parse Error : " + e.getMessage(), e);
		}
	}
	/**
	 * getInstance.
	 * @return SpiConfiguration
	 */
	public static final synchronized SpiConfiguration getInstance(){
		if (inst==null) inst = new SpiConfiguration();
		return inst;
	}
	/**
	 * getDefaultEnviroment.
	 * @return String
	 */
	public String getDefaultEnviroment(){
		return this.parser.getDefaultEnviroment();
	}
	/**
	 * get List enviromentNames.
	 * @return List<String>
	 */
	public List<String> enviromentNames(){
		return this.parser.getEnviromentNames();
	}
	/**
	 * isDefault check.
	 * @param name enviromentName
	 * @return true = default
	 */
	public boolean isDefault(String name){
		return this.parser.getDefaultEnviroment().equals(name);
	}
	/**
	 * speifyNames.
	 * @return List<String>
	 */
	public List<String> speifyNames(){
		List<String> list = new ArrayList<String>();
		for(String s : this.parser.getEnviromentNames()){
			if (!s.equals(this.parser.getDefaultEnviroment())){
				list.add(s);
			}
		}
		return list;
	}
}
