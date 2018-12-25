package com.gongml.companyscort.utils.groovy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Script;
import org.springframework.context.ApplicationContext;

/**
* @Description:函数脚本
* @Author: Gongml
* @Date: 2018-12-25
*/
public class GrovvyFunction extends Script {

	private final static Logger Logger = LoggerFactory.getLogger(GrovvyFunction.class);

	private ApplicationContext context;

	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}

	public GrovvyFunction() {
	}

	@Override
	public Object run() {
		return null;
	}


}
