package com.gongml.companyscort.utils.groovy;

import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import groovy.lang.Binding;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

/** 
* @Description:
* @Author: Gongml
* @Date: 2018-12-25 
*/ 
@Component
public class GroovyShell2 extends GroovyShell {
	
	@Autowired
	private ApplicationContext applicationContext;

	public GroovyShell2(CompilerConfiguration2 compilerConfiguration2) {
        super(new Binding(), compilerConfiguration2);
    }
	
	@Override
	public Script parse(GroovyCodeSource codeSource) throws CompilationFailedException {
		try {
			Class<?> scriptClass=getClassLoader().parseClass(codeSource, false);
			GrovvyFunction script = (GrovvyFunction) scriptClass.newInstance();
			script.setApplicationContext(applicationContext);
	        script.setBinding(getContext());
	        return script;
		} catch (Exception e) {
			throw new RuntimeException("groovy 实例异常",e);
		}finally {
			resetLoadedClasses();
			getClassLoader().clearCache();
		}
	}
}
