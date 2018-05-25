package org.yipuran.mybatis.spi;

import java.util.List;
/**
 * Configration.xml Parser.
 */
interface ConfigurationParser{
	/**
	 * get environment tag default.
	 * @return environment tag info
	 */
	String getDefaultEnviroment();

	/**
	 * get environment name List.
	 * @return List<String>
	 */
	List<String> getEnviromentNames();
}
