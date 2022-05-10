package org.yipuran.mybatis;

import java.util.Properties;

import org.mybatis.guice.XMLMyBatisModule;

public abstract class CoreBatisModule extends XMLMyBatisModule{
	public abstract Properties getProperty();

	@Override
	protected void initialize() {
		addProperties(getProperty());
	}
}
