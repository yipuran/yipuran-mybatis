package org.yipuran.mybatis.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
//import org.yipuran.xml.AbstractXmlHandler;
/**
 * Configration.xml XML parse Handler.
 */
class ConfigHandler extends AbstractXmlHandler<ConfigurationParser>{
	private Stack<String> stack;
	String defaultEnviroment;
	List<String> enviromentlist;

	/* (非 Javadoc)
	 * @see org.yipuran.util.xml.AbstractXmlHandler#result()
	 */
	@Override
	public ConfigurationParser result(){
		return new ConfigurationParser(){
			@Override
			public String getDefaultEnviroment(){
				return ConfigHandler.this.defaultEnviroment;
			}
			@Override
			public List<String> getEnviromentNames(){
				return ConfigHandler.this.enviromentlist;
			}
		};
	}
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException{
		this.stack.push(name);;
		StringBuilder sb = new StringBuilder();
		for(String s : this.stack){
			sb.append("/" + s);
		}
		String xpath = sb.toString();
		if ("/configuration/environments".equals(xpath)){
			int length = attributes.getLength();
			for(int i = 0; i < length; i++){
				if (attributes.getQName(i).equals("default")) this.defaultEnviroment = attributes.getValue(i);
			}
		}else if ("/configuration/environments/environment".equals(xpath)){
			int length = attributes.getLength();
			for(int i = 0; i < length; i++){
				if (attributes.getQName(i).equals("id")) this.enviromentlist.add(attributes.getValue(i));
			}
		}
	}
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException{
		this.stack.pop();
	}
	@Override
	public void startDocument() throws SAXException{
		this.stack = new Stack<String>();
		this.enviromentlist = new ArrayList<String>();
	}
	@Override
	public void endDocument() throws SAXException{
	}
}
